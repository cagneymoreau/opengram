package com.cagneymoreau.telegramintegration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.cagneymoreau.opengram.apiinterfaceitems.Content
import com.cagneymoreau.opengram.apiinterfaceitems.MessageInterface
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import com.cagneymoreau.opengram.ui.support.establishPath
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*

class TelegramMessageEntity(val repo: TelegramRepository,val messageDto: TdApi.Message) : MessageInterface {

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
        get() = messageDto.isOutgoing

    //Content
    override var url: String = ""
    override var description: String = ""
    override lateinit var content: Content
    override var textContent: String = ""
    override val mediaPath: MutableState<String> = mutableStateOf("")


    override val sender: UserInterface? = getUserOrChannel()

    override val id: Long
        get() = messageDto.id

    init {
        
        
        buildContent()
    }

    
    private fun getUserOrChannel() : UserInterface?
    {
        if  (messageDto.senderId.constructor == MessageSenderUser.CONSTRUCTOR){
            return repo.getUser((messageDto.senderId as MessageSenderUser).userId)
        }

        // TODO: how do we handle channel??? 
        return null
        
    }
    
    
    
    //region -----   buildmessagecontent

    private fun buildContent()
    {
        when (messageDto.content.getConstructor()) {
            TdApi.MessageText.CONSTRUCTOR -> buildTextMessage()
            TdApi.MessagePhoto.CONSTRUCTOR -> buildPictureMessage()

            TdApi.MessageAudio.CONSTRUCTOR -> buildAudioMessage()
            TdApi.MessageVoiceNote.CONSTRUCTOR -> buildVoiceNoteMessage()
            TdApi.MessageVideo.CONSTRUCTOR -> buildVideoMessage()
            TdApi.MessageVideoNote.CONSTRUCTOR -> buildVideoNote()
            TdApi.MessageDocument.CONSTRUCTOR -> buildDocumentMessage()
            TdApi.MessageSticker.CONSTRUCTOR -> buildStickerMessage()
            TdApi.MessageAnimation.CONSTRUCTOR -> buildAnimatedMessage()


            else -> cantDisplay()
        }


    }


    private fun cantDisplay() {
        content = Content.Unknown
        textContent = "Cant display yet"
    }

    private fun buildTextMessage() {
        val mtext = messageDto.content as MessageText
        if (mtext.webPage != null) {
          buildWebPreviewMessage(mtext)
            return
        }
        content = Content.Text
        textContent = mtext.text.text

    }


    private fun buildWebPreviewMessage(mtext: MessageText) {

        content = Content.WebContent
        url = mtext.webPage!!.url
        textContent = mtext.text.text
        description = mtext.webPage!!.description.text
        val photo = mtext.webPage!!.photo
        if (photo != null){
           val file = photo.sizes[0].photo
            establishPath(file, repo, {v -> setPhotoPath(v)})
        }
    }



    private fun buildPictureMessage() {
        content = Content.Photo
        val mPhoto = messageDto.content as MessagePhoto
        val t = mPhoto.caption.text
        if (t != null && !t.isEmpty()) {
            textContent = t
        }

        val file = mPhoto.photo.sizes[0].photo

        establishPath(file, repo, {v -> setPhotoPath(v)})

    }

    fun setPhotoPath(v: String)
    {
        mediaPath.value = v

    }


    private fun buildAudioMessage() {
        content = Content.Audio
        val mAudio = messageDto.content as MessageAudio
        val t = mAudio.caption.text
        if (t != null && !t.isEmpty()) {
            textContent = t
        }

        val file = mAudio.audio.audio
        establishPath(file, repo, {v -> setAudioPath(v)})
    }

    private fun setAudioPath(v: String)
    {
        mediaPath.value = v
    }

    private fun buildVoiceNoteMessage() {
        content = Content.AudioNote
        val mAudio = messageDto.content as MessageVoiceNote
        val t = mAudio.caption.text
        if (t != null && !t.isEmpty()) {
            textContent = t
        }

        val file = mAudio.voiceNote.voice
        establishPath(file, repo, {v -> setAudioNotePath(v)})
    }

    private fun setAudioNotePath(v: String)
    {
        mediaPath.value = v
    }

    private fun buildVideoMessage() {
        content = Content.Video
        val mVideo = messageDto.content as MessageVideo
        val t = mVideo.caption.text
        if (t != null && !t.isEmpty()) {
            textContent = t
        }

        val file = mVideo.video.video
        establishPath(file, repo, {v -> setVideoPath(v)})
    }

    fun setVideoPath(v: String)
    {
        mediaPath.value = v
    }

    private fun buildVideoNote() {
        content = Content.VideoNote
        val mVideo = messageDto.content as MessageVideoNote
        val file = mVideo.videoNote.video
        establishPath(file, repo, {v -> setVideoNotePath(v)})
    }

    fun setVideoNotePath(v: String)
    {
        mediaPath.value = v
    }

    //dont download until user decides
    private fun buildDocumentMessage() {
        // TODO:
    }

    private fun buildStickerMessage() {
        content = Content.Sticker
        val mSticker = messageDto.content as MessageSticker

        if(mSticker.sticker.isAnimated){

        }
        // TODO:
    }

    private fun buildAnimatedMessage() {
        content = Content.Animation
        val mAnimation = messageDto.content as MessageAnimation
        val t = mAnimation.caption.text
        if (t != null && !t.isEmpty()) {
            textContent = t
        }

        val file = mAnimation.animation.animation
        establishPath(file, repo, {v -> setVideoPath(v)})
    }

    private fun setAnimationPath(v: String)
    {
        mediaPath.value = v

    }


    
    //endregion

}