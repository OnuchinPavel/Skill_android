package ru.prsolution.winstrike.datasource.model.orders

import com.squareup.moshi.Json
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.orders.OrderModel

data class OrdersListEntity(
    @field:Json(name = "orders")
    val orders: List<OrderEntity>
)

fun OrderEntity.mapToDomain(): OrderModel = OrderModel(
    accessCode = this.accessCode?:"",
    cost = this.cost?:0,
    createAt = this.createAt?:"",
    endAt = this.endAt?:"",
    place = this.place,
    placePid = this.placePid?:"",
    publicId = this.publicId?:"",
    roomName = this.roomName?:"",
    startAt = this.startAt?:"",
    userPid = this.userPid?:""

)


fun List<OrderEntity>.mapToDomain(): List<OrderModel> = map {
    it.mapToDomain()
}


fun Resource<OrdersListEntity>.mapToDomain(): Resource<List<OrderModel>> = Resource<List<OrderModel>>(
    state = state,
    data = data?.orders?.mapToDomain(),
    message = message
)

