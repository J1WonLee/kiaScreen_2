package com.copy.kiascreen.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.copy.kiascreen.login.LoginActivity
import com.copy.kiascreen.R
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.setting.SettingActivity

class NotificationUtil {
    companion object {
        fun makeNotification(context : Context, msg : String, title : String, target : String, channelId : String) : Boolean  {
            return try {
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                makeNotiChannel(channelId, notificationManager)

                Log.d("fcmTest", "makeNotification msg = $msg, title = $title, target = $target")
                val intent = when(target) {
                    "comp" -> {
                        Log.d("fcmTest", "target = comp")
                        Intent(Intent.ACTION_VIEW, Uri.parse("myapp://sample?target=comp"))
                    }
                    "setting" -> {
                        Log.d("fcmTest", "target = setting")
                        Intent(Intent.ACTION_VIEW, Uri.parse("myapp://sample?target=setting"))

                    }
                    else -> {
                        Intent(context, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    }
                }

                val pendingIntent =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.getActivity(context, 0 , intent, PendingIntent.FLAG_IMMUTABLE)
                    }
                    else {
                        PendingIntent.getActivity(context, 0 , intent, 0)
                    }


                val builder = NotificationCompat.Builder(context, channelId)
                    .setContentTitle(title)
                    .setContentText(msg + String(Character.toChars(0x1F609)))
                    .setSmallIcon(R.drawable.icon_home_1)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
//                    .addAction(R.drawable.icon_home_1 , "확인", null)
//                    .addAction(R.drawable.icon_arrow_right_1, "취소", null)
                    .build()

                notificationManager.notify(0, builder)
                true
            } catch (e : java.lang.Exception) {
                Log.d("workerTest", "failed ${e.message}")
                false
            }
        }

        private fun makeNotiChannel(channelId : String, notificationManager: NotificationManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelId, "channelName", importance).apply {
                    description = "channelDescription"
                }

                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}