package com.cagneymoreau.opengram.ui.chatlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.ChatInterface
import com.cagneymoreau.mockapiintegration.MockChatEntity
import com.cagneymoreau.opengram.ui.structure.BasicTopAppBar


@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatListViewModel = hiltViewModel(),
    navToChat: (Long) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    openDrawer: () -> Unit,
)
{

    Scaffold( scaffoldState = scaffoldState,
        topBar = {
            BasicTopAppBar(
                openDrawer = openDrawer,
                search = viewModel)

        },
        modifier = modifier.fillMaxSize(),


    ) {

        val chatList = viewModel.easyChatList.value

        ChatListContent(items = chatList, action = navToChat)

    }



}

@Composable
fun ChatListContent(modifier: Modifier = Modifier,
                    items: List<ChatInterface>,
                    action: (Long) -> Unit)
{

    Surface(modifier = modifier.fillMaxSize()) {

        Column(modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {
            ChatList(items = items, action = action)

        }
    }

}


@Composable
fun ChatList(modifier: Modifier = Modifier,
             items: List<ChatInterface>,
             action: (Long) -> Unit) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = items) { chat ->
            ListItem(
                action = action,
                chat = chat
                )
        }

    }


}


@Composable
fun ListItem( modifier: Modifier = Modifier,
              action: (Long) -> Unit,
              chat: ChatInterface
)
{
    chat.load()
    val image by chat.image.collectAsState()

    Surface(color = MaterialTheme.colors.background, modifier = modifier
        .fillMaxWidth()
        .clickable { action(chat.id) }) {

        Row(modifier = modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween) {

            Column() {
                Image(modifier = modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Black, CircleShape),
                    bitmap = image, contentDescription = "chat image")
            }
            Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(start = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
                ) {

                var title = chat.title
                if (title.length > 24)
                    title = title.substring(0, 24)

                Text(text = title)

                var lastMess = chat.messageStatus
                if (lastMess.length > 24){
                    lastMess = lastMess.substring(0,24)
                }

                Text(text = lastMess)
            }
            Column(modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End) {
                Text(text = chat.readableTime())
                Text(text = chat.unreadCount.toString())
            }
        }
    }
}


@Preview
@Composable
fun PreviewListItem()
{
    val chat = MockChatEntity(1)


    ListItem(
        modifier = Modifier,
        action = {},
        chat)
}

@Preview
@Composable
fun previewChatList() {

    ChatListContent(items =  List(30) { it -> MockChatEntity(it)}, action = {})

}
