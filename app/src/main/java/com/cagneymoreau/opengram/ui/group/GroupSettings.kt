package com.cagneymoreau.opengram.ui.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.WorkStationNavigationActions
import com.cagneymoreau.mockapiintegration.MockChatEntity
import com.cagneymoreau.opengram.ui.contacts.ContactsContent
import com.cagneymoreau.opengram.ui.structure.TopAppBarGoBack


@Composable
fun GroupSettings(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    goBack: () -> Unit,
    groupID: Long,
    navigationActions: WorkStationNavigationActions,
    viewModel: GroupSettingsViewModel = hiltViewModel()
)
{

    Scaffold( scaffoldState = scaffoldState,
        topBar = {
            TopAppBarGoBack(
                goBack = goBack,  "Group Settings")

        },
        modifier = modifier.fillMaxSize(),

        floatingActionButton = {
            FloatingActionButton(onClick =  {viewModel.save()} ) {
            Icon(Icons.Filled.Check,    contentDescription = "next")
        }
        }

    ) {

        viewModel.getGroupDetails(groupID, { it -> navigationActions.navigateToChat(it)})


        val members = viewModel.members
        val img = viewModel.chat!!.image.collectAsState()
        val title = viewModel.title.value
        val description = viewModel.description.value

        GroupInfo(img.value,
            title = title,
            description = description,
            viewModel::chooseImage,
            viewModel::updateTitle,
            viewModel::updateDescription
            )

        ContactsContent(users = members, onClick = {/* Do Nothing */})

    }

}



@Composable
fun GroupInfo(
    img: ImageBitmap,
    title: String,
    description: String,
    chooseImage: () -> Unit,
    updateTitle: (String) -> Unit,
    updateDescription: (String) -> Unit
    )
{

    Column() {

        Row() {
            Image(bitmap = img, contentDescription = "group avatar")
            TextField(value = title, onValueChange = {it -> updateTitle(it)})

        }
        TextField(value = description, onValueChange = {it -> updateDescription(it)})

    }

}


@Preview
@Composable
fun GroupInfoPreview()
{
    val chat = MockChatEntity(1)
    val img = chat.image.collectAsState()
    GroupInfo(img = img.value, title = chat.title, description = chat.description, {}, {}, {} )

}
