package com.lucasxvirtual.financasmercado.ui.tutorial

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucasxvirtual.financasmercado.R
import com.lucasxvirtual.financasmercado.extensions.checkNotificationEnabled
import com.lucasxvirtual.financasmercado.ui.atom.SecondaryButton
import com.lucasxvirtual.financasmercado.ui.molecule.EnableNotification
import com.lucasxvirtual.financasmercado.viewmodels.TutorialViewModel
import kotlinx.coroutines.launch

@Composable
fun TutorialScreen(
    viewModel: TutorialViewModel = hiltViewModel(),
    onTutorialFinished: () -> Unit
) {
    TutorialScreen { it ->
        viewModel.onTutorialFinished(it)
        onTutorialFinished()
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TutorialScreen(onTutorialFinished: (Boolean) -> Unit) {
    val context = LocalContext.current
    val notificationEnabled = remember {
        context.checkNotificationEnabled()
    }
    val pagerState = rememberPagerState {
        if (notificationEnabled) 3 else 4
    }
    var currentPage by remember {
        mutableIntStateOf(0)
    }
    var notificationChecked by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }
    val coroutineScope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp, vertical = 40.dp)
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                when (it) {
                    0 -> PageOne()
                    1 -> PageTwo()
                    2 -> PageThree()
                    3 -> {
                        PageFour { checked -> notificationChecked = checked }
                    }
                }
            }
        }
        val onLastPage = currentPage == pagerState.pageCount - 1
        val text = if (onLastPage) {
            stringResource(R.string.finish)
        } else {
            stringResource(id = R.string.next)
        }
        SecondaryButton(
            text = text,
            action = {
                if (!onLastPage) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                    }
                } else {
                    onTutorialFinished(notificationChecked)
                }
            },
            borderColor = Color.White,
            color = Color.White,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
fun ColumnScope.PageOne() {
    Image(
        painter = painterResource(id = R.drawable.ic_logo),
        contentDescription = null,
        modifier = Modifier
            .size(85.dp)
            .align(Alignment.CenterHorizontally)
    )
    Text(
        text = stringResource(R.string.welcome),
        textAlign = TextAlign.Center,
        color = Color.White,
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 20.dp)
    )
    Text(
        text = stringResource(R.string.welcome_copy),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(top = 20.dp)
    )
    Text(
        text = stringResource(R.string.welcome_control_monthly_spend),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 20.dp)
    )
    Text(
        text = stringResource(R.string.welcome_price_float),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
    Text(
        text = stringResource(R.string.welcome_quotation),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
fun ColumnScope.PageTwo() {
    Text(
        text = stringResource(R.string.step_one),
        textAlign = TextAlign.Center,
        color = Color.White,
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 20.dp)
    )
    Text(
        text = stringResource(R.string.step_one_copy),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(top = 20.dp)
    )
    Image(
        painter = painterResource(id = R.drawable.invoice_id_image),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        contentDescription = null
    )
    Text(
        text = stringResource(R.string.step_one_alt_import),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun ColumnScope.PageThree() {
    Text(
        text = stringResource(R.string.step_two),
        textAlign = TextAlign.Center,
        color = Color.White,
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 20.dp)
    )
    Text(
        text = stringResource(R.string.step_two_copy),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(top = 20.dp)
    )
    Text(
        text = stringResource(R.string.step_two_monthly),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 20.dp)
    )
    Text(
        text = stringResource(R.string.step_two_product),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
    Text(
        text = stringResource(R.string.step_two_invoice),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
    Text(
        text = stringResource(R.string.step_two_analyze),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(top = 20.dp)
    )
}

@Composable
fun ColumnScope.PageFour(notificationStatus: (Boolean) -> Unit) {
    Text(
        text = stringResource(R.string.step_four),
        textAlign = TextAlign.Center,
        color = Color.White,
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 20.dp)
    )
    Text(
        text = stringResource(R.string.enable_notification_copy),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(top = 20.dp)
    )
    Text(
        text = stringResource(R.string.disable_notification_at_any_moment),
        textAlign = TextAlign.Start,
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 20.dp)
    )
    EnableNotification(notificationStatus = notificationStatus)
}