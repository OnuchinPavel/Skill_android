package ru.prsolution.winstrike.datasource.model.orders

import com.squareup.moshi.Json
import ru.prsolution.winstrike.presentation.model.orders.Place

data class OrderEntity(
    @field:Json(name = "access_code")
    val accessCode: String = " ",
    @field:Json(name = "cost")
    val cost: Int = 0,
    @field:Json(name = "create_at")
    val createAt: String,
    @field:Json(name = "end_at")
    val endAt: String = " ",
    @field:Json(name = "place")
    val place: Place,
    @field:Json(name = "place_pid")
    val placePid: String = " ",
    @field:Json(name = "public_id")
    val publicId: String = " ",
    @field:Json(name = "room_name")
    val roomName: String = " ",
    @field:Json(name = "start_at")
    val startAt: String = " ",
    @field:Json(name = "user_pid")
    val userPid: String = " "
)