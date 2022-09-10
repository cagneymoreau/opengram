package com.cagneymoreau.opengram

import androidx.navigation.NavController
import com.cagneymoreau.opengram.RouteArgs.KEY
import com.cagneymoreau.opengram.RouteArgs.USERID
import com.cagneymoreau.opengram.WorkStationScreens.ABOUT_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.CALLLOGS_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.CHATLIST_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.CHAT_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.CONTACTS_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.GROUPSETTINGS_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.LOAD_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.LOGIN_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.MEDIA_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.NEWGROUP_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.SETTINGS_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.UPDATE_SCREEN
import com.cagneymoreau.opengram.WorkStationScreens.USERPROFILE_SCREEN


/**
 * Screens - Each Screen should have a package under ui or might share a package
 */

private object WorkStationScreens {

    const val ABOUT_SCREEN = "about"
    const val CALLLOGS_SCREEN = "calllogs"
    const val CHAT_SCREEN = " chat"
    const val CHATLIST_SCREEN = "chatlist"
    const val CONTACTS_SCREEN = "contacts"
    const val LOAD_SCREEN = "load"
    const val LOGIN_SCREEN = "login"
    const val MEDIA_SCREEN = "mediascreen"
    const val NEWGROUP_SCREEN = "newgroup"
    const val GROUPSETTINGS_SCREEN = "groupsettings"
    const val SETTINGS_SCREEN = "settings"
    const val UPDATE_SCREEN = "update"
    const val USERPROFILE_SCREEN = "userprofile"



}


/**
 * Arguments - used in routes
 */

object RouteArgs {

    const val POSITION = "position"
    const val  USERID = "userid"
    const val TIMEPERIOD = "timeperiod"
    const val KEY = "key"

}


/**
 * Destinations used in []
 */
object Destinations {

    const val ABOUT_ROUTE = "$ABOUT_SCREEN"
    const val CALLLOGS_ROUTE = "$CALLLOGS_SCREEN"
    const val CHAT_ROUTE = "$CHAT_SCREEN/{$USERID}?"
    const val CHATLIST_ROUTE = "$CHATLIST_SCREEN"
    const val CONTACTS_ROUTE = "$CONTACTS_SCREEN"
    const val LOAD_ROUTE = "$LOAD_SCREEN"
    const val LOGIN_ROUTE = "$LOGIN_SCREEN"
    const val MEDIA_ROUTE = "$MEDIA_SCREEN/{$KEY}"
    const val NEWGROUP_ROUTE = "$NEWGROUP_SCREEN"
const val GROUPSETTINGS_ROUTE = "$GROUPSETTINGS_SCREEN/{$USERID}?"
    const val SETTINGS_ROUTE = "$SETTINGS_SCREEN"
    const val UPDATE_ROUTE = "$UPDATE_SCREEN"
    const val USERPROFILE_ROUTE = "$USERPROFILE_SCREEN/{$USERID}"



}


class WorkStationNavigationActions(private val navController: NavController)
{

    fun navigateToLogin()
    {
        navController.navigate(LOGIN_SCREEN)
    }

    fun navigateToNewGroup()
    {
        navController.navigate(NEWGROUP_SCREEN)
    }

    fun navigateToGroupSettings(id: Long)
    {
        navController.navigate("$GROUPSETTINGS_SCREEN/$id")
    }

    fun navigateToContacts()
    {
        navController.navigate(CONTACTS_SCREEN)
    }

    fun navigateToChatList()
    {
        navController.navigate(CHATLIST_SCREEN)
    }

    fun navigateToSettings()
    {
        navController.navigate(SETTINGS_SCREEN)
    }

    fun navigateToAbout()
    {
        navController.navigate(ABOUT_SCREEN)
    }

    fun navigateToChat(id: Long)
    {
        navController.navigate("$CHAT_SCREEN/$id")
    }

    fun navigateToProfile(id: Long)
    {
        navController.navigate("$USERPROFILE_SCREEN/$id")
    }

    fun navigateToFullMedia(key: Int)
    {
        navController.navigate("$MEDIA_SCREEN/$key")
    }




}