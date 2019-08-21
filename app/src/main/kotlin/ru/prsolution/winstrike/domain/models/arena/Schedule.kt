package ru.prsolution.winstrike.domain.models.arena

import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.presentation.model.arena.ScheduleItem


data class Schedule(
    val createAt: String,
    val endTime: String,
    val name: String,
    val publicId: String,
    val roomPid: String,
    val startTime: String,
    val weekDay: String
)

fun Schedule.mapToPresentation(): ScheduleItem = ScheduleItem(
    createAt = this.createAt,
    endTime = this.endTime,
    name = this.name,
    publicId = this.publicId,
    roomPid = this.roomPid,
    startTime = this.startTime,
    weekDay = this.weekDay
)

fun List<Schedule>.mapToPresentation(): List<ScheduleItem> =
    map {it.mapToPresentation()}


fun Resource<List<Schedule>>.mapToPresentation(): Resource<List<ScheduleItem>> = Resource(
    state = state,
    data = data?.mapToPresentation(),
    message = message
)

