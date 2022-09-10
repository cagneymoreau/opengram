package com.cagneymoreau.opengram.ui.structure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.R
import com.cagneymoreau.mockapiintegration.MockRepository
import com.cagneymoreau.opengram.ui.chat.ChatViewModel


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
              viewModel: ChatViewModel = hiltViewModel())
{
    val actions = viewModel.bottomBaraActions()
    if(actions.first.isEmpty()){
        ChatInputBottomBar(
            viewModel = viewModel
        )
    }else{
        ActionBottomBar(names = actions.first, actions = actions.second)
    }

}


/**
 * Bottom bar shows list of buttons with no communication options
 * This can be for example join or mute etc
 */
@Composable
fun ActionBottomBar(
    names: List<String>,
    actions: List<() -> Unit>,
    modifier: Modifier = Modifier
)
{
    Surface(modifier = modifier.fillMaxWidth()) {
        
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in names.indices)
                Button(onClick = actions[i],
                modifier = modifier.weight(1f)) {
                    Text(text = names[i])
                }
        }
        
    }

}


/**
 * Shows normal convo options graphics, type, attach, audio/video
 */
@Composable
fun ChatInputBottomBar(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel
)
{

    var readyForAudio = viewModel.readyForAudio.value
    var recording = viewModel.recording.value
    var recordingTime = viewModel.recordingTime.value

    var showAttach = viewModel.showAttach.value
    var showGraphic = viewModel.showGraphic.value
    var textInput = viewModel.textInput.value

    var pickChoice = viewModel.pickChoice.value

    if (pickChoice != -1){
        MakePick(viewModel)
        viewModel.showAttach.value = false
        return
    }

    Surface(modifier = modifier.fillMaxWidth()) {

        Column(modifier = modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                //GraphicDrawer(show = showGraphic, viewModel)
                AttachDrawer(show = showAttach, viewModel)
            }


            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                if (!recording)
                    TypeInput(
                        viewModel.graphicIcon,
                        {viewModel.showGraphic()},
                        textInput,
                        { v -> viewModel.onTextChanged(v) },
                        viewModel.attachSendIcon.value,
                        {viewModel.attachSendButtonClick()}
                    )
                else{
                    RecordInput(recordingTime)
                }

                Icon(painter = painterResource(id =  viewModel.recordIcon.value) , contentDescription = "record", Modifier.clickable { viewModel.recordButtonClick() } )



            }
        }


    }

}



@Composable
fun TypeInput(
    graphicIcon: Int,
    graphicClick: ()-> Unit,
    text: String,
    onTouch: (String) -> Unit,
    attachSendIcon: Int,
    attachSend: () -> Unit,
    modifier: Modifier = Modifier
)
{
    Row(
        modifier = modifier
    ) {

        Icon(painter = painterResource(id = graphicIcon),
            contentDescription = "stickers",
            modifier.clickable { graphicClick() }
            )

        TextField(value = text, onValueChange = onTouch)

        Icon(painter = painterResource(id = attachSendIcon),
            contentDescription = "attachSend",
            modifier.clickable { attachSend()  })



    }
}

@Composable
fun RecordInput(
    liveText: String
)
{
    Row() {
        Icon(painter = painterResource(id = R.drawable.ic_baseline_delete_sweep_24), contentDescription = "swipe to cancel")
        Text(text = liveText)
    }
}





@Composable
fun GraphicDrawer(
    show: Boolean,
    viewModel: ChatViewModel
)
{
    // TODO:  
//static class StickerSets e

}


 
@Composable
fun AttachDrawer(
    show: Boolean,
    viewModel: ChatViewModel
)
{
    val picks = viewModel.getPickables()

    AnimatedVisibility(visible = show, enter = slideInVertically { 0 },
        exit = slideOutVertically { 0 }
    ) {

        Surface() {

            Column() {


                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    for (i in 0..2) {
                        Column(modifier = Modifier.padding(3.dp)) {

                            // TODO: pickable actions 
                            OutlinedButton(onClick = { viewModel.pickChoice.value = i},
                                modifier= Modifier.size(80.dp),  //avoid the oval shape
                                shape = CircleShape,
                                border= BorderStroke(1.dp, picks[i].color),
                                contentPadding = PaddingValues(10.dp),  //avoid the little icon
                                colors = ButtonDefaults.buttonColors(backgroundColor = picks[i].color)
                            ) {

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(id = picks[i].icon),
                                        contentDescription = "pickitem"
                                    )
                                    Text(text = picks[i].title)
                                }


                            }
                            

                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (i in 3..5) {
                        Column(modifier = Modifier.padding(3.dp)) {

                            OutlinedButton(onClick = { viewModel.pickChoice.value = i},
                                modifier= Modifier.size(80.dp),  //avoid the oval shape
                                shape = CircleShape,
                                border= BorderStroke(1.dp, picks[i].color),
                                contentPadding = PaddingValues(10.dp),  //avoid the little icon
                                colors = ButtonDefaults.buttonColors(backgroundColor = picks[i].color)
                            ) {

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(id = picks[i].icon),
                                        contentDescription = "pickitem"
                                    )
                                    Text(text = picks[i].title)
                                }


                            }
                        }
                    }
                }
            }
        }

    }

}





@Preview
@Composable
fun ChatInputBottomBarPreview()
{
    ChatInputBottomBar(
        viewModel = ChatViewModel(MockRepository())
    )
}



@Preview
@Composable
fun ActionBottomBarPreview()
{
    val names = listOf("Join", "Mute")
    val actions = listOf({}, {})

    ActionBottomBar(names = names, actions = actions)
}



@Preview
@Composable
fun TypeInputBottomBarPreview()
{
    TypeInput(R.drawable.ic_baseline_emoji_emotions_24, {}, "", {}, R.drawable.ic_baseline_attach_file_24, {})
}



@Preview
@Composable
fun AttachDrawerPreview()
{
   AttachDrawer(show = true, viewModel = ChatViewModel(MockRepository()) )
}


@Preview
@Composable
fun GraphicDrawerPreview()
{
    GraphicDrawer(show = true, viewModel = ChatViewModel(MockRepository()))
}

