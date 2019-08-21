package ru.prsolution.winstrike.domain.models.orders

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.orders.OrderEntity
import ru.prsolution.winstrike.presentation.model.orders.Order
import ru.prsolution.winstrike.presentation.model.orders.Place

data class OrderModel(
    val accessCode: String = "",
    val cost: Int = 0,
    val createAt: String = "",
    val endAt: String = "",
    val place: Place,
    val placePid: String = "",
    val publicId: String = "",
    val roomName: String = "",
    val startAt: String = "",
    val userPid: String = ""
)

// Map to presentation
fun OrderModel.mapToPresentation(): Order = Order(
    accessCode = this.accessCode,
    cost = this.cost,
    createAt = this.createAt,
    endAt = this.endAt,
    place = this.place,
    placePid = this.placePid,
    publicId = this.publicId,
    roomName = this.roomName,
    startAt = this.startAt,
    userPid = this.userPid
)

fun List<OrderModel>.mapToPresentation(): List<Order> = map {
    it.mapToPresentation()
}

fun Resource<List<OrderModel>>.mapToPresentation(): Resource<List<Order>> = Resource(
    state = state,
    data = data?.mapToPresentation(),
    message = message
)

// Map to datasource
fun OrderModel.mapToDataSource(): OrderEntity = OrderEntity(
    accessCode = this.accessCode,
    cost = this.cost,
    createAt = this.createAt,
    endAt = this.endAt,
    place = this.place,
    placePid = this.placePid,
    publicId = this.publicId,
    roomName = this.roomName,
    startAt = this.startAt,
    userPid = this.userPid
)

fun List<OrderModel>.mapToDataSource(): List<OrderEntity> = map {
    it.mapToDataSource()
}

fun Resource<List<OrderModel>>.mapToDataSource(): Resource<List<OrderEntity>> = Resource(
    state = state,
    data = data?.mapToDataSource(),
    message = message
)

