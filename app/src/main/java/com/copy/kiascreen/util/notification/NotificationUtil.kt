package com.copy.kiascreen.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.copy.kiascreen.login.LoginActivity
import com.copy.kiascreen.R
import com.copy.kiascreen.setting.SettingActivity

class NotificationUtil {
    companion object {
        fun makeNotification(context : Context, msg : String, channelId : String) : Boolean  {
            return try {
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                makeNotiChannel(channelId, notificationManager)

//                val intent = Intent(context, MainActivity::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
                var message = ""
                val intent = when(msg) {
                    "1" -> {
                        message = "moveMain"
                        Intent(context, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }

                    }
                    "2" -> {
                        message = "moveSetting"
                        Intent(context, SettingActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }

                    }
                    else -> {
                        Intent(context, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    }
                }

                val pendingIntent = PendingIntent.getActivity(context, 0 , intent, 0)

                val builder = NotificationCompat.Builder(context, channelId)
                    .setContentTitle("kiaSample")
                    .setContentText(message)
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