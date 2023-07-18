package com.copy.kiascreen.comparison.vo

import android.util.Log

object CompListItems {
    private val performItem = PerformanceItem(
        "구동모터(150kW)",
        "전기",
        "단속 고정 기어",
        "150 / 204",
        "255",
        "0",
        "FWD(전륜구동)",
        "0",
        "리튬 이온",
        "64.8",
        "401",
        "45분(80%)",
        "-",
        "4,420",
        "1,825",
        "1,570",
        "2,720",
        "1,571",
        "1,582",
        "215/55R17",
        "1,705"
    )

    private val econItem = EconItem(
        "5.3",
        "5.9",
        "4.8",
        "전기차",
        "17",
        "0",
        "6,577,033"
    )

    private val safetyItem = SafetyItem(
        "1등급",
        "2022",
        "90.00",
        "57.359",
        "17.421",
        "15.200"
    )

    private val performItem2 = PerformanceItem(
        "구동모터(120kW)",
        "디젤",
        "수동 가변 기어",
        "170 / 224",
        "285",
        "0",
        "FWD(전륜구동)",
        "0",
        "-",
        "0",
        "550",
        "-",
        "-",
        "3,420",
        "2,825",
        "2,570",
        "1,720",
        "2,571",
        "2,582",
        "235/55R17",
        "2,705"
    )

    private val econItem2 = EconItem(
        "2.3",
        "2.9",
        "2.8",
        "디젤",
        "27",
        "15",
        "1,577,033"
    )

    private val safetyItem2 = SafetyItem(
        "2등급",
        "2021",
        "95.00",
        "51.359",
        "27.421",
        "35.200"
    )

    private var compItem = CompItem(performItem, econItem, safetyItem)

    private var compItem2 = CompItem(performItem2, econItem2, safetyItem2)

    var compItemList = mutableListOf(compItem)

    fun addCompItem() = compItemList.add(compItem2)

    fun getComeItem() = compItem

    private var updatedItem = CompItem(performItem2, econItem2, safetyItem2)

    fun getUpdatedItem() = updatedItem

    fun resetCompItemList() {
        compItemList = mutableListOf(compItem)
        Log.d("compItemTest", "after reset size = ${compItemList.size}")
    }

}