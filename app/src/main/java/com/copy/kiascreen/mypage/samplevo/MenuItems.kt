package com.copy.kiascreen.mypage.samplevo

import android.view.MenuItem
import com.copy.kiascreen.R

object MenuItems {
    val menuItems = listOf<QuickMenu> (
            QuickMenu("0", "메뉴\n"),
            QuickMenu("1", "모델\n비교"),
            QuickMenu("0", "시승\n신청")
        )

    val snapItems = listOf<SnapItem> (
        SnapItem(R.drawable.snap, "오늘의 추천 차량 보러 가기", "차량을 비교해 보세요"),
        SnapItem(R.drawable.snap, "오늘의 추천 차량 보러 가기2", "차량을 비교해 보세요2"),
        SnapItem(R.drawable.snap, "오늘의 추천 차량 보러 가기3", "차량을 비교해 보세요3")
    )
}

