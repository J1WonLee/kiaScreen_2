package com.copy.kiascreen.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat

class StringTypeUtil {
    private val decimalFormat = DecimalFormat("#,###")
    private val newDateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun getMoneyFormat(money : String) : String = decimalFormat.format(Integer.parseInt(money))

    fun getDateFormat(dtFormat : SimpleDateFormat, date : String) : String {
//        val formatDate = dtFormat.parse(date)
//
//        return newDateFormat.format(formatDate)

        with(dtFormat.parse(date)) {
            return newDateFormat.format(this)
        }
    }
}