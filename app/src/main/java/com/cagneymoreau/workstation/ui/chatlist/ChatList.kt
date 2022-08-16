package com.cagneymoreau.workstation.ui.chatlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel = hiltViewModel(),
    navToChat: (Long) -> Unit
)
{

    val chatList = viewModel.easyChatList.value


    ChatListContent(items = chatList, action = navToChat)

}

@Composable
fun ChatListContent(modifier: Modifier = Modifier,
                    items: List<ChatDescriptionEntity>,
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
fun ChatList(modifier: Modifier = Modifier, items: List<ChatDescriptionEntity>, action: (Long) -> Unit) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = items) { chat ->
            ListItem(name = chat.title,
                recentMessage = chat.lastMess,
                time = chat.time,
                unreadCount = chat.count,
                avatar = "",
                action = action,
                id = chat.id
                )
        }

    }


}
    @Preview
    @Composable
    fun previewChatList() {
        
        ChatListContent(items =  List(30) { it -> ChatDescriptionEntity("", "title $it",
            "lastMessage", "8:00pm", "25", 234543890L, it.toLong()
        )}, action = {})

    }



