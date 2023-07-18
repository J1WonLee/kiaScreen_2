package com.copy.kiascreen.coach

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.copy.kiasample.coach.CoachPagerAdapter
import com.copy.kiascreen.databinding.ActivityCoachBinding
import com.copy.kiascreen.roomVo.User
import com.copy.kiascreen.util.activity.BaseActivityWithoutVM
import com.copy.kiascreen.util.activity.TransitionMode

class CoachActivity : BaseActivityWithoutVM<ActivityCoachBinding>(TransitionMode.HORIZON) {
    private lateinit var vp2 : ViewPager2
//    private var pagerAdapter = CoachPagerAdapter(this)
    private var user : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setAdapter()
    }

    private fun getDataFromIntent() {
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        }
        else {
            intent.getParcelableExtra<User>("user")
        }
        Log.d("coachTest", "user id = ${user?.id}")
    }

    override fun inflateLayout(layoutInflater: LayoutInflater) = ActivityCoachBinding.inflate(layoutInflater)

    override fun initView() {
        vp2 = binding.coachPager2
    }

    private fun setAdapter() {
        var pagerAdapter = CoachPagerAdapter(this, user)
        vp2.adapter = pagerAdapter
    }


}