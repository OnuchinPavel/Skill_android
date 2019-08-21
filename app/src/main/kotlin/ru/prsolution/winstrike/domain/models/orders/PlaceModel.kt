package ru.prsolution.winstrike.domain.models.orders

import ru.prsolution.winstrike.presentation.model.orders.Computer
import ru.prsolution.winstrike.presentation.model.orders.Place

data class PlaceModel(
    val computer: Computer,
    val name: String
)

fun PlaceModel.mapToPresentation(): Place = Place(
    computer = this.computer,
    name = this.name
)



