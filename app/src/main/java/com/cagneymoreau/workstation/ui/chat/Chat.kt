package com.cagneymoreau.workstation.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.workstation.R
import com.cagneymoreau.workstation.ui.chatlist.ListItem
import com.cagneymoreau.workstation.ui.loadscreen.LogoDisplay
import org.drinkless.td.libcore.telegram.TdApi



@Composable
fun ChatScreen(
    chatId: Long,
    viewModel: ChatViewModel = hiltViewModel() )
{
    val messageList = viewModel.messageList.value

    ChatContent(messages = messageList)

}



@Composable
fun ChatContent(modifier: Modifier = Modifier, messages: List<TdApi.Message>)
{
    Surface() {
        Column(modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {

            ChatMessageList(messages = messages)

        }
    }

}


@Composable
fun ChatMessageList(modifier: Modifier = Modifier, messages: List<TdApi.Message>)
{
    LazyColumn(modifier = modifier.fillMaxSize(), reverseLayout = true) {
        items(items = messages) { message ->
           ChatBubble(message = message)
        }

    }

}

@Composable
fun ChatBubble(modifier: Modifier = Modifier, message: TdApi.Message)
{
    var color = MaterialTheme.colors.secondary
    var endPos = 80.dp
    var startPos = 10.dp
    var imageAlign = Alignment.Start

     if(message.isOutgoing){

         color = MaterialTheme.colors.primaryVariant
         endPos = 10.dp
         startPos = 80.dp
         imageAlign = Alignment.End
     }



    Row(modifier = modifier.padding(10.dp)) {

        Column(modifier = modifier.fillMaxSize(), horizontalAlignment = imageAlign) {
            Image(modifier = modifier, painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "user_image")

        }

    }


    Row(modifier = modifier.padding(top = 10.dp, bottom = 10.dp, end = endPos, start = startPos)) {
        
        Column(modifier = modifier
            .background(
                color = color,
                shape = RoundedCornerShape(4, 4, 4, 4)
            )
            .fillMaxWidth()
            .padding(5.dp)) {

            when(message.content.constructor){

                TdApi.MessageText.CONSTRUCTOR -> {
                    TextContent(content = message.content as TdApi.MessageText)
                }
                TdApi.MessagePhoto.CONSTRUCTOR -> {
                    Text(text = "not yet")
                }
                TdApi.MessageAudio.CONSTRUCTOR -> {
                    Text(text = "not yet")
                }
                TdApi.MessageVoiceNote.CONSTRUCTOR -> {
                    Text(text = "not yet")
                }
                TdApi.MessageVideo.CONSTRUCTOR -> {
                    Text(text = "not yet")
                }
                TdApi.MessageVideoNote.CONSTRUCTOR -> {
                    Text(text = "not yet")
                }
                TdApi.MessageDocument.CONSTRUCTOR -> {
                    Text(text = "not yet")
                }
                TdApi.MessageSticker.CONSTRUCTOR -> {
                    Text(text = "not yet")
                }
                TdApi.MessageAnimation.CONSTRUCTOR -> {
                    Text(text = "not yet")
                }

            }

        }
        
        
    }


}



@Composable
fun TextContent(content: TdApi.MessageText)
{
    Text(text = content.text.text)
}

@Composable
fun PictureContent()
{

}

@Composable
fun VideoContent()
{

}

@Composable
fun DocumentContent()
{

}





@Preview
@Composable
fun previewChatList()
{
    val testMessages = mutableListOf<TdApi.Message>()

    var outgoing = true;
    var message = "Message Content Filler"

    for(i in 0 until 16){

        val content = TdApi.MessageText(TdApi.FormattedText("$i - $message", null), null)
        testMessages.add(TdApi.Message(2134,null,2134,null,null,outgoing,
            false,true, true,true,false,
            true,false,false,false,
            true,true,true,true,123,
            123,null,null, 1234,1234,1234,123,
            0.0,1234,"1234",1234,"test", content ,null))

        outgoing = !outgoing
        message += message

    }





    ChatContent(messages = testMessages)


}