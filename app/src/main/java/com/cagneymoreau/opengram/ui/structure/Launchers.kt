package com.cagneymoreau.opengram.ui.structure

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.cagneymoreau.opengram.ui.chat.ChatViewModel



@Composable
fun MakePick(viewModel: ChatViewModel)
{
    //println("testing")
    val i =  viewModel.pickChoice.value
    when(i){

        0-> FilePicker()
        else -> {}


    }

    viewModel.pickChoice.value = -1

}



@Composable
fun FilePicker()
{
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        val item = context.contentResolver.openInputStream(it)
        val bytes = item?.readBytes()
    }
    SideEffect {
        launcher.launch("*/*")
    }
}

@Composable
fun GalleryPicker(callback: (Uri) -> Unit)
{

}

@Composable
fun CameraPicker(callback: (Uri) -> Unit)
{

}

@Composable
fun LocationPicker(callback: (Uri) -> Unit)
{

}

@Composable
fun PollPicker(callback: (Uri) -> Unit)
{

}

@Composable
fun ContactPicker(callback: (Uri) -> Unit)
{

}