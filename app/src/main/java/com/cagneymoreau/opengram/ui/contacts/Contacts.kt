package com.cagneymoreau.opengram.ui.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import com.cagneymoreau.mockapiintegration.MockUserEntity
import com.cagneymoreau.opengram.ui.structure.TopAppBarGoBack


@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    goBack: () -> Unit,
   contactsViewModel: ContactsViewModel = hiltViewModel()
)
{

    Scaffold( scaffoldState = scaffoldState,
        topBar = {
            TopAppBarGoBack(
                goBack = goBack,  "Contacts")

        },
        modifier = modifier.fillMaxSize(),


    ) {

        val list = contactsViewModel.list

        ContactsContent(users = list, onClick =  {})
    }


}


@Composable
fun ContactsContent(
    modifier: Modifier = Modifier,
    users:List<UserInterface>,
    onClick: (UserInterface) -> Unit
)
{
    Surface(modifier = modifier.fillMaxWidth()) {

        Row() {

        }
        ContactsList(users = users)
    }
}


@Composable
fun ContactsList( modifier: Modifier = Modifier,
                  users:List<UserInterface>)
{
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = users) { user ->
            UserCard(user = user)
        }
    }
}


@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    user: UserInterface
)
{

    val img by user.image.collectAsState()

    Row(modifier = modifier.fillMaxWidth()) {
        Image(bitmap = img, contentDescription = "user photo")

        Column() {
            Text(text = user.name)
            Text(text = user.activeStatus)
        }
    }

}

@Composable
fun ContactHolder()
{

}


@Preview
@Composable
fun ContactsContentPreview()
{

    val list = List(50) { i -> MockUserEntity(i)}

    ContactsContent(users =  list, onClick =  {})

}