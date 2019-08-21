package ru.prsolution.winstrike.domain.usecases

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.arena.Arena
import ru.prsolution.winstrike.domain.models.arena.ArenaSchema
import ru.prsolution.winstrike.domain.models.arena.Schedule
import ru.prsolution.winstrike.domain.models.common.FCMModel
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.domain.models.orders.OrderModel
import ru.prsolution.winstrike.domain.repository.ArenaRepository
import ru.prsolution.winstrike.presentation.model.fcm.FCMPid
import ru.prsolution.winstrike.presentation.model.fcm.mapToDomain

class ArenaUseCase constructor(private val arenaRepository: ArenaRepository) {

    suspend fun get(refresh: Boolean): Resource<List<Arena>>? =
        arenaRepository.get(refresh)


    suspend fun get(arenaPid: String?, time: Map<String, String>, refresh: Boolean): Resource<ArenaSchema>? =
        arenaRepository.get(arenaPid, time, refresh)

    suspend fun getHall(arenaPid: String?, time: Map<String, String>, refresh: Boolean): Resource<ArenaSchema>? =
        arenaRepository.getHall(arenaPid, time, refresh)

    suspend fun getSchedule(): Resource<List<Schedule>>? =
        arenaRepository.getSchedule()


    suspend fun getOrders(): Resource<List<OrderModel>>? =
        arenaRepository.getOrders()

    suspend fun sendFCMCode(fcmPid: FCMPid): Resource<MessageResponse>? =
        arenaRepository.sendFCMCode(fcmPid.mapToDomain())
}
