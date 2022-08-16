package com.cagneymoreau.workstation

import android.icu.text.Transliterator
import androidx.navigation.NavController
import com.cagneymoreau.workstation.Destinations.CHAT_ROUTE
import com.cagneymoreau.workstation.RouteArgs.USERID
import com.cagneymoreau.workstation.WorkStationScreens.CHATLIST_SCREEN
import com.cagneymoreau.workstation.WorkStationScreens.CHAT_SCREEN
import com.cagneymoreau.workstation.WorkStationScreens.LOAD_SCREEN
import com.cagneymoreau.workstation.WorkStationScreens.LOGIN_SCREEN
import com.cagneymoreau.workstation.WorkStationScreens.TIME_CARD

/**
 * Screens - Each Screen should have a package under ui or might share a package
 */

private object WorkStationScreens {

    const val LOAD_SCREEN = "load"
    const val LOGIN_SCREEN = "login"
    const val CHAT_SCREEN = " chat"
    const val CHATLIST_SCREEN = "chatlist"
    const val TIME_CARD = "timecard"
    const val GROUPVIEW = "groupview"
    const val TODO = "todolist"

}


/**
 * Arguments - used in routes
 */

object RouteArgs {

    const val POSITION = "position"
    const val  USERID = "userid"
    const val TIMEPERIOD = "timeperiod"


}


/**
 * Destinations used in []
 */
object Destinations {

    const val LOAD_ROUTE = "$LOAD_SCREEN"
    const val LOGIN_ROUTE = "$LOGIN_SCREEN"
    const val CHATLIST_ROUTE = "$CHATLIST_SCREEN"
    const val CHAT_ROUTE = "$CHAT_SCREEN/{$USERID}?"
    const val TIMECARD_ROUTE = "$TIME_CARD"

}


class WorkStationNavigationActions(private val navController: NavController)
{

    fun navigateToLogin()
    {
        navController.navigate(Destinations.LOGIN_ROUTE)
    }

    fun navigateToChatList()
    {
        navController.navigate(Destinations.CHATLIST_ROUTE)
    }

    fun navigateToChat(id: Long)
    {
        navController.navigate("$CHAT_ROUTE/$id")
    }



}