package ru.prsolution.winstrike.datasource.model.arena

import com.squareup.moshi.Json

data class SchedulersEntity(
    @field:Json(name = "schedules")
    val schedules: List<ScheduleEntity>
)