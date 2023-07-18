package com.copy.kiascreen.util.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.copy.kiascreen.R

abstract class BaseActivityWithoutVM<B : ViewBinding>(private val transitionMode: TransitionMode) : AppCompatActivity() {
    private var _binding : ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding : B
        get() = _binding as B

    abstract fun initView()

    abstract fun inflateLayout(layoutInflater: LayoutInflater) : B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when(transitionMode) {
            TransitionMode.HORIZON -> overridePendingTransition(R.anim.activity_horizon_enter, R.anim.activity_animation_none)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.activity_vertical_enter, R.anim.activity_animation_none)
            else -> Unit
        }

        _binding = inflateLayout(layoutInflater)


        setContentView(requireNotNull(_binding).root)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun finish() {
        super.finish()

        when (transitionMode) {
            TransitionMode.HORIZON -> overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_horizon_exit)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_vertical_exit)
            else -> Unit
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (isFinishing) {
            when (transitionMode) {
                TransitionMode.HORIZON -> overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_horizon_exit)
                TransitionMode.VERTICAL -> overridePendingTransition(R.anim.activity_animation_none, R.anim.activity_vertical_exit)
                else -> Unit
            }
        }
    }
}