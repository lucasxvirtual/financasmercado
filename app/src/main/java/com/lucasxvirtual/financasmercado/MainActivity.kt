package com.lucasxvirtual.financasmercado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.ads.MobileAds
import com.lucasxvirtual.financasmercado.ui.FinancasMercadoApp
import com.lucasxvirtual.financasmercado.ui.theme.FinancasMercadoTheme
import com.lucasxvirtual.financasmercado.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.hasSeenTutorial.value == null
        }
        setContent {
            FinancasMercadoTheme {
                val hasSeenTutorial by viewModel.hasSeenTutorial.collectAsState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    hasSeenTutorial?.let {
                        FinancasMercadoApp(it)
                    }
                }
            }
        }
        MobileAds.initialize(this) {}
    }
}
