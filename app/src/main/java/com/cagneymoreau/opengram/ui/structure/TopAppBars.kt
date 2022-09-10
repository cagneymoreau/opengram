/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cagneymoreau.opengram.ui.structure
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cagneymoreau.opengram.R
import com.cagneymoreau.mockapiintegration.MockSearchAbleEntity


@Composable
fun BasicTopAppBar(
    openDrawer: () -> Unit,
    search: Searchable? = null
) {

   if (search != null) {

        TopAppBar(
            title = { "" },
            navigationIcon = {
                IconButton(onClick = openDrawer) {
                    Icon(Icons.Filled.Menu, stringResource(id = R.string.open_drawer))
                }
            },
            actions = {AppBarSearch(title = stringResource(id = R.string.app_name), search)},
            modifier = Modifier.fillMaxWidth()
        )
    }else{

       TopAppBar(
           title = { Text(text = stringResource(id = R.string.app_name)) },
           navigationIcon = {
               IconButton(onClick = openDrawer) {
                   Icon(Icons.Filled.Menu, stringResource(id = R.string.open_drawer))
               }
           },
           modifier = Modifier.fillMaxWidth()
       )
   }
}




@Composable
fun TopAppBarGoBack(
    goBack: () -> Unit,
    description: String,
    search: Searchable? = null
) {

     if (search != null) {
        TopAppBar(
            title = { Text(text = "") },
            navigationIcon = {
                IconButton(onClick = goBack) {
                    Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.go_back))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            actions = {AppBarSearch(title = description, search)}

        )
    }else{
        TopAppBar(
            title = { Text(text = description) },

            navigationIcon = {
                IconButton(onClick = goBack) {
                    Icon(Icons.Filled.ArrowBack, stringResource(id = R.string.go_back))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun AppBarSearch(
    title: String,
    search: Searchable
)
{
    var searching by remember { mutableStateOf(false) }
    val query = search.searchTarget.value

    if (searching){
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            textStyle =  MaterialTheme.typography.h6,
            onValueChange = { it -> search.query(it)},
            trailingIcon = {
                IconButton(onClick = {
                    searching = false }) {
                    Icon(Icons.Filled.Close, stringResource(id = R.string.close))
                }

            }
        )

    }else{

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize()) {

            ProvideTextStyle(value = MaterialTheme.typography.h6) {

            }

            Column(horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f).fillMaxHeight()) {
                Text(text = title,

                    style = MaterialTheme.typography.h6,
                    color =  MaterialTheme.colors.background.copy(ContentAlpha.high)
                )
            }

            Column(horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f).fillMaxHeight()) {
                IconButton(onClick = {searching = true }) {
                    Icon(Icons.Filled.Search, stringResource(id = R.string.search))
                }
            }



        }


    }



}



@Preview
@Composable
private fun BasicTopAppBarPreview() {

        Surface {
            Column() {
                BasicTopAppBar({})
                BasicTopAppBar({}, MockSearchAbleEntity())
            }

        }

}



@Preview
@Composable
private fun TopAppBarGoBackPreview() {

        Surface {
            Column() {
                TopAppBarGoBack ({}, "FamBam")
                TopAppBarGoBack ({}, "FamBam", search = MockSearchAbleEntity())
            }

        }

}


@Preview
@Composable
private fun AppBarSearchPreview() {

    Surface {
        AppBarSearch ("FamBam", MockSearchAbleEntity())
    }

}

