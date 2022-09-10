package com.cagneymoreau.mockapiintegration

import com.cagneymoreau.opengram.apiinterfaceitems.*
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class MockRepository: RepositoryInterface {



    override val authState: StateFlow<Int>
        get() = TODO("Not yet implemented")
    override var mySelf: UserInterface = MockUserEntity(1)
    override val easyChatList: StateFlow<List<ChatInterface>>
        get() = TODO("Not yet implemented")
    override val viewableMessages: StateFlow<List<MessageInterface>>
        get() = TODO("Not yet implemented")

    override fun getChatsList() {
        TODO("Not yet implemented")
    }

    override fun getChat(id: Long): ChatInterface {
        TODO("Not yet implemented")
    }

    override fun submitPhoneNumber(phoneNumber: String) {
        TODO("Not yet implemented")
    }

    override fun submitVerificationCode(code: String) {
        TODO("Not yet implemented")
    }

    override fun requestMessages(chatID: Long, amt: Int) {
        TODO("Not yet implemented")
    }

    override fun bottomBarActions(chat: ChatInterface): Pair<List<String>, List<() -> Unit>> {
        TODO("Not yet implemented")
    }

    override fun sendTextMessage(id: Long, mess: String) {
        TODO("Not yet implemented")
    }

    override fun getAbout(): String {
        TODO("Not yet implemented")
    }

    override fun aboutClickCounter() {
        TODO("Not yet implemented")
    }

    override fun getContacts(): List<UserInterface> {
        TODO("Not yet implemented")
    }

    override fun setMediaToView(key: Int, value: ContentInterface) {
        TODO("Not yet implemented")
    }

    override fun getMediaToView(key: Int): ContentInterface {
        TODO("Not yet implemented")
    }

    override fun getUser(id: Long): UserInterface {
        return MockUserEntity(Random(123).nextInt())
    }
}