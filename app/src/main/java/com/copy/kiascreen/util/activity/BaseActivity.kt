package com.copy.kiascreen.util.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.copy.kiascreen.R
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.AlertUtil

abstract class BaseActivity<B : ViewBinding, VM : ViewModel> (private val transitionMode: TransitionMode) : AppCompatActivity() {
    private var _binding : ViewBinding? = null
    var user : User? = null
    abstract val viewModel : VM
    var backBtnTime :Long = 0

    abstract fun initView()

    abstract fun inflateLayout(layoutInflater: LayoutInflater) : B

    @Suppress("UNCHECKED_CAST")
    protected val binding : B
        get() = _binding as B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (transitionMode) {
            TransitionMode.HORIZON -> overridePendingTransition(R.anim.activity_horizon_enter, R.anim.activity_animation_none)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.activity_vertical_enter, R.anim.activity_animation_none)
            else -> Unit
        }
        _binding = inflateLayout(layoutInflater)

        setContentView(requireNotNull(_binding).root)

        getDataFromIntent()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("user", User::class.java)
        }
        else {
            intent?.getParcelableExtra<User>("user")
        }

        Log.d("loginTest", "onNewIntent ${user?.id}")
    }

    abstract fun getDataFromIntent()

    override fun finish() {
        super.finish()

        when (transitionMode) {
            TransitionMode.HORIZON -> overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_horizon_exit)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_vertical_exit)
            else -> Unit
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backBtnTime + 2000) {
            backBtnTime = System.currentTimeMillis()
            return
        }

        if (System.currentTimeMillis() <= backBtnTime + 2000) {
            if (System.currentTimeMillis() <= backBtnTime + 2000) {
                AlertUtil.makeFinishAlert(this, DialogInterface.OnClickListener { _, _ -> finish()  }, DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
            }
        }
    }
}

enum class TransitionMode {
    NONE,
    HORIZON,
    VERTICAL,
    FADE
}