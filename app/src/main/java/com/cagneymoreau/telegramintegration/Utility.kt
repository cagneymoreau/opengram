package com.cagneymoreau.opengram.ui.support

import android.graphics.*
import com.cagneymoreau.telegramintegration.TelegramRepository
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.zip.GZIPInputStream

// TODO: move me to UI
fun getInitials(title: String, color: Int): Bitmap {

    var title = title
    if (title.isEmpty()) title = "?"
    val words = title.split(" ").toTypedArray()
    var initials = ""
    if (initials.isNotEmpty()) {
        initials += words[0].substring(0, 1)
    }
    if (words.size > 1 && words[words.size-1].isNotEmpty()) {
        initials += words[words.size - 1].substring(0, 1)
    }
    val b = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    //c.drawARGB(128,0,204,204);
    c.drawColor(color)
    val paint = Paint()
    paint.color = Color.WHITE
    paint.textSize = 48f
    c.drawText(initials, 25f, 60f, paint)
    return b

}

@Throws(IOException::class)
fun decompress(compressed: ByteArray?): String? {
    val BUFFER_SIZE = 32
    val `is` = ByteArrayInputStream(compressed)
    val gis = GZIPInputStream(`is`, BUFFER_SIZE)
    val string = StringBuilder()
    val data = ByteArray(BUFFER_SIZE)
    var bytesRead: Int
    while (gis.read(data).also { bytesRead = it } != -1) {
        string.append(String(data, 0, bytesRead))
    }
    gis.close()
    `is`.close()
    return string.toString()
}



fun establishPath(file: TdApi.File, telegramRepository: TelegramRepository, response: (String) -> Unit)
{

    if (!file.local.path.isEmpty()) {
        val path: String = file.local.path
       response(path)
    } else {
        val handler =
            Client.ResultHandler { `object` ->
                if (TdApi.File.CONSTRUCTOR == `object`.constructor) {
                    val file = `object` as TdApi.File
                    val path = file.local.path
                   response(path)
                }
            }
        val id: Int = file.id
        telegramRepository.downloadFile(id, 1, handler)
    }




}

fun mediaFromPath(path: String) : Bitmap
{
    val options = BitmapFactory.Options()
    //options.inSampleSize = 1 // TODO: make adjustable
    return BitmapFactory.decodeFile(path, options)

}



fun establishLastMessage(file: TdApi.File, telegramRepository: TelegramRepository, response: (String) -> Unit)
{

    if (!file.local.path.isEmpty()) {
        val path: String = file.local.path
        response(path)
    } else {
        val handler =
            Client.ResultHandler { `object` ->
                if (TdApi.File.CONSTRUCTOR == `object`.constructor) {
                    val file = `object` as TdApi.File
                    val path = file.local.path
                    response(path)
                }
            }
        val id: Int = file.id
        telegramRepository.downloadFile(id, 1, handler)
    }




}


