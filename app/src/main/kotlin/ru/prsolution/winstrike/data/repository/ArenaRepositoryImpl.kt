package ru.prsolution.winstrike.data.repository

import ru.prsolution.winstrike.data.datasource.ArenaCacheDataSource
import ru.prsolution.winstrike.data.datasource.ArenaRemoteDataSource
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.fcm.FCMEntity
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.arena.Schedule
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.common.mapToDataSource
import ru.prsolution.winstrike.domain.models.orders.OrderModel
import ru.prsolution.winstrike.domain.models.payment.PaymentModel
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse
import ru.prsolution.winstrike.domain.models.payment.mapToDataSource
import ru.prsolution.winstrike.domain.repository.ArenaRepository

class ArenaRepositoryImpl constructor(
    private val cacheDataSource: ArenaCacheDataSource,
    private val remoteDataSource: ArenaRemoteDataSource
) : ArenaRepository {

    override suspend fun get(refresh: Boolean): Resource<List<Arena>>? =
        remoteDataSource.getArenas()

    override suspend fun get(arenaPid: String?, time: Map<String, String>, refresh: Boolean): Resource<ArenaSchema>? =
        remoteDataSource.getSchema(arenaPid, time)

    override suspend fun getHall(arenaPid: String?, time: Map<String, String>, refresh: Boolean): Resource<ArenaSchema>? =
        remoteDataSource.getHallSchema(arenaPid, time)

    override suspend fun pay(paymentModel: PaymentModel): Resource<PaymentResponse>? =
        remoteDataSource.getPayment(paymentModel.mapToDataSource())


    override suspend fun getSchedule(): Resource<List<Schedule>>? =
        remoteDataSource.getSchedule()


    override suspend fun getOrders(): Resource<List<OrderModel>>? =
        remoteDataSource.getOrders()

    override suspend fun sendFCMCode(fcmModel: FCMModel): Resource<MessageResponse>? =
        remoteDataSource.sendFCMCode(fcmModel.mapToDataSource())
}

