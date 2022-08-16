package com.cagneymoreau.workstation.repository

import com.cagneymoreau.workstation.ui.chatlist.ChatDescriptionEntity
import kotlinx.coroutines.flow.StateFlow
import org.drinkless.td.libcore.telegram.TdApi


// TODO: build this out and this will allow creating a fake repo for testing

interface RepositoryInterface {


    val authState: StateFlow<Int>

    val easyChatList: StateFlow<List<ChatDescriptionEntity>>
    //val easyChatList: StateFlow<MutableList<ChatDescriptionEntity>>
    //val easyChatList: List<ChatDescriptionEntity>
    //val easyChatList: MutableList<ChatDescriptionEntity>

    val viewableMessages: StateFlow<MutableList<TdApi.Message>>









    fun getChatsList()





    //region authorization

    fun submitPhoneNumber(phoneNumber: String)
    {

    }

    fun submitVerificationCode(code: String)
    {

    }




}