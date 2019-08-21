package ru.prsolution.winstrike.datasource.model.orders

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.orders.PlaceModel
import ru.prsolution.winstrike.presentation.model.orders.Computer

data class PlaceEntity(
    @field:Json(name = "computer")
    val computer: Computer,
    @field:Json(name = "name")
    val name: String
)

fun PlaceEntity.mapToDomain(): PlaceModel = PlaceModel(
    computer = this.computer,
    name = this.name
)