package com.copy.kiascreen.util.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.copy.kiascreen.util.notification.NotificationUtil

class NotiWorker(private val context : Context, workParameters : WorkerParameters) : Worker(context , workParameters) {
    private val channelId = "45"
    override fun doWork(): Result {
        val target = inputData.getString(TARGET_KEY)
        val title = inputData.getString(TITLE_KEY)
        val msg = inputData.getString(MSG_KEY)

        var success = NotificationUtil.makeNotification(context, msg!!,title!!, target!!, channelId)

        return if (success) {
            Result.success()
        }
        else {
            Result.failure()
        }
    }

    companion object {
        const val MSG_KEY = "msg_Key"
        const val TITLE_KEY = "title_Key"
        const val TARGET_KEY = "target_key"
    }
}

