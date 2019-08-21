package ru.prsolution.winstrike.datasource.model.arena

import com.squareup.moshi.Json
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.datasource.model.city.CityListEntity
import ru.prsolution.winstrike.datasource.model.city.mapToDomain
import ru.prsolution.winstrike.domain.models.arena.Schedule
import ru.prsolution.winstrike.domain.models.city.City
import ru.prsolution.winstrike.domain.models.payment.PaymentResponse

data class ScheduleEntity(
    @field:Json(name = "create_at")
    val createAt: String,
    @field:Json(name = "end_time")
    val endTime: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "public_id")
    val publicId: String,
    @field:Json(name = "room_pid")
    val roomPid: String,
    @field:Json(name = "start_time")
    val startTime: String,
    @field:Json(name = "week_day")
    val weekDay: String
)

fun ScheduleEntity.mapToDomain(): Schedule = Schedule(
    createAt = this.createAt,
    endTime = this.endTime,
    name = this.name,
    publicId = this.publicId,
    roomPid = this.roomPid,
    startTime = this.startTime,
    weekDay = this.weekDay
)

fun List<ScheduleEntity>.mapToDomain(): List<Schedule> = map { it.mapToDomain()}

fun Resource<SchedulersEntity>.mapToDomain(): Resource<List<Schedule>> = Resource<List<Schedule>>(
    state = state,
    data = data?.schedules?.mapToDomain(),
    message = message
)


