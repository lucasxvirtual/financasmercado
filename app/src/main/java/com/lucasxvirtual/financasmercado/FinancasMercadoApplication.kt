package com.lucasxvirtual.financasmercado

import android.app.Application
import com.google.firebase.FirebaseApp
import com.lucasxvirtual.financasmercado.data.Repository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class FinancasMercadoApplication: Application() {

    @Inject
    lateinit var repository: Repository

    private val job = SupervisorJob()

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        CoroutineScope(job).launch {
            repository.checkForNotificationTokenToSend()
        }
    }

    override fun onTerminate() {
        job.cancel()
        super.onTerminate()
    }
}