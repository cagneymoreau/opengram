package com.cagneymoreau.opengram.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.ui.structure.TopAppBarGoBack


@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    goBack: () -> Unit,
    viewModel: AboutViewModel = hiltViewModel()
)
{
    val about = viewModel.getAbout()
    val click = viewModel::click

    Scaffold( scaffoldState = scaffoldState,
        topBar = {
            TopAppBarGoBack(
                goBack = goBack,  "About")
        },
        modifier = modifier
        )
        {
            it ->

            AboutContent(about = about, click = click)

        }



}



@Composable
fun AboutContent(
    modifier: Modifier = Modifier,
    about: String,
    click: () -> Unit
)
{
    Surface() {
        Text(text = about, Modifier.clickable { click })

    }


}

/*
@Composable
fun Test(
    modifier: Modifier = Modifier,
    about: String,
    click: () -> Unit
)
{
    val state = rememberLazyListState()
    val fullyVisibleIndices: List<Int> by remember {
        derivedStateOf {
            val layoutInfo = state.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                emptyList()
            } else {
                val fullyVisibleItemsInfo = visibleItemsInfo.toMutableList()

                val lastItem = fullyVisibleItemsInfo.last()

                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

                if (lastItem.offset + lastItem.size > viewportHeight) {
                    fullyVisibleItemsInfo.removeLast()
                }

                val firstItemIfLeft = fullyVisibleItemsInfo.firstOrNull()
                if (firstItemIfLeft != null && firstItemIfLeft.offset < layoutInfo.viewportStartOffset) {
                    fullyVisibleItemsInfo.removeFirst()
                }

                fullyVisibleItemsInfo.map { it.index }
            }
        }
    }
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(30.dp)
    ) {
        items(100) {
            Text(
                it.toString(),
                modifier = Modifier
                    .background(if (fullyVisibleIndices.contains(it)) Color.Green else Color.Transparent)
                    .padding(30.dp)
            )
        }
    }


}


 */



@Preview
@Composable
fun AboutContentPreview(modifier: Modifier = Modifier)
{
    AboutContent(about = "Opengram is a UI that could potentially overlay other app api as UI." +
            " The goal is a community buildng app that includes chatting, commnity calendar" +
            " and hopefully p2p decentralyzed messaging", click =  {})

}
