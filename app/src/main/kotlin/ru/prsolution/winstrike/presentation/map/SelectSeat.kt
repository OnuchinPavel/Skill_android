package ru.prsolution.winstrike.presentation.map

import android.widget.ImageView
import ru.prsolution.winstrike.domain.models.arena.SeatMap

data class SelectSeat (
    val ivSeat: ImageView ,
    val seat: SeatMap
)