package com.copy.kiascreen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.copy.kiascreen.application.KiaSampleApplication
import com.copy.kiascreen.coach.CoachActivity
import com.copy.kiascreen.databinding.ActivityIntroBinding
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.login.LoginViewModel
import com.copy.kiascreen.registry.dialog.FragmentMarketTerm
import com.copy.kiascreen.room.RoomUtil
import com.copy.kiascreen.room.successOrNull
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.AlertUtil
import com.copy.kiascreen.util.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {
    private lateinit var binding : ActivityIntroBinding
    private lateinit var introImg : AppCompatImageView
    private lateinit var connManager : ConnectivityManager
    private lateinit var introTxt : TextView

    private val viewModel : LoginViewModel by viewModels()

    private var user : User? = null
    val reGrants = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUser()
        initView()
//        requestPermissions()


        if (chkInternet()) {
            Handler().postDelayed({
                requestPermission()
                listenResult()
//                setAnimation()
            },1500)
        }
        else {
            Toast.makeText(this , "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show()
            finish()
        }
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

    private fun requestPermission() {
        var num = 0
        var requestList = arrayListOf<String>()
        for (item in PermissionUtil.PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, item) != PackageManager.PERMISSION_GRANTED) {
                num++
                requestList.add(item)
            }
        }

        if (num > 0) {
//            ActivityCompat.requestPermissions(this, requestList.toArray(arrayOf()), 0)
            FragmentIntroPermission().show(supportFragmentManager, null)
        }
        else {
            moveCoachOrMain()
        }
    }

    private fun listenResult() {
        supportFragmentManager.setFragmentResultListener("requestKey", this) { requestKey, bundle ->
            require(requestKey == "requestKey")
//            setAnimation()
            moveCoachOrMain()
        }
    }

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


    private fun moveCoachOrMain() {
        val intent = if (KiaSampleApplication.prefs.coachFlag) {
            Intent(this, BuildCompActivity::class.java)
        }
        else {
            Intent(this, CoachActivity::class.java)
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
            is RoomUtil.Loading -> Log.d("Introtest", "onLoading!!")
            is RoomUtil.Success -> {
                if (result.successOrNull() != null) {
                    Log.d("Introtest", "result success not null!!")
                    KiaSampleApplication.prefs.loginFlag = true
                    user = result.data as User
                }
                else {
                    user = null
                }

            }
            is RoomUtil.Error -> {
                Log.d("Introtest", "result Error null!!")
                user = null
            }
        }
    }

    override fun onBackPressed() { }
}