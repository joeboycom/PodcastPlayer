package com.joe.podcastplayer.extension

import android.support.v4.media.MediaMetadataCompat
import com.prof.rssparser.FeedItem

fun FeedItem.toMediaMetadataCompat(): MediaMetadataCompat =
    MediaMetadataCompat.Builder().also {
        it.id = guid.toString()
        it.title = title
        it.artist = author
        it.albumArtUri = image
        it.mediaUri = audio.toString()

        it.displayTitle = title
        it.displaySubtitle = author
        it.displayDescription = description
        it.displayIconUri = image
    }.build()

@JvmName("toMediaMetadataCompatFeedItem")
fun List<FeedItem>.toMediaMetadataCompat(): List<MediaMetadataCompat> =
    this.map { it.toMediaMetadataCompat() }