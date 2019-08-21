package ru.prsolution.winstrike.presentation.model.orders

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.orders.OrderModel
import ru.prsolution.winstrike.domain.models.orders.mapToPresentation

data class Order(
    val accessCode: String,
    val cost: Int,
    val createAt: String,
    val endAt: String,
    val place: Place,
    val placePid: String,
    val publicId: String,
    val roomName: String,
    val startAt: String,
    val userPid: String
)


fun Order.mapToDomain(): OrderModel = OrderModel(
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

fun List<Order>.mapToDomain(): List<OrderModel> = map {
    it.mapToDomain()
}

fun Resource<List<Order>>.mapToPresentation(): Resource<List<OrderModel>> = Resource(
    state = state,
    data = data?.mapToDomain(),
    message = message
)
