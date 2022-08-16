package com.cagneymoreau.workstation.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.workstation.ui.loadscreen.LoadViewModel
import com.cagneymoreau.workstation.ui.theme.WorkStationTheme



@Composable
fun LoginScreen(
    navToChat: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel())
{
    viewModel.setNavActions(navToChat)
    val title = viewModel.title.value
    val description =  viewModel.description.value
    val input = viewModel.input.value
    val button1Text = viewModel.button1Text.value
    val btn1 = viewModel.button1Action.value

    LoginContent(title = title,
        description = description,
        input =  input,
        button1Text =  button1Text,
        button2Text =  "button 2",
        btn1 =  btn1,
        btn2 =  {},
        editAction = { v -> viewModel.updateText(v)})
}


@Composable
fun LoginContent(modifier: Modifier = Modifier,
                 title: String,
                 description: String,
                 input: String,
                 button1Text: String,
                 button2Text: String,
                 btn1: () -> Unit,
                 btn2: () -> Unit,
                 editAction: (String) -> Unit
                )
{
    Surface() {
        Column(modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text =title)
            Text(text = description)
            TextField(value = input, onValueChange = { txt -> editAction(txt) })
            Button(onClick = btn1 ) {
                Text(text = button1Text)
            }
            Button(onClick = btn2 ) {
                    Text(text = button2Text)
            }
        }
    }
}


@Preview
@Composable
fun previewLogin()
{
    WorkStationTheme() {
        LoginContent(title = "Title", description =  "instruction/description", input =  "", button1Text =  "button 1", button2Text =  "button 2", btn1 =  {}, btn2 =  {}, editAction = {})

    }
}