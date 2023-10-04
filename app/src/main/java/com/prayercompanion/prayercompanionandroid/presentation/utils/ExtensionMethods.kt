package com.prayercompanion.prayercompanionandroid.presentation.utils

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.navigate(event: UiEvent.Navigate, builder: NavOptionsBuilder.() -> Unit = {}) {
    this.navigate(route = event.route.name + event.args.joinToString { "/$it" }, builder)
}

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}
