package ru.prsolution.winstrike.presentation.model.orders

import ru.prsolution.winstrike.domain.models.orders.PlaceModel

data class Place(
    val computer: Computer,
    val name: String = " "
)

fun Place.mapToDomain(): PlaceModel = PlaceModel(
    computer = this.computer,
    name = this.name
)
