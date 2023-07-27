package com.copy.kiascreen

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.copy.kiascreen.application.KiaSampleApplication
import com.copy.kiascreen.coach.CoachActivity
import com.copy.kiascreen.databinding.ActivityIntroBinding
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.login.LoginViewModel
import com.copy.kiascreen.room.RoomUtil
import com.copy.kiascreen.room.successOrNull
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.setting.SettingActivity
import com.copy.kiascreen.util.PermissionUtil
import com.copy.kiascreen.util.activity.BaseActivity
import com.copy.kiascreen.util.activity.TransitionMode
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.internal.notify

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {
    private lateinit var binding : ActivityIntroBinding
    private lateinit var introImg : AppCompatImageView
    private lateinit var connManager : ConnectivityManager
    private lateinit var introTxt : TextView
    private var user : User? = null

    private val viewModel : LoginViewModel by viewModels()

    private var targetActivity : String = ""
    val reGrants = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("introTest", "onCreate called")
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!chkInternet()) {
            /*
            Handler().postDelayed({
            getTargetActivity()
            requestPermission()
            listenResult()
        },500)
             */
            Toast.makeText(this , "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show()
            Log.d("introTest", "chkInternet failed")
            finish()
        }

        initView()
        getUser()
        getFcmToken()
        getTargetActivity()
        if (KiaSampleApplication.prefs.initialLaunch) {
            requestPermission()
            listenResult()
        }
        else {
            moveCoachOrMain()
        }


        Log.d("introTest", "onCreate end")
    }

    override fun onStart() {
        super.onStart()
    }

    private fun initView() {
        introImg = binding.introImg
        introTxt = binding.appTitleTv
    }

    private fun chkInternet() : Boolean {
        connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connManager.activeNetwork
            network != null
        } else {
            var networkInfo = connManager.activeNetworkInfo
            networkInfo != null
        }
    }



    private fun requestPermission() {
        Log.d("introTest", "requestPermission called")
        try {
            Log.d("introTest", "try requestPermission called")
            var num = 0
            var requestList = arrayListOf<String>()
            val fragmentManager = supportFragmentManager
            for (item in PermissionUtil.PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this, item) != PackageManager.PERMISSION_GRANTED) {
                    num++
                    requestList.add(item)
                }
            }

            if (num > 0) {
                FragmentIntroPermission().show(fragmentManager, null)
            }
            else {
                moveCoachOrMain()
            }
        } catch (e : Exception) {
            Log.d("introTest", "Error = ${e.message}")
        }
    }

    private fun listenResult() {
        Log.d("introTest", "listenResult called")
        supportFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            require(requestKey == "requestKey")
            Log.d("introTest", "setFragmentResultListener called")
//            setAnimation()
            moveCoachOrMain()
        }
    }

    private fun moveCoachOrMain() {
        Log.d("introTest", "moveCoachOrMain called")
        KiaSampleApplication.prefs.initialLaunch = true

        val intent = if (targetActivity.isNullOrEmpty()) {
            if (KiaSampleApplication.prefs.coachFlag) {
                Intent(this, BuildCompActivity::class.java)
            }
            else {
                Intent(this, CoachActivity::class.java)
            }
        }
        else {
           when(targetActivity) {
               "comp" -> Intent(this, BuildCompActivity::class.java)
               "setting" -> Intent(this, SettingActivity::class.java)
               else -> {
                   Intent(this, BuildCompActivity::class.java)
               }
           }
        }

        user?.let {
            intent.putExtra("user", user)
        }

        startActivity(intent)
        finish()
    }



    private fun getUser() {
        if (KiaSampleApplication.prefs.autoLoginFlag && KiaSampleApplication.prefs.autoLoginId!!.isNotEmpty()) {
            repeatOnStarted {
                viewModel.logInSharedFlow.collect {
                    handleLoginResult(it)
                }
            }
            viewModel.autoLogin(KiaSampleApplication.prefs.autoLoginId!!)
        }
    }



    private fun handleLoginResult(result : RoomUtil<*>) {
        when(result) {
            is RoomUtil.Loading -> Log.d("introtest", "onLoading!!")
            is RoomUtil.Success -> {
                if (result.successOrNull() != null) {
                    Log.d("introtest", "result success not null!!")
                    KiaSampleApplication.prefs.loginFlag = true
                    user = result.data as User
                }
                else {
                    Log.d("introtest", "result success null!!")
                    user = null
                }

            }
            is RoomUtil.Error -> {
                Log.d("introtest", "result Error null!!")
                user = null
            }
        }
    }

    override fun onBackPressed() { }

    override fun onPause() {
        super.onPause()
        Log.d("introtest", "onPause called!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("introtest", "onDestroy called!")
    }

    private fun getTargetActivity() {
        Log.d("introtest", "getTargetActivity called")
        intent.data?.let {
            targetActivity = it.getQueryParameter("target")?: ""
        }
        Log.d("fcmTest", "targetActiviy = $targetActivity")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("introtest", "onConfigurationChanged called!")
    }

    private fun getFcmToken() {

        if (KiaSampleApplication.prefs.fcmToken.isNullOrEmpty()) {
            // 등록된 토큰이 없음 받아온다
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("fcmTest", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result

                KiaSampleApplication.prefs.fcmToken = token
            })
        }
        Log.d("fcmTest" , "token = ${KiaSampleApplication.prefs.fcmToken}")
    }
}






/*
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when(requestCode) {
        0 -> {
            if (grantResults.isNotEmpty()) {
                var rejectPermissions = ""
                for ((i, permission) in grantResults.withIndex()) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        reGrants.add(permissions[i])
                        rejectPermissions+=permissions[i]
                    }
                }
                KiaSampleApplication.prefs.permissions = rejectPermissions
                Log.d("permissionTest", "${KiaSampleApplication.prefs.permissions}")
            }
        }
    }

    if (reGrants.isNotEmpty()) {
//            requestPermissions()
//            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:"+applicationContext.packageName)))
    }
}


// fcm 토큰 등록
    private fun getFcmToken() {

        if (KiaSampleApplication.prefs.fcmToken.isNullOrEmpty()) {
            // 등록된 토큰이 없음 받아온다
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("fcmTest", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result

                KiaSampleApplication.prefs.fcmToken = token
            })
        }
        Log.d("fcmTest" , "token = ${KiaSampleApplication.prefs.fcmToken}")
    }

    private fun requestPermissions() {
        var num = 0
        var requestList = arrayListOf<String>()
        for (item in PermissionUtil.PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, item) != PackageManager.PERMISSION_GRANTED) {
                num++
                requestList.add(item)
            }
        }

        if (num > 0) {
            ActivityCompat.requestPermissions(this, requestList.toArray(arrayOf()), 0)
        }
    }

     private fun getTargetActivity() {
        Log.d("introtest", "getTargetActivity called")
        intent.data?.let {
            targetActivity = it.getQueryParameter("target")?: ""
        }
        Log.d("fcmTest", "targetActiviy = $targetActivity")
    }

    oncreate에서 빼옴
    lifecycleScope.launch {
        Log.d("introTest", "chkInternet true -> start lifecyclerscope launch")
        delay(500)
        requestPermission()
        listenResult()
    }

 */
