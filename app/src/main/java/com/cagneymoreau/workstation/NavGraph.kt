package com.cagneymoreau.workstation

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
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cagneymoreau.workstation.ui.chat.ChatScreen
import com.cagneymoreau.workstation.ui.chatlist.ChatListScreen
import com.cagneymoreau.workstation.ui.loadscreen.LoadScreen
import com.cagneymoreau.workstation.ui.login.LoginScreen
import kotlinx.coroutines.CoroutineScope


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

        composable(route = Destinations.LOAD_ROUTE){
            LoadScreen({ navActions.navigateToLogin() }, {navActions.navigateToChatList()})
        }

        composable(route = Destinations.LOGIN_ROUTE){
            LoginScreen({navActions.navigateToChatList()})
        }


        composable(Destinations.CHATLIST_ROUTE){
            ChatListScreen(navToChat =  { v -> navActions.navigateToChat(v)})
        }

        composable(Destinations.CHAT_ROUTE,
            arguments = listOf(
           navArgument(RouteArgs.USERID) { type = NavType.IntType; defaultValue = 0 }
        )){
            entry -> var chatId = entry.arguments?.getLong(RouteArgs.USERID)

            if (chatId == null) chatId = 0;

            ChatScreen(chatId)

        }

        composable(Destinations.TIMECARD_ROUTE){

        }



    }



}