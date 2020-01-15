package com.ltei.audiodownloader.web

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.ltei.audiodownloader.misc.util.fromJson
import com.ltei.audiodownloader.misc.util.getOrNull
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmClient {

    @GET("/2.0/")
    fun searchTrack(
        @Query("track") track: String,
        @Query("method") method: String = "track.search",
        @Query("api_key") apiKey: String = API_KEY,
        @Query("format") format: String = "json"
    ): Call<ResponseBody>

    @GET("/2.0/")
    fun getTrackInfo(
        @Query("mbid") mbid: String,
        @Query("method") method: String = "track.getInfo",
        @Query("api_key") apiKey: String = API_KEY,
        @Query("format") format: String = "json"
    ): Call<ResponseBody>

    companion object {
        private const val APP_NAME = "AutoYoutube"
        private const val API_KEY = "10439db247865c4a854058a5bdd8336e"
        private const val SHARED_SECRET = "ec6cfc5b754dc273f0cbbad4b2c99f61"

        private val GSON = Gson()
        val instance: LastFmClient = WebGlobals.buildRetrofitClient("http://ws.audioscrobbler.com/")

        fun searchTrack(track: String): List<SearchTrackResult> {
            val response = instance.searchTrack(track).execute()
            try {
                val json = JsonParser.parseString(response.body()!!.string())
                return json.asJsonObject["results"]
                    .asJsonObject["trackmatches"]
                    .asJsonObject["track"].asJsonArray.map {
                    val obj = it.asJsonObject
                    SearchTrackResult(
                        name = obj["name"].asString,
                        artist = obj["artist"].asString,
                        mbid = obj["mbid"].asString
                    )
                }
            } catch (e: Exception) {
                val code = response.code()
                val body = response.body()?.string()
                val err = response.errorBody()?.string()
                throw IllegalStateException("${LastFmClient::class.java.simpleName} received bad response :\nCode:$code\n${body}\n\nError:\n${err}")
            }
        }

        fun getTrackInfo(mbid: String): TrackInfo {
            val response = instance.getTrackInfo(mbid).execute()
            val json = JsonParser.parseString(response.body()!!.string())
            val trackJson = json.asJsonObject["track"].asJsonObject
            return TrackInfo(
                name = trackJson["name"].asString,
                mbid = trackJson["mbid"].asString,
                artist = GSON.fromJson(trackJson["artist"]),
                album = GSON.fromJson(trackJson["album"]),
                topTags = trackJson["toptags"].asJsonObject["tag"].asJsonArray.map {
                    it.asJsonObject["name"].asString
                },
                wiki = trackJson.getOrNull("wiki")?.let { GSON.fromJson<TrackInfo.Wiki>(it) }
            )
        }
    }

    data class SearchTrackResult(
        val name: String,
        val artist: String,
        val mbid: String
    )

    data class TrackInfo(
        val name: String,
        val mbid: String,
        val artist: Artist,
        val album: Album,
        val topTags: List<String>,
        val wiki: Wiki?
    ) {
        data class Artist(
            val name: String
        )

        data class Album(
            val title: String
        )

        data class Wiki(
            val published: String,
            val summary: String,
            val content: String
        )
    }

}