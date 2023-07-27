package com.copy.kiascreen.registry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import com.copy.kiascreen.R
import com.copy.kiascreen.databinding.ActivityRegisterAgreeBinding
import com.copy.kiascreen.registry.fragment.FragmentInfoServiceTerm
import com.copy.kiascreen.registry.fragment.FragmentMarketServiceTerm
import com.copy.kiascreen.util.OnBackPressedListener
import com.copy.kiascreen.util.activity.BaseActivityWithoutVM
import com.copy.kiascreen.util.activity.TransitionMode
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.checkbox.MaterialCheckBox

// 회원가입시 약간 동의
class RegisterAgreeActivity : BaseActivityWithoutVM<ActivityRegisterAgreeBinding>(TransitionMode.HORIZON), RegisterTerm {
    private lateinit var agreeAll : MaterialCheckBox
    private lateinit var infoService : MaterialCheckBox
    private lateinit var marketingService : MaterialCheckBox
    private lateinit var agreeAllFrame : FrameLayout
    private lateinit var infoFrame : FrameLayout
    private lateinit var marketFrame : FrameLayout
    private lateinit var toolbar : MaterialToolbar
    private lateinit var fragmentWrapper : FrameLayout

    private var isInfoSeen = false
    private var isMarketSeen = false

    private var infoClicked = false
    private var marketClicked = false

    override fun inflateLayout(layoutInflater: LayoutInflater) =  ActivityRegisterAgreeBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        setOnClick()
        setChkBoxListener()
        setBtnCLickListener()
    }

    override fun initView() {
        agreeAllFrame = binding.flAgreeAll
        infoFrame = binding.flAgree
        marketFrame = binding.flAgreeMarketing
        agreeAll = binding.cbAgreeAll
        infoService = binding.cbAgreeInfo
        marketingService = binding.cbAgreeMarketing
        toolbar = binding.toolbar
        fragmentWrapper = binding.termFrame
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
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setOnClick() {
        agreeAllFrame.setOnClickListener {
            if (!isInfoSeen || !isMarketSeen) {
                Toast.makeText(this, "정보 이용 약관을 참조해 주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                agreeAll.isClickable = true
            }
        }

        marketFrame.setOnClickListener {
            // FragmentDialog로 보여주고 chkbox clickable        dialog -> famelayout에 inflate로 변경
//            FragmentMarketTerm().show(supportFragmentManager, null)
            supportFragmentManager.beginTransaction().replace(R.id.term_frame, FragmentMarketServiceTerm()).commit()
            marketingService.isClickable = true
            isInfoSeen = true
            fragmentWrapper.isClickable = true
        }

        infoFrame.setOnClickListener {
//            FragmentInfoTerm().show(supportFragmentManager, null)
            supportFragmentManager.beginTransaction().replace(R.id.term_frame, FragmentInfoServiceTerm()).commit()
            infoService.isClickable = true
            isMarketSeen = true
            fragmentWrapper.isClickable = true
        }
    }

    private fun setChkBoxListener() {
        agreeAll.setOnClickListener {
            if (agreeAll.isChecked) {
                marketingService.isChecked = true
                infoService.isChecked = true

                infoClicked = true
                marketClicked = true

                binding.btnStart.isEnabled = true
            }
            else {
                marketingService.isChecked = false
                infoService.isChecked = false

                infoClicked = false
                marketClicked = false

                binding.btnStart.isEnabled = false
            }

        }

        marketingService.setOnClickListener {
            marketingService.isChecked = !marketClicked
            marketClicked = !marketClicked
            btnClickable(marketClicked, infoClicked)
        }

        infoService.setOnClickListener {
            infoService.isChecked = !infoClicked
            infoClicked = !infoClicked
            btnClickable(marketClicked, infoClicked)
        }
    }

    private fun btnClickable(flag : Boolean, flag2 : Boolean) {
        binding.btnStart.isEnabled = flag && flag2
        agreeAll.isChecked = flag && flag2
    }

    private fun setBtnCLickListener() {
        binding.btnStart.setOnClickListener {
            Intent(this, MemberRegisterActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onAgreeClick(stage : Int) {
        when(stage) {
            2 -> {
                marketingService.isChecked = true
                marketClicked = true
                btnClickable(true, infoClicked)
            }
            1 -> {
                infoService.isChecked = true
                infoClicked = true
                btnClickable(marketClicked, true)
            }
        }
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            if (fragment is OnBackPressedListener) {
                (fragment as OnBackPressedListener).onBackPressed()
                return
            }
        }

        super.onBackPressed()
    }

    override fun onResume() {
        Log.d("registerTest", "onresume called")
        super.onResume()
        supportFragmentManager.findFragmentById(R.id.term_frame)
            ?.let { supportFragmentManager.beginTransaction().remove(it).commit() }

    }
}