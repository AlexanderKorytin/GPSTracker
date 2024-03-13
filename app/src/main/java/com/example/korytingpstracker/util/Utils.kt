package com.example.korytingpstracker.util

import android.annotation.SuppressLint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun getTime(timeInMillis: Long): String {
    val formater = SimpleDateFormat("HH:mm:ss")
    formater.timeZone = TimeZone.getTimeZone("UTC")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    return formater.format(calendar.time)
}

fun getDate(): String {
    val formater = SimpleDateFormat("dd.MM.yyyy HH:mm")
    val calendar = Calendar.getInstance()
    return formater.format(calendar.time)
}