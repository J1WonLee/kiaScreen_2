package com.copy.kiascreen.setting

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.viewModels
import com.copy.kiascreen.R
import com.copy.kiascreen.application.KiaSampleApplication
import com.copy.kiascreen.comparison.BuildCompActivity
import com.copy.kiascreen.databinding.ActivitySettingBinding
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.activity.BaseActivity
import com.copy.kiascreen.util.activity.TransitionMode
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingActivity : BaseActivity<ActivitySettingBinding, SettingViewModel>(TransitionMode.HORIZON) {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var pushSwitch : SwitchMaterial
    private lateinit var autoLoginSwitch : SwitchMaterial
//    private var user : User? = null

    override val viewModel: SettingViewModel by viewModels()
    override fun inflateLayout(layoutInflater: LayoutInflater) = ActivitySettingBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        setSwitchListener()
    }

    override fun initView() {
        toolbar = binding.settingToolbar
        pushSwitch = binding.pushSwitch
        autoLoginSwitch = binding.autoLoginSwitch

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
//                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getDataFromIntent() {
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        }
        else {
            intent.getParcelableExtra<User>("user")
        }
        Log.d("myPageTest", "user id = ${user?.id}")
    }

    private fun setSwitchListener() {
        if (user == null) {
            autoLoginSwitch.isEnabled = false
        }

        var push = KiaSampleApplication.prefs.pushFlag
        var login = KiaSampleApplication.prefs.autoLoginFlag
        pushSwitch.isChecked = push
        autoLoginSwitch.isChecked = login

        pushSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            KiaSampleApplication.prefs.pushFlag = isChecked
//            setPeriodicRequest(isChecked)
        }

        autoLoginSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            KiaSampleApplication.prefs.autoLoginFlag = isChecked
            if (isChecked) {
                KiaSampleApplication.prefs.autoLoginId = user?.id
            }

        }
    }

    private fun setOnCSCallListener() {
        binding.csNumberTitle.setOnClickListener {
            var callNumber = "tel:"+binding.csNumberTitle.text
            Intent(Intent.ACTION_DIAL, Uri.parse(callNumber)).apply {
                startActivity(this)
            }
        }
    }

    override fun onBackPressed() {
        if (isTaskRoot) {
            // main으로 돌려보낸다
            Intent(this, BuildCompActivity::class.java).apply {
                startActivity(this)
                finish()
            }
        }
        else {
            super.onBackPressed()
        }
    }

    /*
    추후 fcm 관리를 위함
    private fun setPeriodicRequest(flag : Boolean) {
        val workerManager = WorkManager.getInstance(this)
        if (flag) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

//            val worker = PeriodicWorkRequestBuilder<NotiWorker>(15, TimeUnit.MINUTES).setConstraints(constraints).build()
//
//
//            workerManager.enqueueUniquePeriodicWork("workerName", ExistingPeriodicWorkPolicy.KEEP ,worker)
        }
        else {
            workerManager.cancelUniqueWork("workerName")
        }
    }

     */
}