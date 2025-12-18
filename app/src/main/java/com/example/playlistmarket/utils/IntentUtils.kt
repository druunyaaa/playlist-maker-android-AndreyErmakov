package com.example.playlistmarket.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmarket.R

fun shareApp(context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_link))
    }
    context.startActivity(Intent.createChooser(intent, null))
}

fun contactSupport(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email)))
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_subject))
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_message))
    }
    context.startActivity(intent)
}

fun openAgreement(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.agreement_link)))
    context.startActivity(intent)
}