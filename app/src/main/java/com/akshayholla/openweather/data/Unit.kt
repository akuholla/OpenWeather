package com.akshayholla.openweather.data

enum class Unit(val param: String, val symbol: String) {
    METRIC("metric","°C"),
    IMPERIAL("imperial","°F")
}

fun String.transformToAppUnit() : com.akshayholla.openweather.data.Unit {
    return when(this) {
        "metric" -> {
            Unit.METRIC
        }
        "imperial" -> {
            Unit.IMPERIAL
        }
        else -> {
            //Unknown, default to metric
            Unit.METRIC
        }
    }
}