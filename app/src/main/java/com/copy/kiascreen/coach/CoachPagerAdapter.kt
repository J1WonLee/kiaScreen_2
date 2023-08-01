package com.copy.kiasample.coach

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.copy.kiascreen.coach.CoachFragment01
import com.copy.kiascreen.coach.CoachFragment02
import com.copy.kiascreen.coach.CoachFragment03
import com.copy.kiascreen.roomVo.User

class CoachPagerAdapter(fragmentActivity: FragmentActivity, user : User?) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = HashMap<Int, Fragment>()

    init {
        fragments[0] = CoachFragment01()
        fragments[1] = CoachFragment02()
        if (user != null) {
            fragments[2] = CoachFragment03.newInstance(user)
        }
        else {
            fragments[2] = CoachFragment03()
        }
    }

//    fun setFragment3(mUser : User?) {
//        fragments[2] = if (mUser == null) {
//            CoachFragment03()
//        }
//        else {
//            CoachFragment03.newInstance(mUser)
//        }
//    }

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return fragments[position]!!
    }
}