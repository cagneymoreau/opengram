package com.cagneymoreau.opengram.ui.userprofile

import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.ui.structure.TopAppBarGoBack

@Composable
fun UserProfileScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    goBack: () -> Unit,
    viewModel:UserProfileViewModel = hiltViewModel()
)
{


    Scaffold( scaffoldState = scaffoldState,
        topBar = {
            TopAppBarGoBack(
                goBack = goBack,  "About")
        },
        modifier = modifier
    )
    {
            it ->

        Text(text = "User Profile Screen")

    }



}
