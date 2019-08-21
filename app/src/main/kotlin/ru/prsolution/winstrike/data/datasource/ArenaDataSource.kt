package ru.prsolution.winstrike.data.datasource

import io.reactivex.Single
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.fcm.FCMEntity
import ru.prsolution.winstrike.datasource.model.orders.OrderEntity
import ru.prsolution.winstrike.datasource.model.orders.OrdersListEntity
import ru.prsolution.winstrike.datasource.model.payment.PaymentEntity
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.arena.Schedule
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.orders.OrderModel
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

interface ArenaCacheDataSource {

    fun get(): Single<List<Arena>>

    fun set(list: List<Arena>?): Single<List<Arena>>

}

interface ArenaRemoteDataSource {

    suspend fun getArenas(): Resource<List<Arena>>?

    suspend fun getSchema(arenaPid: String?, time: Map<String, String>): Resource<ArenaSchema>?

    suspend fun getHallSchema(arenaPid: String?, time: Map<String, String>): Resource<ArenaSchema>?

    suspend fun getPayment(payment: PaymentEntity): Resource<PaymentResponse>?

    suspend fun getSchedule(): Resource<List<Schedule>>?

    suspend fun getOrders(): Resource<List<OrderModel>>?

    suspend fun sendFCMCode(fcmEntity: FCMEntity): Resource<MessageResponse>?
}
