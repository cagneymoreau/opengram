package com.cagneymoreau.workstation.repository

import com.cagneymoreau.workstation.ui.chatlist.ChatDescriptionEntity
import org.drinkless.td.libcore.telegram.TdApi

data class ChatMapper(val api: TdApi.Chat, val entity: ChatDescriptionEntity) {




    fun updateTitle(title: String)
    {


    }

    fun updatePhoto()
    {

    }

    fun updateOrder(positionsIn: TdApi.ChatPosition )
    {
        api.positions = arrayOf(positionsIn)
        entity.order = positionsIn.order
    }


}


    fun createListViewModel(api: TdApi.Chat): ChatDescriptionEntity
    {
        return ChatDescriptionEntity("", api.title, "", "", "", 123445, 0)
    }