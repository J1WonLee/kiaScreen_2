package com.copy.kiascreen.comparison.vo

data class SelectedOption(
    val brandName : String,
    var carName : String,
    var engineName : String,
    var trimName : String,
    var price : Int
)

data class Brand(
    val brandName : String,
    val cars : List<Car>
)

data class Car (
    val carName : String,
    val carPrice : Int,
    val engines : List<Engine>
)

data class Engine(
    val engineName : String,
    val enginePrice : Int,
    val trims : List<Trim>
)

data class Trim(
    val trimName : String,
    val trimPrice : Int
)

data class LastSelectedItem(
    var brand : Int = 0,
    var car : Int = 0,
    var engine : Int = 0,
    var trim : Int = 0
)
