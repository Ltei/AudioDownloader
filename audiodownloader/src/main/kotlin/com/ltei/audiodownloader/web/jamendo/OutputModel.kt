package com.ltei.audiodownloader.web.jamendo

import com.google.gson.annotations.SerializedName

data class Headers(
    @SerializedName("status") val status: String,
    @SerializedName("code") val code: String,
    @SerializedName("error_message") val errorMessage: String,
    @SerializedName("warnings") val warnings: String,
    @SerializedName("results_count") val resultsCount: String,
    @SerializedName("next") val next: String
)

data class GetObjectResult<T>(
    @SerializedName("headers") val headers: Headers,
    @SerializedName("results") val results: List<T>
)

data class Artist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("website") val website: String,
    @SerializedName("joindate") val joinDate: String,
    @SerializedName("image") val image: String,
    @SerializedName("shorturl") val shortUrl: String,
    @SerializedName("shareurl") val shareUrl: String
)

data class Track(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("artist_id") val artistId: String,
    @SerializedName("artist_name") val artistName: String,
    @SerializedName("artist_idstr") val artistIdStr: String,
    @SerializedName("album_name") val albumName: String,
    @SerializedName("album_id") val albumId: String,
    @SerializedName("license_ccurl") val licenseCcurl: String,
    @SerializedName("position") val position: Int,
    @SerializedName("releasedata") val releaseDate: String,
    @SerializedName("album_image") val albumImage: String,
    @SerializedName("audio") val audio: String,
    @SerializedName("audiodownload") val audioDownload: String,
    @SerializedName("prourl") val proUrl: String,
    @SerializedName("sorturl") val sortUrl: String,
    @SerializedName("shareurl") val shareUrl: String,
    @SerializedName("waveform") val waveForm: String,
    @SerializedName("image") val image: String
)

