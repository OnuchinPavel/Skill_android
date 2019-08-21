package ru.prsolution.winstrike.presentation.utils

import android.graphics.Point
import ru.prsolution.winstrike.domain.models.arena.SeatMap
import timber.log.Timber

class MapViewUtils() {

    companion object {

        /**вычисляет расстояние от начала координат до начальной точки картинки через гипотенузу*/
        private fun getDist(coord: Point): Double {
            val d = Math.sqrt(Math.pow(coord.x.toDouble(), 2.0) + Math.pow(coord.y.toDouble(), 2.0))
            return d
        }

        fun calculateScreenSize(seatSize: Point, seats: MutableList<SeatMap>?, mXScaleFactor: Float, mYScaleFactor: Float): Point {
            val mSeats = seats
            val farthestSeat = mSeats?.maxWith(Comparator<SeatMap> { p1, p2 ->
                when {
                    getDist(Point(p1.dx.toInt(),
                                                                                                    p1.dy.toInt())) > getDist(
                            Point(p2.dx.toInt(), p2.dy.toInt())) -> 1
                    getDist(Point(p1.dx.toInt(),
                                                                                                    p1.dy.toInt())) == getDist(
                            Point(p2.dx.toInt(), p2.dy.toInt())) -> 0
                    else -> -1
                }
            })
            Timber.d("farthestSeat: %s", farthestSeat?.name)
            val point = Point()
            point.x = farthestSeat?.dx?.toInt() ?: 0
            point.y = farthestSeat?.dy?.toInt() ?: 0
            point.x = (point.x * mXScaleFactor).toInt() + seatSize.x * 2
            point.y = (point.y * mYScaleFactor / 1.5).toInt() + seatSize.y * 4

            return point
        }

        // Declare an extension function that calls a lambda called block if the value is null
        inline fun <T> T.guard(block: T.() -> Unit): T {
            if (this == null) block(); return this
        }

        fun getSeatOffsetYArena1(seat: SeatMap): Int {
            var value: Int = 0
            val y = seat.dy

            // Row 76 - 80 (left) && Row: 6 - 10 (right)
            if (y == 100.0) {
                value = 7

            // Rotated rows (66 - 41)
             // Row: 66 - 45
            } else if (y == 145.0) {
                value = 15
            // Row: 67 - 44
            } else if (y == 181.0) {
                value = 15
            // Row: 68 - 43
            } else if (y == 218.0) {
                value = 15
            // Row: 69 - 42
            } else if (y == 254.0) {
                value = 15
            // Row: 70 - 41
            } else if (y == 290.0) {
                value = 15

            // Row: 15 - 11 (right)
            } else if (y == 154.0) {
                value = 0
            // Row: 16 - 20 (right)
            } else if (y == 189.0) {
                value = 8

            // Row: 25 - 21 (right)
            } else if (y == 244.0) {
                value = 0
            // Row: 26 - 30 (right)
            } else if (y == 279.0) {
                value = 7

            // Row:  81 - 85 (left) && 35 - 31 (right)
            } else if (y == 336.0) {
                value = 8
            // Row: 86 - 90 (left) &&  Row: 36 - 40 (right)
            } else if (y == 369.0) {
                value = 15

            // HP STAGE 1 (left) (Row: 1 - 5) && HP STAGE 2 (right) (Row: 6 - 10)
            } else if (y == 455.0) {
                value = 25

            // LG ROOM Row: 15 - 11 && SENNHEISER  Row: 21 - 25
            } else if (y == 524.0) {
                value = 35
            // LG ROOM Row: 16 - 20
            } else if (y == 564.0) {
                value = 35
            }
            return value
        }

        fun getSeatOffsetXArena1(seat: SeatMap): Int {
            var value: Int = 0
/*            val id = seat.id
            if (arrayListOf("5", "6", "7", "8", "9", "15", "16", "17", "18", "19").contains(id)) {
                value = 20
            } else if (arrayListOf("26", "27", "28", "29", "30").contains(id)) {
                value = 20
            } else if (arrayListOf("37", "38", "39", "40", "41").contains(id)) {
                value = 20
            } else if (arrayListOf("54", "55", "56", "57", "58").contains(id)) {
                value = 20
            } else if (arrayListOf("65", "66", "67", "68", "69").contains(id)) {
                value = 20
            } else if (arrayListOf("75", "76", "77", "78", "79").contains(id)) {
                value = 20
            } else if (arrayListOf("85", "86", "87", "88", "89").contains(id)) {
                value = 20
            } else if (arrayListOf("95", "96", "97", "98", "99").contains(id)) {
                value = 20
            } else if (arrayListOf("105", "106", "107", "108", "109").contains(id)) {
                value = 20
            } else if (arrayListOf("110", "111", "112", "113", "114").contains(id)) {
                value = 20
            }*/
            return value
        }

        fun getLabelOffsetXArena1(label: String?): Int {
            return when (label) {
                "ЗАЛ" -> 0
                "HP STAGE 1" -> 0
                "HP STAGE 2" -> 0
                "SENNHEISER" -> 0
                "LG ROOM" -> 0
                else -> 0
            }
        }

        fun getLabelOffsetYArena1(label: String?): Int {
            return when (label) {
                "ЗАЛ" -> 0
                "HP STAGE 1" -> 8
                "HP STAGE 2" -> 8
                "SENNHEISER" -> 8
                "LG ROOM" -> 8
                else -> 0
            }
        }
    }
}
