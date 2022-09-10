package com.cagneymoreau.opengram.ui.settings

import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.ui.structure.TopAppBarGoBack

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    goBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
)
{


    Scaffold( scaffoldState = scaffoldState,
        topBar = {
            TopAppBarGoBack(
                goBack = goBack,  "Settings")
        },
        modifier = modifier
    )
    {
            it ->

        Text(text = "Settings Here")

    }


}


