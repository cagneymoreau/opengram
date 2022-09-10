package com.cagneymoreau.telegramintegration

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import com.cagneymoreau.opengram.ui.support.establishPath
import com.cagneymoreau.opengram.ui.support.getInitials
import com.cagneymoreau.opengram.ui.support.mediaFromPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.drinkless.td.libcore.telegram.TdApi

class TelegramUserEntity(val repo: TelegramRepository,val userDto: TdApi.User) : UserInterface {

    var init = false

    private var imgPath: String = ""

    val _image = MutableStateFlow(getInitials("$name $nameTwo", Color.Blue.toArgb()).asImageBitmap())
    override val image: StateFlow<ImageBitmap> = getImage()


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
        get() = userDto.firstName
    override val nameTwo: String
        get() = userDto.lastName
    override val nameAlternate: String
        get() = userDto.username
    override val description: String
        get() = ""
    override val activeStatus: String
        get() = userDto.status.toString()
    override val id: Long
        get() = userDto.id



    fun getImage(): MutableStateFlow<ImageBitmap>
    {
        if(!init)
        {
            init = true

            val photo = userDto.profilePhoto
            if (imgPath.isEmpty() && photo != null)
                establishPath(photo.small, repo) { p -> updatePath(p) }
        }


        return _image
    }



    fun updatePath(path: String)
    {
        imgPath = path
        _image.value = mediaFromPath(imgPath).asImageBitmap()

    }



}