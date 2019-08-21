package ru.prsolution.winstrike.datasource.remote

import ru.prsolution.winstrike.data.datasource.ArenaRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.arena.mapToDomain
import ru.prsolution.winstrike.datasource.model.fcm.FCMEntity
import ru.prsolution.winstrike.datasource.model.orders.mapToDomain
import ru.prsolution.winstrike.datasource.model.payment.PaymentEntity
import ru.prsolution.winstrike.datasource.model.payment.mapToDomain
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.arena.Schedule
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.orders.OrderModel
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse
import ru.prsolution.winstrike.networking.ArenaApi

class ArenaRemoteDataSourceImpl constructor(
    private val api: ArenaApi
) : ArenaRemoteDataSource, BaseRepository() {

    override suspend fun getArenas(): Resource<List<Arena>>? {
        val arenaResponse = safeApiCall(
            call = { api.getArenaAsync().await() },
            errorMessage = "Error Fetching Cities List"
        )

        return arenaResponse?.mapToDomain()
    }

    override suspend fun getSchema(arenaPid: String?, time: Map<String, String>): Resource<ArenaSchema>? {
        val response = safeApiCall(
            call = { api.getSchemaAsync(arenaPid, time).await() },
            errorMessage = "Error Fetching Schema"
        )

        return response?.mapToDomain()
    }

    override suspend fun getHallSchema(arenaPid: String?, time: Map<String, String>): Resource<ArenaSchema>? {
        val response = safeApiCall(
            call = { api.getSchemaHallAsync(arenaPid, time).await() },
            errorMessage = "Error Fetching Schema"
        )

        return response?.mapToDomain()
    }

    override suspend fun getPayment(payment: PaymentEntity): Resource<PaymentResponse>? {
        val response = safeApiCall(
            call = { api.getPaymentAsync(payment).await() },
            errorMessage = "Error pay payment response"
        )

        return response?.mapToDomain()
    }

    override suspend fun getSchedule(): Resource<List<Schedule>>? {
        val response = safeApiCall(
            call = { api.getSchedulesAsync().await() },
            errorMessage = "Error pay payment response"
        )

        return response?.mapToDomain()
    }


    override suspend fun getOrders(): Resource<List<OrderModel>>? {
        val response = safeApiCall(
            call = { api.getOrdersAsync().await() },
            errorMessage = "Error pay payment response"
        )

        return response?.mapToDomain()
    }


    override suspend fun sendFCMCode(fcmEntity: FCMEntity): Resource<MessageResponse>? {
        val response = safeApiCall(
            call = { api.sendTockenAsync(fcmEntity = fcmEntity).await() },
            errorMessage = "Error pay payment response"
        )
        return response
    }

}
