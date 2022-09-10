package com.cagneymoreau.opengram.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.Content
import com.cagneymoreau.opengram.apiinterfaceitems.MessageInterface
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import com.cagneymoreau.opengram.apiinterfaceitems.compareSenders
import com.cagneymoreau.mockapiintegration.MockMessageEntity
import com.cagneymoreau.opengram.ui.mediadisplay.DisplayContent
import com.cagneymoreau.opengram.ui.structure.*


// TODO: sticker set
// TODO: attach
// TODO: user profile

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    chatId: Long,
    viewModel: ChatViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    goBack: ()-> Unit,
    viewUser: (Long) -> Unit,
    viewMedia: (Int) -> Unit
)
{
    viewModel.requestMessages(chatId)
    val messageList = viewModel.messageList.value

    Scaffold( scaffoldState = scaffoldState,
        topBar = {
            TopAppBarGoBack(
                goBack = goBack,  viewModel.getTitle())
        },
        modifier = modifier,

        bottomBar = {
            BottomBar()
        }

        ) {

        it ->

        Box(modifier = modifier.padding(it)) {
            ChatContent(messages = messageList, viewUser = viewUser, viewMedia = viewMedia)

        }

    }

}



@Composable
fun ChatContent(modifier: Modifier = Modifier,
                messages: List<MessageInterface>,
                viewUser: (Long) -> Unit,
                viewMedia: (Int) -> Unit
)
{
    Surface() {
        Column(modifier = modifier,
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {

            ChatMessageList(messages = messages, viewMedia = viewMedia, viewUser = viewUser)

        }
    }

}


@Composable
fun ChatMessageList(modifier: Modifier = Modifier,
                    messages: List<MessageInterface>,
                    viewUser: (Long) -> Unit,
                    viewMedia: (Int) -> Unit
)
{
    LazyColumn(modifier = modifier, reverseLayout = true) {
        itemsIndexed(items = messages, key = {index, messages -> messages.id}) { index, message ->

            var avatar = true
            if (index > 0){
                avatar =  compareSenders(message, messages[index-1])
            }

            var nameShow = true;
            if (index < messages.size-1)
            {
                nameShow = compareSenders(message, messages[index+1])
            }

           ChatBubble(message = message, showAvatar = avatar, showUserName = nameShow, viewUser = viewUser, viewMedia = viewMedia)
        }

    }

}

@Composable
fun ChatBubble(modifier: Modifier = Modifier,
               message: MessageInterface,
               showAvatar: Boolean,
               showUserName: Boolean,
               viewUser: (Long) -> Unit,
               viewMedia: (Int) -> Unit
)
{

    val sender = message.sender

    var color = MaterialTheme.colors.secondary
    var endPos = 80.dp
    var startPos = 10.dp
    var imageAlign = Alignment.Start
    var contentArrange = Arrangement.Start

     if(message.isOutgoing){

         color = MaterialTheme.colors.primaryVariant
         endPos = 10.dp
         startPos = 80.dp
         imageAlign = Alignment.End
         contentArrange = Arrangement.End
     }


        Row(
            modifier = modifier
                .padding(
                    top = 2.dp,
                    bottom = 2.dp,
                    end = endPos,
                    start = startPos
                )
                .height(intrinsicSize = IntrinsicSize.Min)
                .fillMaxWidth(),
           horizontalArrangement = contentArrange,
            verticalAlignment = Alignment.Bottom
        ) {

            if (!message.isOutgoing)
                Avatar(
                    sender = sender,
                    showAvatar = showAvatar,
                    viewUser = viewUser
                )

            Column(modifier = modifier.weight(1f),
                    horizontalAlignment = imageAlign) {
            Column(
                modifier = modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(
                            4,
                            4, 4,
                            4
                        )
                    )

                    .padding(5.dp)
            ) {

                if (!message.isOutgoing &&
                    showUserName &&
                    sender != null) Text(text = sender.name, color = Color.Blue)


                DisplayContent(content = message, action = viewMedia)

            }
        }

            if (message.isOutgoing)
                Avatar(sender =  sender,
                    showAvatar =  showAvatar,
                    viewUser =  viewUser)

        }



}

@Composable
fun Avatar(modifier: Modifier = Modifier,
            sender: UserInterface?,
           showAvatar: Boolean,
            viewUser: (Long)-> Unit)
{
        Column(modifier = modifier
            .padding(2.dp)
            .fillMaxHeight()
            .width(45.dp),
            verticalArrangement = Arrangement.Bottom) {

            if (sender != null && showAvatar) {

                val image by sender.image.collectAsState()

                Image(
                    modifier = modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                        .clickable { viewUser(sender.id) },
                    bitmap = image, contentDescription = "user_image"
                )
            }
        }
}


@Preview
@Composable
fun PreviewChatList()
{
    val testMessages = mutableListOf<MockMessageEntity>()

    var outgoing = true;
    var message = "Message Content Filler"

    for(i in 0 until 16){

        testMessages.add(MockMessageEntity(i, Content.Text,message))

        outgoing = !outgoing
        message += message

    }

    ChatContent(messages = testMessages, viewMedia =  {}, viewUser =  {})
}




@Preview
@Composable
fun PreviewChatBubble()
{
    val v = MockMessageEntity(1, Content.Text, "tester")


            ChatBubble(message = v, viewUser ={} , viewMedia = {}, showAvatar = true, showUserName = true)







}
