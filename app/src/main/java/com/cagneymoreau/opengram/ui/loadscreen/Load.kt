package com.cagneymoreau.opengram.ui.loadscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.R



@Composable
fun LoadScreen(
    navToLogin: () -> Unit,
    navToChat: () -> Unit,
    viewModel: LoadViewModel = hiltViewModel())
{
    viewModel.setNavActions(navToLogin, navToChat)
    LoadContent()
}


@Composable
fun LoadContent(
    modifier: Modifier = Modifier
)
{
    Surface() {
        Column(modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {

            LogoDisplay(logo = painterResource(id = R.drawable.ic_launcher_foreground))

        }
    }

}


@Composable
fun LogoDisplay(logo: Painter)
{
    Image(painter = logo, contentDescription = "workstationlogo" )
}


@Preview
@Composable
fun previewLoadingScreen()
{
    LoadContent()
}