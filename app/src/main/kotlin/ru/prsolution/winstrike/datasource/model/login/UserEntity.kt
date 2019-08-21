package ru.prsolution.winstrike.datasource.model.login

import com.squareup.moshi.Json
import ru.prsolution.winstrike.domain.models.login.UserModel

class UserEntity(

    @field:Json(name = "confirmed")
    val confirmed: Boolean?,

    @field:Json(name = "confirmed_on")
    val confirmedOn: Any?,

    @field:Json(name = "email")
    val email: String?,

    @field:Json(name = "name")
    val name: String?,

    @field:Json(name = "phone")
    val phone: String?,

    @field:Json(name = "public_id")
    val publicId: String?,

    @field:Json(name = "registered_on")
    val registeredOn: String?,

    @field:Json(name = "social_id")
    val socialId: Any?,

    @field:Json(name = "role")
    val role: String?,

    @field:Json(name = "user_id")
    val id: String?,
    val password: String?
)

fun UserEntity.mapToDomain(): UserModel = UserModel(
    id = this.id,
    name = this.name,
    phone = this.phone,
    publickId = this.publicId,
    confirmed = this.confirmed
)
