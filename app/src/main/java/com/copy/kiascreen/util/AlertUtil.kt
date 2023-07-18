package com.copy.kiascreen.util

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.copy.kiascreen.R

class AlertUtil {

    companion object {
        fun makeAlertDialog(activity: AppCompatActivity, msg : String) {
            AlertDialog.Builder(activity)
                .setView(R.layout.dialog_build_compar)
                .show()
                .also {
                    if (it == null) {
                        return@also
                    }

                    it.findViewById<TextView>(R.id.alert_content_tv)?.text = msg

                    val btn = it.findViewById<AppCompatButton>(R.id.alert_btn)

                    btn?.setOnClickListener { btn ->
                        it.dismiss()
                    }
                }
        }


        fun makeAlertDialogIntent(activity: AppCompatActivity, msg : String ) {
            AlertDialog.Builder(activity)
                .setView(R.layout.dialog_build_compar)
                .show()
                .also {
                    if (it == null) {
                        return@also
                    }

                    it.findViewById<TextView>(R.id.alert_content_tv)?.text = msg

                    val btn = it.findViewById<AppCompatButton>(R.id.alert_btn)

                    btn?.setOnClickListener { btn ->
                        it.dismiss()
                        activity.finish()
                    }
                }
        }

        fun <T : AppCompatActivity> makeAlertDialogLoginIntent(activity: AppCompatActivity, msg: String, targetActivity: Class<T> ) {
            AlertDialog.Builder(activity)
                .setView(R.layout.dialog_build_compar)
                .show()
                .also {
                    if (it == null) {
                        return@also
                    }

                    it.findViewById<TextView>(R.id.alert_content_tv)?.text = msg

                    val btn = it.findViewById<AppCompatButton>(R.id.alert_btn)

                    btn?.setOnClickListener { btn ->
                        it.dismiss()
                        Intent(activity, targetActivity).apply {
                            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            activity.startActivity(this)
                            activity.finish()
//                            activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
                        }

                    }
                }
        }


        fun makeFinishAlert(context : Context, pListener : DialogInterface.OnClickListener, nListener : DialogInterface.OnClickListener) {
            AlertDialog.Builder(context)
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("확인", pListener)
                .setNegativeButton("취소", nListener)
                .create().show()
        }

    }
}