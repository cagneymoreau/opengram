package com.cagneymoreau.opengram.apiinterfaceitems

import kotlinx.coroutines.flow.StateFlow



interface RepositoryInterface {


    val authState: StateFlow<Int>

    val easyChatList: StateFlow<List<ChatInterface>>
    val viewableMessages: StateFlow<List<MessageInterface>>


    //login
    // TODO: this is telegram specific and needs to be changed


    fun submitPhoneNumber(phoneNumber: String)

    fun submitVerificationCode(code: String)


    //chatlist
    /** Get list of conversation, chats channels etc **/
    fun getChatsList()

    fun getChat(id: Long) : ChatInterface


    //chat -----
    /** Get messages from a specific chat **/
    fun requestMessages(chatID: Long, amt: Int)

    /** If user cannot chat what actions should fill the bottom bar of that chat **/
    fun bottomBarActions(chat: ChatInterface): Pair<List<String>, List<()-> Unit>>


    //messages -----
    fun sendTextMessage(id: Long, mess: String)



    //contacts
    fun getContacts():List<UserInterface>

    fun getUser(id: Long): UserInterface

    var mySelf: UserInterface

    //about section
    fun getAbout(): String

    /**Unlock hidden features **/
    fun aboutClickCounter()


    //mediaViewing
    fun setMediaToView(key: Int, value: ContentInterface)

    fun getMediaToView(key: Int) : ContentInterface

}