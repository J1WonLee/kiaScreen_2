package com.copy.kiascreen.fcm

import android.util.Log
import androidx.work.*
import com.copy.kiascreen.application.KiaSampleApplication
import com.copy.kiascreen.util.worker.NotiWorker
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(rm: RemoteMessage) {
        super.onMessageReceived(rm)
        Log.d("FcmTest", "OnMessageReceived Called")

        if (KiaSampleApplication.prefs.pushFlag) {
            var target = ""
            var msg = ""
            var title =""
            rm.data.let {
                for((key, value) in it) {
                    Log.d("FcmTest", "key -> ${key}, value -> ${value}")
                    when(key) {
                        "target" -> target = value
                        "msg" -> msg = value
                        "title" -> title = value
                    }
                }

                scheduleFcm(title, target, msg)
            }
        }
    }

    private fun scheduleFcm(title : String, target : String, msg : String) {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val data = Data.Builder().putString(NotiWorker.MSG_KEY, msg).putString(NotiWorker.TARGET_KEY, target).putString(NotiWorker.TITLE_KEY, title).build()

        val worker = OneTimeWorkRequestBuilder<NotiWorker>().setInputData(data).setConstraints(constraints).build()

        val workerManager = WorkManager.getInstance(this)

        workerManager.enqueueUniqueWork("fcmWorker", ExistingWorkPolicy.REPLACE, worker)
    }
}