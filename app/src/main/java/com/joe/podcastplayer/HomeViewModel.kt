/*
 *   Copyright 2016 Marco Gomiero
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.joe.podcastplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joe.podcastplayer.base.BaseViewModel
import com.prof.rssparser.Channel
import com.prof.rssparser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class HomeViewModel : BaseViewModel() {

    private val url = "https://feeds.soundcloud.com/users/soundcloud:users:322164009/sounds.rss"
    private lateinit var articleListLive: MutableLiveData<Channel>
    private var parser: Parser = Parser.Builder()
        .context(PodcastPlayerApplication.application)
        // If you want to provide a custom charset (the default is utf-8):
        // .charset(Charset.forName("ISO-8859-7"))
        .cacheExpirationMillis(24L * 60L * 60L * 100L) // one day
        .build()

    private val _snackbar = MutableLiveData<String>()
    val snackbar: LiveData<String>
        get() = _snackbar

    private val _rssChannel = MutableLiveData<Channel>()
    val rssChannel: LiveData<Channel>
        get() = _rssChannel

    private val okHttpClient by lazy {
        OkHttpClient()
    }

    fun onSnackbarShowed() {
        _snackbar.value = null
    }

    fun fetchFeed() {
        viewModelScope.launch {
            try {
                val channel = parser.getChannel(url)
                _rssChannel.postValue(channel)
            } catch (e: Exception) {
                e.printStackTrace()
                _snackbar.value = "An error has occurred. Please retry"
                _rssChannel.postValue(Channel(null, null, null, null, null, null, arrayListOf()))
            }
        }
    }

    fun fetchForUrlAndParseRawData(url: String) {
        val parser = Parser.Builder().build()

        viewModelScope.launch(Dispatchers.IO) {
            val request = Request.Builder()
                .url(url)
                .build()
            val result = okHttpClient.newCall(request).execute()
            val raw = runCatching { result.body()?.string() }.getOrNull()
            if (raw == null) {
                _snackbar.postValue("Something went wrong!")
            } else {
                val channel = parser.parse(raw)
                _rssChannel.postValue(channel)
            }
        }
    }
}
