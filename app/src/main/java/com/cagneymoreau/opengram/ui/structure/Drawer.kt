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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DrawerState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cagneymoreau.opengram.Destinations
import com.cagneymoreau.opengram.WorkStationNavigationActions
import com.cagneymoreau.mockapiintegration.MockRepository
import com.cagneymoreau.mockapiintegration.MockUserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.cagneymoreau.opengram.R

@Composable
fun AppModalDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    drawerViewModel: DrawerViewModel = hiltViewModel(),
    navigationActions: WorkStationNavigationActions,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalDrawer(
        drawerShape = MaterialTheme.shapes.medium,
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                drawerViewModel,
                {navigationActions.navigateToNewGroup()},
                {navigationActions.navigateToContacts()},
                {navigationActions.navigateToChatList()},
                {navigationActions.navigateToSettings()},
                {navigationActions.navigateToAbout()},
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        content()
    }
}

@Composable
private fun AppDrawer(
    currentRoute: String,
    drawerViewModel: DrawerViewModel,
    newGroup: () -> Unit,
    contacts: () -> Unit,
    chatList: () -> Unit,
    settings: () -> Unit,
    about: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        DrawerHeader(drawerViewModel = drawerViewModel)
        DrawerButton(
            painter = painterResource(id = R.drawable.ic_baseline_group_24),
            label = stringResource(id = R.string.new_group),
            isSelected = currentRoute == Destinations.NEWGROUP_ROUTE ,
            action = {
                newGroup()
                closeDrawer()
            }
        )
        DrawerButton(
            painter = painterResource(id = R.drawable.ic_baseline_emoji_people_24),
            label = stringResource(id = R.string.contacts),
            isSelected = currentRoute == Destinations.CONTACTS_ROUTE,
            action = {
                contacts()
                closeDrawer()
            }
        )
        DrawerButton(
            painter = painterResource(id = R.drawable.ic_baseline_format_list_bulleted_24),
            label = stringResource(id = R.string.chatlist),
            isSelected = currentRoute == Destinations.CHATLIST_ROUTE,
            action = {
                chatList()
                closeDrawer()
            }
        )
        DrawerButton(
            painter = painterResource(id = R.drawable.ic_baseline_settings_24),
            label = stringResource(id = R.string.settings),
            isSelected = currentRoute == Destinations.SETTINGS_ROUTE,
            action = {
                settings()
                closeDrawer()
            }
        )
        DrawerButton(
            painter = painterResource(id = R.drawable.ic_baseline_help_24),
            label = stringResource(id = R.string.about),
            isSelected = currentRoute == Destinations.ABOUT_ROUTE,
            action = {
                about()
                closeDrawer()
            }
        )
    }
}



@Composable
private fun DrawerHeader(
    modifier: Modifier = Modifier,
    drawerViewModel: DrawerViewModel
) {
    var self = drawerViewModel.getMyself()
    if (self == null) self = MockUserEntity(10)
    val image = self.image.collectAsState()

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .height(dimensionResource(id = R.dimen.header_height))
            .padding(dimensionResource(id = R.dimen.header_padding))
    ) {
        Image(
            bitmap = image.value,
            contentDescription = stringResource(id = R.string.temp),
            modifier = Modifier.width(dimensionResource(id = R.dimen.header_image_width))
        )
        Text(
            text = stringResource(id = R.string.temp)
        )
    }
}

@Composable
private fun DrawerButton(
    painter: Painter,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tintColor = if (isSelected) {
        MaterialTheme.colors.secondary
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    }

    TextButton(
        onClick = action,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.horizontal_margin))
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painter,
                contentDescription = null, // decorative
                tint = tintColor
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.body2,
                color = tintColor
            )
        }
    }
}

@Preview("Drawer contents")
@Composable
fun PreviewAppDrawer() {

        Surface {
            AppDrawer(
                currentRoute = Destinations.CHAT_ROUTE,
                DrawerViewModel(MockRepository()),
                { },
                {},
                {},
                {},
                {},
                {}
            )
        }

}
