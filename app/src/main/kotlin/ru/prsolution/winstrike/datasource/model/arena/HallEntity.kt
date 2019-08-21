package ru.prsolution.winstrike.datasource.model.arena

import com.squareup.moshi.Json
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.arena.Hall

data class HallEntity (
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "image_url")
    val imageUrl: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "public_id")
    val publicId: String
)

fun HallEntity.mapToDomain(): Hall = Hall(
    description = this.description,
    imageUrl = this.imageUrl,
    name = this.name,
    publicId = this.publicId
)

fun List<HallEntity>.mapToDomain(): List<Hall> = map {
    it.mapToDomain()
}




