package com.cagneymoreau.opengram

import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cagneymoreau.opengram.ui.about.AboutScreen
import com.cagneymoreau.opengram.ui.chat.ChatScreen
import com.cagneymoreau.opengram.ui.chatlist.ChatListScreen
import com.cagneymoreau.opengram.ui.contacts.ContactsScreen
import com.cagneymoreau.opengram.ui.group.NewGroupSelectionScreen
import com.cagneymoreau.opengram.ui.loadscreen.LoadScreen
import com.cagneymoreau.opengram.ui.login.LoginScreen
import com.cagneymoreau.opengram.ui.mediadisplay.FullMediaScreen
import com.cagneymoreau.opengram.ui.settings.SettingsScreen
import com.cagneymoreau.opengram.ui.structure.AppModalDrawer
import com.cagneymoreau.opengram.ui.userprofile.UserProfileScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun WorkstationNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = Destinations.LOAD_ROUTE,
    navActions: WorkStationNavigationActions = remember(navController) {
    WorkStationNavigationActions(navController)
    }

)



{

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(navController = navController,
        startDestination = startDestination,
        modifier = modifier){

        composable(route = Destinations.ABOUT_ROUTE){
            AboutScreen(goBack =  navController::popBackStack)
        }

        composable(route = Destinations.CALLLOGS_ROUTE){

        }
        composable(Destinations.CHAT_ROUTE,
            arguments = listOf(
                navArgument(RouteArgs.USERID) { type = NavType.LongType; defaultValue = 0 }
            )){
                entry -> var chatId = entry.arguments?.getLong(RouteArgs.USERID)

            if (chatId == null) chatId = 0;

            ChatScreen(chatId = chatId,
                goBack = {navController.popBackStack()},
                viewUser = {it -> navActions.navigateToProfile(it)},
                viewMedia = {v -> navActions.navigateToFullMedia(v)})

        }
        composable(Destinations.CHATLIST_ROUTE){
            AppModalDrawer(drawerState = drawerState, currentRoute = currentRoute, navigationActions = navActions) {
                ChatListScreen(navToChat =  { v -> navActions.navigateToChat(v)},
                    openDrawer = {coroutineScope.launch { drawerState.open() }})
            }

        }
        composable(route = Destinations.CONTACTS_ROUTE){
            ContactsScreen( goBack = {navController.popBackStack()})
        }
        composable(route = Destinations.LOAD_ROUTE){
            LoadScreen({ navActions.navigateToLogin() }, {navActions.navigateToChatList()})
        }

        composable(route = Destinations.LOGIN_ROUTE){
            LoginScreen({navActions.navigateToChatList()})
        }
        composable(route = Destinations.MEDIA_ROUTE,
            arguments = listOf(
                navArgument(RouteArgs.KEY) { type = NavType.IntType; defaultValue = 0 }
            )){

                entry -> var key = entry.arguments?.getInt(RouteArgs.KEY)

            if (key == null) key = 0

            FullMediaScreen(key = key)
        }
        composable(route = Destinations.NEWGROUP_ROUTE){
            NewGroupSelectionScreen( goBack = { navController.popBackStack() }, navigationActions = navActions)
        }
        composable(route = Destinations.SETTINGS_ROUTE){
                SettingsScreen(goBack =  navController::popBackStack)
        }
        composable(route = Destinations.UPDATE_ROUTE){

        }
        composable(route = Destinations.USERPROFILE_ROUTE){
                UserProfileScreen(goBack =  navController::popBackStack)
        }


    }



}