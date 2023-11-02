package com.lucasxvirtual.financasmercado.ui.importinvoice

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.lucasxvirtual.financasmercado.InvoiceAnalyzer
import com.lucasxvirtual.financasmercado.R
import com.lucasxvirtual.financasmercado.extensions.getAllInvoicesIds
import com.lucasxvirtual.financasmercado.extensions.isValidInvoiceNumber
import com.lucasxvirtual.financasmercado.ui.MaskVisualTransformation
import com.lucasxvirtual.financasmercado.ui.atom.Ad
import com.lucasxvirtual.financasmercado.ui.atom.PrimaryButton
import com.lucasxvirtual.financasmercado.ui.theme.FinancasMercadoTheme
import com.lucasxvirtual.financasmercado.viewmodels.ImportInvoiceViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun ImportInvoiceScreen(
    viewModel: ImportInvoiceViewModel = hiltViewModel(),
    hasCameraPermission: Boolean,
    onBackPressed: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    ImportInvoiceScreen(
        uiState,
        viewModel::submitAccessKey,
        viewModel::submitAll,
        viewModel::onDialogShown,
        onBackPressed,
        hasCameraPermission
    )
}

@Composable
fun ImportInvoiceScreen(
    uiState: ImportInvoiceUIState,
    submitAccessKey: (String) -> Unit = {},
    submitAll: (List<String>?) -> Unit = {},
    onDialogShown: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    hasCameraPermission: Boolean
) {
    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }

    var invoiceNumbers by rememberSaveable {
        mutableStateOf("")
//        mutableStateOf("33230917833301001332652100001084411427869589")
    }

    val invoiceAnalyzer = remember {
        InvoiceAnalyzer {
            if (invoiceNumbers.isEmpty()) {
                invoiceNumbers = it.filter { numberString -> numberString.isDigit() }
                Log.d("LUCASDEBUG", invoiceNumbers)
                submitAccessKey(invoiceNumbers)
            }
        }
    }

    var mode by rememberSaveable {
        if (hasCameraPermission) {
            mutableStateOf(InsertType.CAMERA)
        } else {
            mutableStateOf(InsertType.TYPING)
        }
    }

    val focusRequester = remember { FocusRequester() }

    var showWhereIsDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (!it) {
            mode = mode.switch()
            Toast.makeText(
                context,
                context.getString(R.string.enable_camera_access),
                Toast.LENGTH_SHORT).show()
        }
    }

    if (uiState.showDialog) {
        ImportingDialog(
            titleText = uiState.text?.let { stringResource(id = it) }.orEmpty(),
            accessKey = invoiceNumbers,
            success = uiState is ImportInvoiceUIState.Success,
            error = uiState is ImportInvoiceUIState.Error
        )
        if (uiState !is ImportInvoiceUIState.Loading) {
            LaunchedEffect(Unit) {
                delay(DELAY_TO_DISMISS_DIALOG)
                onDialogShown()
                invoiceNumbers = ""
            }
        }
    }

    if (uiState is ImportInvoiceUIState.Close) {
        LaunchedEffect(uiState) {
            invoiceAnalyzer.close()
            onBackPressed()
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {

        if (showWhereIsDialog) {
            WhereIsDialog {
                showWhereIsDialog = false
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (mode == InsertType.CAMERA) {
                AndroidView(
                    modifier = Modifier.fillMaxHeight(0.5f),
                    factory = { context ->
                        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        PreviewView(context).apply {
                            setBackgroundColor(Color.White.toArgb())
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            scaleType = PreviewView.ScaleType.FILL_START
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }.also { previewView ->
                            previewView.controller = cameraController
                            cameraController.bindToLifecycle(lifecycleOwner)
                            cameraController.setImageAnalysisAnalyzer(
                                ContextCompat.getMainExecutor(context),
                                invoiceAnalyzer
                            )
                        }
                    },
                    onRelease = {
                        cameraController.unbind()
                    }
                )
                HowToText(
                    insertType = mode,
                    onClick = { showWhereIsDialog = true },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 150.dp)
                )
            } else {
                LaunchedEffect(Unit) {
                    if (mode == InsertType.TYPING) {
                        try {
                            focusRequester.requestFocus()
                        } catch (_: Exception) {}
                    }
                }
                TextField(
                    value = invoiceNumbers,
                    onValueChange = {
                        if (it.length <= INVOICE_MAX_INPUT) {
                            invoiceNumbers = it.filter { value -> value.isDigit() }
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    visualTransformation = MaskVisualTransformation(INVOICE_MASK),
                    shape = RoundedCornerShape(4.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onTertiary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onTertiary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    isError = invoiceNumbers.let {
                        it.length == INVOICE_MAX_INPUT && !it.isValidInvoiceNumber()
                    },
                    minLines = 5,
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp)
                        .padding(horizontal = 20.dp)
                        .border(4.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp))
                        .focusRequester(focusRequester)
                )
                HowToText(
                    insertType = mode,
                    onClick = { showWhereIsDialog = true },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp)
                )
                PrimaryButton(
                    text = stringResource(R.string.proceed),
                    action = { submitAccessKey(invoiceNumbers) },
                    enabled = invoiceNumbers.isValidInvoiceNumber(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                )
            }
            Text(
                text = stringResource(R.string.or),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
            )
            val buttonText = if (mode == InsertType.CAMERA) {
                stringResource(R.string.insert_manually)
            } else {
                stringResource(R.string.use_camera)
            }
            PrimaryButton(
                text = buttonText,
                action = {
                    mode = mode.switch()
                    if (mode == InsertType.CAMERA) {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheckResult != PackageManager.PERMISSION_GRANTED) {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )

//            if (BuildConfig.DEBUG) {
                PrimaryButton(
                    text = "Cadastrar tudo",
                    action = {
                        submitAll(context.getAllInvoicesIds())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            Ad(Modifier.align(Alignment.CenterHorizontally))
//            }
        }
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp)
                .background(Color.Black.copy(0.1f), RoundedCornerShape(50))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = stringResource(R.string.close),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun HowToText(
    insertType: InsertType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraImportText = buildAnnotatedString {
        val howToImportText = if (insertType == InsertType.CAMERA) {
            stringResource(id = R.string.how_to_import_invoice_camera)
        } else {
            stringResource(R.string.insert_invoice_access_key)
        }
        val whereIsText = stringResource(id = R.string.but_where_is_this)
        val fullText = "$howToImportText $whereIsText"
        append("$howToImportText ")
        withAnnotation("tag", "annotation") {
            append(whereIsText)
        }
        addStyle(
            style = SpanStyle(
                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                letterSpacing = MaterialTheme.typography.bodyMedium.letterSpacing,
                color = MaterialTheme.colorScheme.primary
            ),
            0,
            howToImportText.length
        )
        addStyle(
            style = SpanStyle(
                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                letterSpacing = MaterialTheme.typography.bodyMedium.letterSpacing,
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline
            ),
            fullText.indexOf(whereIsText),
            fullText.length
        )
    }
    ClickableText(
        text = cameraImportText,
        onClick = {
            cameraImportText.getStringAnnotations(it, it).firstOrNull()?.tag?.let {
                onClick()
            }
        },
        modifier = modifier
    )
}

@Composable
fun ImportingDialog(
    titleText: String,
    accessKey: String,
    success: Boolean,
    error: Boolean
) {
    Dialog(
        onDismissRequest = { /*TODO*/ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 10.dp,
                focusedElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = titleText,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp)
            )
            when {
                success -> Icon(
                    painter = painterResource(id = R.drawable.ic_done),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(46.dp)
                )
                error -> Icon(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(46.dp)
                )
                else -> CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(40.dp)
                )
            }
            Text(
                text = accessKey,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp)
            )
        }
    }
}

@Composable
fun WhereIsDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Image(
            painter = painterResource(id = R.drawable.invoice_id_image),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

enum class InsertType {
    CAMERA, TYPING;

    fun switch() = if (this == CAMERA) TYPING else CAMERA
}

private const val INVOICE_MASK = "#### #### #### #### #### #### #### #### #### #### ####"
private const val INVOICE_MAX_INPUT = 44

@Composable
@Preview
fun PreviewSuccessDialog() {
    FinancasMercadoTheme {
        ImportingDialog(
            titleText = ImportInvoiceUIState.Success.text?.let { stringResource(id = it) }.orEmpty(),
            accessKey = "xxxx",
            success = true,
            error = false
        )
    }
}

@Composable
@Preview
fun PreviewErrorDialog() {
    FinancasMercadoTheme {
        ImportingDialog(
            titleText = ImportInvoiceUIState.Error.text?.let { stringResource(id = it) }.orEmpty(),
            accessKey = "xxxx",
            success = false,
            error = true
        )
    }
}

@Composable
@Preview
fun PreviewLoadingDialog() {
    FinancasMercadoTheme {
        ImportingDialog(
            titleText = ImportInvoiceUIState.Loading.text?.let { stringResource(id = it) }.orEmpty(),
            accessKey = "xxxx",
            success = false,
            error = false
        )
    }
}

private val DELAY_TO_DISMISS_DIALOG = 3.seconds
