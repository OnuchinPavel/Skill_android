package ru.prsolution.winstrike.domain.models.arena

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
 * Created by oleg on 01.02.2018.
 */

@Parcelize
data class SeatCarousel(
    val type: String = "",
    var title: String = "",
    val imageUrl: String?,
    val description: String?,
    val hallName: String?,
    val hallPublicId: String?
) : Parcelable

enum class Type {
    COMMON, VIP, HALL
}
