package com.lucasxvirtual.financasmercado

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.lucasxvirtual.financasmercado.extensions.isValidInvoiceNumber

class InvoiceAnalyzer(
    private val onAccessKeyAcquired: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val regex = "\\d{4} \\d{4} \\d{4} \\d{4} \\d{4} \\d{4} \\d{4} \\d{4} \\d{4} \\d{4} \\d{4}".toRegex()
    private val regexNoSpace = "\\d{44}".toRegex()

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            recognizer.process(image)
                .addOnSuccessListener {
                    regex.find(it.text)?.value?.let { value ->
                        if (validateAccessKey(value)) {
                            onAccessKeyAcquired(value)
                        }
                    } ?: regexNoSpace.find(it.text.replace("[\\n\\s]".toRegex(), ""))?.value?.let { value ->
                        if (validateAccessKey(value)) {
                            onAccessKeyAcquired(value)
                        }
                    }
                }
                .addOnCompleteListener { imageProxy.close() }
        }
    }

    private fun validateAccessKey(key: String): Boolean {
        val formattedKey = key.filterNot { it.isWhitespace() }
        return formattedKey.isValidInvoiceNumber()
    }

    fun close() {
        recognizer.close()
    }
}