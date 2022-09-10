package com.cagneymoreau.opengram.ui.chat

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cagneymoreau.opengram.R
import com.cagneymoreau.opengram.apiinterfaceitems.ChatInterface
import com.cagneymoreau.opengram.apiinterfaceitems.MessageInterface
import com.cagneymoreau.opengram.apiinterfaceitems.RepositoryInterface
import com.cagneymoreau.opengram.ui.structure.Pickable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repositoryInterface: RepositoryInterface
    ) : ViewModel() {


    //region ------------------ chat bubble control



    var chat: ChatInterface? = null
    val messageList: MutableState<List<MessageInterface>> = mutableStateOf(mutableListOf())

    fun getTitle():String{
         return chat!!.title
    }



    fun requestMessages(chatId:  Long){

        chat = repositoryInterface.getChat(chatId)

        repositoryInterface.requestMessages(chatId, 10)


        viewModelScope.launch {

            repositoryInterface.viewableMessages.collect{
                    i -> messageList.value = i
            }

        }

    }

    /** If we cant participate in this chat send back the actions we can perform such as join or mute **/
    fun bottomBaraActions(): Pair<List<String>, List<() -> Unit>>
    {
        return repositoryInterface.bottomBarActions(chat!!)
    }

    //endregion



    //region --------------------- typing bottom bar control


    val graphicIcon = R.drawable.ic_baseline_emoji_emotions_24

    var textInput = mutableStateOf("")


    var attachSendIcon = mutableStateOf(R.drawable.ic_baseline_attach_file_24)


    var recordIcon = mutableStateOf(R.drawable.ic_baseline_mic_24)

    var readyForAudio = mutableStateOf(false)
    var recording = mutableStateOf(false)
    var recordingTime = mutableStateOf("")


    var showAttach = mutableStateOf(false)
    var showGraphic = mutableStateOf(false)

    var pickChoice = mutableStateOf(-1)



    fun onTextChanged(v: String)
    {
        textInput.value = v

        if (v.isEmpty()){

            if (attachSendIcon.value != R.drawable.ic_baseline_attach_file_24)
                attachSendIcon.value = R.drawable.ic_baseline_attach_file_24


        }else{

            if (attachSendIcon.value != R.drawable.ic_baseline_send_24)
                attachSendIcon.value = R.drawable.ic_baseline_send_24

        }
    }

    fun attachSendButtonClick()
    {
        if (textInput.value.isEmpty()) showAttach.value = !showAttach.value
        else{
            repositoryInterface.sendTextMessage(chat!!.id, textInput.value)
            onTextChanged("")
        }

    }


    fun recordButtonClick()
    {

    }

    fun showGraphic()
    {

    }

    fun openCamera()
    {

    }

    fun picked(uri: Uri)
    {
        val f = uri.path?.let { File(it) }

        val s = f?.let { f.path }

    }




    fun getPickables(): List<Pickable>
    {

        val file = Pickable(  R.drawable.ic_baseline_insert_drive_file_24, "File", Color.Green )
        val gallery = Pickable(R.drawable.ic_baseline_image_24, "Gallery", Color.Red)
        val camera = Pickable(R.drawable.ic_baseline_camera_alt_24, "Camera", Color.Yellow)
        val location = Pickable(R.drawable.ic_baseline_location_on_24, "Location", Color.Blue)
        val poll = Pickable(R.drawable.ic_baseline_poll_24, "Poll", Color.Gray)
        val contact = Pickable(R.drawable.ic_baseline_person_24, "Contact", Color.Magenta)

        return listOf(file, gallery, camera, location, poll, contact)

    }


    fun getGraphics(): List<Pickable>
    {
        val gif = Pickable(R.drawable.ic_baseline_gif_box_24, "Gif", Color.Blue)
        val emoji = Pickable( R.drawable.ic_baseline_emoji_emotions_24, "Emoji", Color.Red)
        val sticker = Pickable( R.drawable.ic_baseline_sticker_24, "Sticker", Color.Green)


        return listOf(emoji, gif, sticker)

    }



    //endregion


}