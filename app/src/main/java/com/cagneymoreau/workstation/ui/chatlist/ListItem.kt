package com.cagneymoreau.workstation.ui.chatlist

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview



@Composable
fun ListItem( modifier: Modifier = Modifier,
              name:String,
              recentMessage: String,
              time: String,
              unreadCount: String,
              avatar: String,
              action: (Long) -> Unit,
              id: Long
                )
{

    Surface(color = MaterialTheme.colors.background, modifier = modifier.fillMaxWidth().clickable { action(id) }) {

        Row(modifier = modifier.fillMaxWidth(), Arrangement.SpaceAround) {

            if (avatar != null){

               // Image(bitmap = avatar.asImageBitmap(), contentDescription = "")

            }else{
                // TODO:
            }

            Column() {
                Text(text = name)
                Text(text = recentMessage)
            }
            Column() {
                Text(text = time)
                Text(text = unreadCount)
            }


         }
        }


}


@Preview
@Composable
fun PreviewListItem()
{
        ListItem(name = "A Channel",
            recentMessage = "recent message",
            time =  "10:42",
            unreadCount = "500",
            avatar = "", modifier = Modifier,
            action = {},
            id = 0)
}

