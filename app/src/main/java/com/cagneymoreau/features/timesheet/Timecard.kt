package com.cagneymoreau.features.timesheet


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import com.cagneymoreau.opengram.ui.theme.manilla_light
import java.time.Instant
import java.time.ZoneId

/**
 *
 * TimeSheet for tracking work
 */


@Composable
fun DailyView(list: List<TimeCardEntry>, name: String, date: String, header: String )
{

    Column() {
        Header(name = name, date = date, header = header)

        LazyColumn()
        {
            items(items = list) { demoList ->
                SingleEntry(entry = demoList)
            }

        }
    }

}


@Composable
fun SingleEntry(entry: TimeCardEntry)
{
    androidx.compose.material.Surface(
        color = manilla_light,
        modifier = Modifier
            .padding(5.dp, 5.dp)
            .fillMaxWidth()) {

        Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly) {

            Column(verticalArrangement = Arrangement.Center) {
                Text(text = entry.title)
            }
            Column(verticalArrangement = Arrangement.SpaceEvenly ) {
                Text(text = "in")
                Text(text = "out")
            }
            Column( verticalArrangement = Arrangement.Center) {
               Text(text = getLocalTime(entry.start))
                Text(text = getLocalTime(entry.finish))
            }

        }


    }

}


@Composable
fun Header(name: String, date: String, header: String)
{
    androidx.compose.material.Surface(
        color = manilla_light,
        modifier = Modifier
            .padding(5.dp, 5.dp)
            .fillMaxWidth()

    ) {

        Column(modifier = Modifier.border(2.dp, Color.Red, RectangleShape)) {
            Text(text = header)
            Text(text = name, )
            Text(text = date)
        }







    }

}


@Preview()
@Composable
fun Preview()
{
    val demoList: List<TimeCardEntry> =  listOf(
        TimeCardEntry(1657010000000,1657020000000,0,"office",""),
        TimeCardEntry(1657020000001,1657030000000,1,"1st st",""),
        TimeCardEntry(1657030000001,1657040000000,2,"p. firm",""),
        TimeCardEntry(1657040000001,1657050000000,3,"office",""),
        TimeCardEntry(0,0,0,"",""),
        TimeCardEntry(0,0,0,"",""),
        TimeCardEntry(0,0,0,"",""),
        TimeCardEntry(0,0,0,"",""),
        TimeCardEntry(0,0,0,"","")
    )

    DailyView(list = demoList, name = "MyName", date = "05/07/2022", header = "TimeCard")

}

fun getLocalTime(now: Long): String{

    if (now == 0L) return ""

    var time = Instant.ofEpochMilli(now).atZone(ZoneId.systemDefault()).toLocalDateTime()

    var hr = time.hour
    if (hr > 12) hr -= 12

    return  "$hr:${time.minute}"

}