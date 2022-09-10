package com.cagneymoreau.mockapiintegration

import androidx.compose.runtime.MutableState
import com.cagneymoreau.opengram.apiinterfaceitems.Content
import com.cagneymoreau.opengram.apiinterfaceitems.MessageInterface
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface

class MockMessageEntity(val p: Int, val v: Content, val mess: String) : MessageInterface{

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    override val isOutgoing: Boolean
        get() = ((p % 2) == 0)
    override val content: Content
        get() = v
    override val sender: UserInterface
        get() = MockUserEntity(p)

    override val id: Long
        get() = p + 1000L
    override var textContent: String = mess

    override val mediaPath: MutableState<String>
        get() = TODO("Not yet implemented")

    override var url: String = "www.url.com"

    override var description: String = "description"

}