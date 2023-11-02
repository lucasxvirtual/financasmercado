package com.lucasxvirtual.financasmercado

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lucasxvirtual.financasmercado.data.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FinancasMercadoFirebaseMessagingService: FirebaseMessagingService() {

    @Inject
    lateinit var repository: Repository

    private val job = SupervisorJob()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("LUCASDEBUG", "new firebase token $token")
        CoroutineScope(job).launch {
            repository.onNewNotificationToken(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        CoroutineScope(job).launch {
            repository.isNotificationEnabled().collectLatest {
                if (it) {
                    super.onMessageReceived(message)
                }
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}