package com.cagneymoreau.mockapiintegration

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.cagneymoreau.opengram.apiinterfaceitems.ChatInterface
import com.cagneymoreau.opengram.ui.support.getInitials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MockChatEntity(val position: Int)  : ChatInterface {

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    override val image: StateFlow<ImageBitmap> = MutableStateFlow(getInitials(title, Color.Blue.toArgb()).asImageBitmap())

    override val title: String
        get() = "title $position"
    override val description: String
        get() = "this is a description"
    override val messageStatus: String
        get() = "messageStatus"
    override val unreadCount: Int
        get() = 0
    override var time: Int  =  (System.currentTimeMillis() /1000).toInt() - (position * 80000)
    override val id: Long
        get() = 123456




    override fun load() {

    }


}