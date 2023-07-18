package com.copy.kiascreen.roomVo

import com.copy.kiascreen.comparison.vo.Brand
import com.copy.kiascreen.comparison.vo.Car
import com.copy.kiascreen.comparison.vo.Engine
import com.copy.kiascreen.comparison.vo.Trim

object BrandItems {
    var kiaTrim = listOf<Trim> (
        Trim("kia trim1", 1500),
        Trim("kia trim2", 2500),
        Trim("kia trim3", 3500)
    )

    var chevTrim = listOf<Trim> (
        Trim("chevTrim", 3500),
        Trim("chevTrim2", 4500),
        Trim("chevTrim3", 5500)
    )

    var hyunTrim = listOf<Trim> (
        Trim("hyunTrim", 5500),
        Trim("hyunTrim2", 6500),
        Trim("hyunTrim3", 7500)
    )

    var kiaEngine = listOf<Engine>(
        Engine("kia Engine", 1500, kiaTrim),
        Engine("kia Engine2", 2500, kiaTrim),
        Engine("kia Engine3", 3500, kiaTrim)
    )

    var chevEngine = listOf<Engine>(
        Engine("chevEngine", 1500, chevTrim),
        Engine("chevEngine2", 2500, chevTrim),
        Engine("chevEngine3", 3500, chevTrim)
    )

    var hyunEngine = listOf<Engine>(
        Engine("hyunEngine", 1500, hyunTrim),
        Engine("hyunEngine2", 2500, hyunTrim),
        Engine("hyunEngine3", 3500, hyunTrim)
    )

    var kiaCars = listOf<Car>(
        Car("kiaCars", 1500, kiaEngine),
        Car("kiaCars2", 2500, kiaEngine),
        Car("kiaCars3", 3500, kiaEngine)
    )

    var chevCars = listOf<Car>(
        Car("chevCars", 1500, chevEngine),
        Car("chevCars2", 2500, chevEngine),
        Car("chevCars3", 3500, chevEngine)
    )

    var hyunCars = listOf<Car>(
        Car("hyunCars", 1500, hyunEngine),
        Car("hyunCars2", 2500, hyunEngine),
        Car("hyunCars3", 3500, hyunEngine)
    )

    private var brandItems = mutableListOf<Brand>(
        Brand("기아", kiaCars)
    )

    private var resetItem = mutableListOf<Brand> (
        Brand("기아", kiaCars)
    )

    var brandItems2 = mutableListOf<Brand>(
        Brand("기아", kiaCars),
        Brand("쉐보레", chevCars),
        Brand("현대", hyunCars)
    )

    var spinnerList = mutableListOf<MutableList<Brand>>(brandItems)

    var resetList = mutableListOf<MutableList<Brand>>(resetItem)

    var carImgLink = mutableListOf<String>(
        "https://www.kia.com/content/dam/kwp/kr/ko/compare/scarimg_1120.png",
        "https://www.kia.com/content/dam/kwp/kr/ko/compare/scarimg_1189.png",
        "https://www.kia.com/content/dam/kwp/kr/ko/compare/scarimg_52.png"
    )

    var baseImgLink = mutableListOf<String>(
        "https://www.kia.com/content/dam/kwp/kr/ko/compare/scarimg_1189.png",
        "https://www.kia.com/content/dam/kwp/kr/ko/compare/scarimg_1189.png",
        "https://www.kia.com/content/dam/kwp/kr/ko/compare/scarimg_52.png"
    )

}