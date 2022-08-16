package com.cagneymoreau.workstation.ui.timecard

data class TimeCardEntry(
    val start: Long = 0,
    val finish: Long = 0,
    val uniqueId: Int = 0,
    val title: String = "",
    val description: String = ""
)
