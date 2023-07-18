package com.copy.kiascreen.comparison.vo

data class CompItem(
    val perform : PerformanceItem,
    val econ :  EconItem,
    val safetyItem: SafetyItem
)

data class PerformanceItem(
    val engine : String,
    val fuel : String,
    val gear : String,
    val maxOutput : String,
    val maxTorque : String,
    val cc : String,
    val dm : String,
    val fuelTank : String,
    val batteryType : String,
    val maxBatteryCapacity : String,
    val driveDistance : String,
    val rapidCharge : String,
    val normalCharge : String,
    val overallLength : String,
    val width : String,
    val height : String,
    val wheelB : String,
    val frontT : String,
    val backT : String,
    val tire : String,
    val weight : String
)

data class EconItem(
    val cfe : String,
    val cityFe : String,
    val hfe : String,
    val feGrade : String,
    val insuranceGrade : String,
    val co2 : String,
    val maintance : String,
)

data class SafetyItem(
    val totalGrade : String,
    val year : String,
    val score : String,
    val crashSafety : String,
    val pedSafety : String,
    val preventSafety : String
)