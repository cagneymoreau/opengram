package com.cagneymoreau.mockapiintegration

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import com.cagneymoreau.opengram.ui.support.getInitials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

class MockUserEntity(val i: Int) : UserInterface {


    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    override val name: String
        get() = "user"
    override val nameTwo: String
        get() = "$i"
    override val nameAlternate: String
        get() = "handle #$i"
    override val description: String
        get() = "my bio $i"
    override val activeStatus: String
        get() = randStatus()
    override val image = MutableStateFlow(getInitials("$name $nameTwo", Color.Blue.toArgb()).asImageBitmap())

    override val id: Long = 213456 * i.toLong()


    private fun randStatus():String
    {
        val i = Random(234,).nextInt(3)

        return when (i){
            0 -> "recent"
            1-> "today"
            2-> "within week"
            else -> "long ago"
        }

    }




}