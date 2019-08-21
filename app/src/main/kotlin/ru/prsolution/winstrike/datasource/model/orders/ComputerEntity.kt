package ru.prsolution.winstrike.datasource.model.orders

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.orders.ComputerModel

data class ComputerEntity(
    @field:Json(name = "active")
    val active: Boolean,
    @field:Json(name = "create_at")
    val createAt: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "public_id")
    val publicId: String,
    @field:Json(name = "room_pid")
    val roomPid: String
)

fun ComputerEntity.mapToDomain(): ComputerModel = ComputerModel(
    active = this.active,
    createAt = this.createAt,
    name = this.name,
    publicId = this.publicId,
    roomPid = this.roomPid
)

