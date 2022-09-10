package com.cagneymoreau.opengram.ui.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.WorkStationNavigationActions
import com.cagneymoreau.opengram.apiinterfaceitems.UserInterface
import com.cagneymoreau.mockapiintegration.MockUserEntity
import com.cagneymoreau.opengram.ui.contacts.ContactsContent
import com.cagneymoreau.opengram.ui.structure.TopAppBarGoBack


@Composable
fun NewGroupSelectionScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    goBack: () -> Unit,
    navigationActions: WorkStationNavigationActions,
    viewModel: NewGroupViewModel = hiltViewModel()
)
{

    Scaffold( scaffoldState = scaffoldState,
        topBar = {
            TopAppBarGoBack(
                goBack = goBack,  "New Group")

        },
        modifier = modifier.fillMaxSize(),

        floatingActionButton = {FloatingActionButton(onClick = { navigationActions.navigateToGroupSettings(0L)} ) {
            Icon(Icons.Filled.ArrowForward,    contentDescription = "next")
        }}

        ) {


        val contacts = viewModel.unSelected.collectAsState()
        val chosen = viewModel.chosen.collectAsState()

        NewGroupSelectionContent(chosen = chosen.value, onClick = { it -> viewModel.unSelected})

        ContactsContent(users = contacts.value, onClick = {it -> viewModel.choose(it)})

    }

}



@Composable
fun NewGroupSelectionContent(
    modifier: Modifier = Modifier,
    chosen: List<UserInterface>,
    onClick: (UserInterface) -> Unit
)
{

    Surface() {
        Column {
            Text(text = "Choose group members")
            LazyRow(){
                items(items = chosen){ user ->
                    
                    UserPill(userInterface = user)
                    
                }
            }
            
        }
    }

}


@Composable
fun UserPill(userInterface: UserInterface)
{
    Box(modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .background(Color.Black)
        ) {

        val img = userInterface.image.collectAsState()
        Row(modifier = Modifier.padding(5.dp)) {
            Image(bitmap = img.value, contentDescription = "user image")
            Text(text = userInterface.name, color = Color.White)
        }

    }
    
}



@Preview
@Composable
fun NewGroupSelectionContentPreview()
{
    val l = List(5) {it -> MockUserEntity(it)}

    NewGroupSelectionContent(chosen = l, onClick = {})

}



@Preview
@Composable
fun UserPillPreview()
{
    Surface() {
        UserPill(userInterface = MockUserEntity(1))
    }

}

