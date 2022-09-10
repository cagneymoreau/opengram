package com.cagneymoreau.opengram.apiinterfaceitems

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

interface ChatInterface {


    val title: String
    val description: String
    val messageStatus: String
    val unreadCount: Int
    var time: Int
    val image: StateFlow<ImageBitmap>
    val id: Long




    fun load()

    fun readableTime(): String
    {
        val t = time

        val timeCalc: Long = 1000L * t
        val last = Instant.ofEpochMilli(timeCalc).atZone(ZoneId.systemDefault()).toLocalDateTime()

        var d = ""

        val today = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault())
            .toLocalDate()
        val midnight = LocalTime.MIDNIGHT
        val daybench = LocalDateTime.of(today, midnight)

        val weekBench = LocalDateTime.of(today,midnight).minusDays(5)

        if(last.isBefore(weekBench)){
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
                .withZone(ZoneId.systemDefault())
            d = formatter.format(last)
        }
        else if (last.isBefore(daybench)) {
            d = last.getDayOfWeek().name
            d = d.substring(0, 3)
        } else {
            var hour: Int = last.getHour()
            if (hour > 12) hour = hour - 12
            d = hour.toString() + ":" + last.getMinute()
        }

        return d

    }


}