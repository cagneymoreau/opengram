package com.cagneymoreau.workstation.ui.parts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SearchBar(modifier: Modifier = Modifier)
{
    TextField(value = "", onValueChange = {},
        leadingIcon = {Icon(imageVector =  Icons.Default.Search, contentDescription = null)} ,
        modifier = modifier.fillMaxWidth().heightIn(56.dp))

}