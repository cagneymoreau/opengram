package com.cagneymoreau.telegramintegration

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.cagneymoreau.opengram.apiinterfaceitems.ChatInterface
import com.cagneymoreau.opengram.ui.support.establishPath
import com.cagneymoreau.opengram.ui.support.getInitials
import com.cagneymoreau.opengram.ui.support.mediaFromPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.drinkless.td.libcore.telegram.TdApi

class TelegramChatEntity(telegramRepository: TelegramRepository, chatObject: TdApi.Chat) : ChatInterface, Comparable<TelegramChatEntity> {

    val chatDTO = chatObject
    val repo = telegramRepository

    val _image = MutableStateFlow(getInitials(title, Color.Blue.toArgb()).asImageBitmap())
    override val image: StateFlow<ImageBitmap> = _image

    override val title: String
        get() = chatDTO.title
    override val description: String
        get() = TODO("Not yet implemented")
    override val messageStatus: String
        get() = getLastMessage()
    override val unreadCount: Int
        get() = getUndreadCount()
    override var time: Int = (System.currentTimeMillis()/1000).toInt()
    override val id: Long
        get() = chatDTO.id


    override fun compareTo(other: TelegramChatEntity): Int {

        if (other.chatDTO.positions[0].order < chatDTO.positions[0].order) return -1
        if (other.chatDTO.positions[0].order > chatDTO.positions[0].order) return  1
        else return 0

    }


   // private var lastMssage: TdApi.Message = chatDTO.lastMessage
    private var imgPath: String = ""


    override fun load()
    {
        val photo = chatDTO.photo
        if (imgPath.isEmpty() && photo != null)
        establishPath(photo.small, repo) { p -> updatePath(p) }

        val lasMess = chatDTO.lastMessage
        if (lasMess != null) time = lasMess.date


    }

    fun updatePath(path: String)
    {
        imgPath = path
       _image.value = mediaFromPath(imgPath).asImageBitmap()

    }

    fun getLastMessage(): String
    {
        val lasMess = chatDTO.lastMessage

        if (lasMess == null) return "unknown message"

        val content = lasMess.content

        if (content.constructor == TdApi.MessageText.CONSTRUCTOR){

            val textcontent = content as TdApi.MessageText

            return textcontent.text.text

        }

        return "media message"
    }



    fun getUndreadCount(): Int
    {
        return chatDTO.unreadCount

    }




}