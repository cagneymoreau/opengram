package com.cagneymoreau.opengram.ui.mediadisplay

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.apiinterfaceitems.Content
import com.cagneymoreau.opengram.apiinterfaceitems.ContentInterface
import com.cagneymoreau.opengram.ui.support.mediaFromPath
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import kotlin.random.Random


/** Show images and video in full screen with zoom, pause etc
 *
 * Also maybe use this for preview of sending something so add send option??
 */

@Composable
fun FullMediaScreen(
    modifier: Modifier = Modifier,
    key:Int,
    mediaViewModel: MediaViewModel = hiltViewModel()
)
{
    val content = mediaViewModel.getContent(key)

    DisplayContent(action = {}, content = content)

}


@Composable
fun DisplayContent(
    modifier: Modifier = Modifier,
    action: (Int) -> Unit,
    content: ContentInterface
)
{
    Column(modifier = modifier.clickable { action(Random(2134).nextInt(500,1000))}) {

        when (content.content) {

            Content.Text -> {
                TextContent(content = content.textContent)
            }
            Content.Photo -> {
                PictureContent(content = content.mediaPath, caption = content.textContent)
            }
            Content.Audio -> {
                AudioContent(path = content.mediaPath)
            }
            Content.AudioNote -> {
                AudioNoteContent(path = content.mediaPath)
            }
            Content.Video -> {
                VideoContent(path = content.mediaPath)
            }
            Content.VideoNote -> {
                VideoNoteContent(path = content.mediaPath)
            }
            Content.Document -> {
                Text(text = "not yet")
            }
            Content.Sticker -> {
                Text(text = "not yet")
            }
            Content.Animation -> {
                Text(text = "not yet")
            }
            Content.WebContent -> {
                WebPreviewContent(content = content)
            }
            else -> {
                Text(text = "Unknown Content Type!")
            }

        }
    }
}

@Composable
fun TextContent(content: String)
{
    Text(text = content, color = Color.White)
}

@Composable
fun WebPreviewContent(content: ContentInterface)
{

        Column() {
            Text(text = content.textContent)
            Text(text = content.description.substring(0, 50))

            if (content.mediaPath.value.isNotEmpty()) {
                Image(
                    bitmap = mediaFromPath(content.mediaPath.value).asImageBitmap(),
                    contentDescription = "picture message"
                )

            }
        }

}



@Composable
fun PictureContent(content: MutableState<String>, caption: String)
{

    if (content.value.isNotEmpty()) {

        Column() {

            Image(
                bitmap = mediaFromPath(content.value).asImageBitmap(),
                contentDescription = "picture message"
            )
            Text(text = caption)
        }
    }

}

@Composable
fun AudioContent(path: MutableState<String>)
{
    // This is the official way to access current context from Composable functions
    val context = LocalContext.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember (context){
        ExoPlayer.Builder(context).build()
    }


    AndroidView(factory = {context -> StyledPlayerControlView(context).apply { player = exoPlayer } })

    if (path.value.isNotEmpty()) {

        val mediaItem = MediaItem.fromUri(path.value)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

    }

}

@Composable
fun AudioNoteContent(path: MutableState<String>)
{
    Text(text = "empty")
}


@Composable
fun VideoContent(path: MutableState<String>)
{
    // This is the official way to access current context from Composable functions
    val mContext = LocalContext.current

    // Do not recreate the player everytime this Composable commits
    val exoPlayer = remember (mContext){
        ExoPlayer.Builder(mContext).build()
    }

    exoPlayer.videoSize

    AndroidView(factory = {context -> PlayerView(context).apply { player = exoPlayer } },
        modifier = Modifier.width(200.dp).height(400.dp))

    if (path.value.isNotEmpty()) {

        val mediaItem = MediaItem.fromUri(path.value)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.volume = 0f
        exoPlayer.play()


    }

}

@Composable
fun VideoNoteContent(path: MutableState<String>)
{

}



@Composable
fun DocumentPreviewContent()
{

}




@Composable
fun MediaViewerBar()
{

}

@Composable
fun MediaPickerBar()
{

}


@Preview
@Composable
fun DisplayTextPreview()
{
    TextContent(content = "Here is some text")
}

@Preview
@Composable
fun DisplayPhotoPreview()
{

}


@Preview
@Composable
fun DisplayPhotoPickerPreview()
{

}