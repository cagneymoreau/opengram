package com.cagneymoreau.opengram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cagneymoreau.opengram.ui.theme.WorkStationTheme
import dagger.hilt.android.AndroidEntryPoint

/* BIG */
// TODO: send text/image
// TODO: navigation
// TODO: login is tied to telegram api

/* SMALL */
// TODO: search
// TODO: sen/view graphics and video

/* Features */ 
// TODO: calendar 
// TODO: timesheet 
// TODO: map
// TODO: status for iot

/*Support */
// TODO: 3.0 upgrade 
// TODO: store/ 3.0 paywall annoy 

/* Alternate API */
// TODO: decentralized xmpp or matrix
// TODO: text option 

/* Code Cleanup */
// TODO: test /mock sort 
// TODO: mock repo

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkStationTheme {
                WorkstationNavGraph()
            }
        }
    }
}
