package ru.prsolution.winstrike.presentation.model.arena

import ru.prsolution.winstrike.domain.models.arena.Schedule


data class ScheduleItem(
    val createAt: String,
    val endTime: String,
    val name: String,
    val publicId: String,
    val roomPid: String,
    val startTime: String,
    val weekDay: String
)

fun ScheduleItem.mapToDomain(): Schedule = Schedule(
    createAt = this.createAt,
    endTime = this.endTime,
    name = this.name,
    publicId = this.publicId,
    roomPid = this.roomPid,
    startTime = this.startTime,
    weekDay = this.weekDay
)


