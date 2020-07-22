package edu.rosehulman.gearlocker.models

data class Item (
    var name: String = "",
    var estimatedCost: Float = 0.0f,
    var condition: Int = 0
)