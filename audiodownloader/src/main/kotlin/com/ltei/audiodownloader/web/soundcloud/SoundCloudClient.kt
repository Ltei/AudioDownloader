package com.ltei.audiodownloader.web.soundcloud

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.ltei.audiodownloader.web.WebGlobals
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.lang.reflect.Type

interface SoundCloudClient {

    @GET("/users/{userId}")
    fun getUser(
        @Path("userId") userId: String,
        @Query("client_id") clientId: String
    ): Call<User>

    @GET("/tracks/{trackId}")
    fun getTrack(
        @Path("trackId") trackId: String,
        @Query("client_id") clientId: String
    ): Call<Track>

    @GET("/playlists/{playlistId}")
    fun getPlaylist(
        @Path("playlistId") playlistId: String,
        @Query("client_id") clientId: String
    ): Call<Playlist>

    @GET("/resolve")
    fun resolveResource(
        @Query("client_id") clientId: String,
        @Query("url") url: String
    ): Call<ResolvableResource>

    companion object {
        private const val ROOT_URL = "https://api-v2.soundcloud.com/"
        private const val CLIENT_ID = "3UT1QkKC2kBqMLmSnbLbIps1suqeSlRs"

        val instance: SoundCloudClient = WebGlobals.buildRetrofitClient(
            ROOT_URL,
            gson = GsonBuilder()
                .registerTypeAdapter(ResolvableResource::class.java, ResolvableResourceAdapter())
                .create()
        )

        fun getUser(userId: String) = instance.getUser(
            userId = userId,
            clientId = CLIENT_ID
        )

        fun getTrack(trackId: String) = instance.getTrack(
            trackId = trackId,
            clientId = CLIENT_ID
        )

        fun getPlaylist(playlistId: String) = instance.getPlaylist(
            playlistId = playlistId,
            clientId = CLIENT_ID
        )

        fun resolveResource(url: String) = instance.resolveResource(
            clientId = CLIENT_ID,
            url = url
        )
    }

    // Object model

    interface ResolvableResource {
        val kind: String
    }

    data class Playlist(
        @SerializedName("kind") override val kind: String,
        @SerializedName("id") val id: Int,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("user_id") val userId: Int,
        @SerializedName("duration") val duration: Long,
        @SerializedName("sharing") val sharing: String,
        @SerializedName("tag_list") val tagList: String,
        @SerializedName("permalink") val permalink: String,
        @SerializedName("track_count") val trackCount: String,
        @SerializedName("streamable") val streamable: Boolean,

        @SerializedName("tracks") val tracks: List<Track>,

        @SerializedName("permalink_url") val permalinkUrl: String,
        @SerializedName("reposts_count") val repostsCount: Int,
        @SerializedName("genre") val genre: String,
        @SerializedName("purchase_url") val purchaseUrl: String,
        @SerializedName("description") val description: String,
        @SerializedName("uri") val uri: String,
        @SerializedName("label_name") val labelName: String,
        @SerializedName("set_type") val setType: String,
        @SerializedName("public") val public: Boolean,
        @SerializedName("last_modified") val lastModified: String,
        @SerializedName("license") val license: String
    ): ResolvableResource

    data class Track(
        @SerializedName("comment_count") val commentCount: Int,
        @SerializedName("full_duration") val fullDuration: Long,
        @SerializedName("downloadable") val downloadable: Boolean,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("description") val description: String,
        @SerializedName("media") val media: Media,
        @SerializedName("title") val title: String,
        @SerializedName("publisher_metadata") val publisherMetadata: PublisherMetadata,
        @SerializedName("duration") val duration: Long,
        @SerializedName("has_downloads_left") val hasDownloadsLeft: Boolean,
        @SerializedName("artwork_url") val artworkUrl: String,
        @SerializedName("public") val public: Boolean,
        @SerializedName("streamable") val streamable: Boolean,
        @SerializedName("tag_list") val tagList: String,
        @SerializedName("download_url") val downloadUrl: String,
        @SerializedName("genre") val genre: String,
        @SerializedName("id") val id: Long,
        @SerializedName("reposts_count") val repostsCount: Int,
        @SerializedName("state") val state: String,
        @SerializedName("label_name") val labelName: String,
        @SerializedName("last_modified") val lastModified: String,
        @SerializedName("commentable") val commentable: Boolean,
        @SerializedName("policy") val policy: String,
        @SerializedName("visuals") val visuals: String,
        @SerializedName("kind") override val kind: String,
        @SerializedName("purchase_url") val purchaseUrl: String,
        @SerializedName("sharing") val sharing: String,
        @SerializedName("uri") val uri: String,
        @SerializedName("secret_token") val secretToken: String,
        @SerializedName("download_count") val downloadCount: Int,
        @SerializedName("likes_count") val likesCount: Int,
        @SerializedName("urn") val urn: String,
        @SerializedName("license") val license: String,
        @SerializedName("purchase_title") val purchaseTitle: String,
        @SerializedName("display_date") val displayDate: String,
        @SerializedName("embeddable_by") val embeddableBy: String,
        @SerializedName("release_date") val releaseDate: String,
        @SerializedName("user_id") val userId: Int,
        @SerializedName("monetization_model") val monetizationModel: String,
        @SerializedName("waveform_url") val waveformUrl: String,
        @SerializedName("permalink") val permalink: String,
        @SerializedName("permalink_url") val permalinkUrl: String,
        @SerializedName("user") val user: User,
        @SerializedName("playback_count") val playbackCount: Int
    ): ResolvableResource

    data class Media(
        @SerializedName("transcodings") val transcodings: List<MediaTranscoding>
    )

    data class MediaTranscoding(
        @SerializedName("url") val url: String,
        @SerializedName("preset") val preset: String,
        @SerializedName("duration") val duration: Long,
        @SerializedName("snipped") val snipped: Boolean,
        @SerializedName("format") val format: MediaTranscodingFormat,
        @SerializedName("quality") val quality: String
    )

    data class MediaTranscodingFormat(
        @SerializedName("protocol") val protocol: String,
        @SerializedName("mimeType") val mimeType: String
    )

    data class PublisherMetadata(
        @SerializedName("urn") val urn: String,
        @SerializedName("explicit") val explicit: Boolean,
        @SerializedName("contains_music") val containsMusic: Boolean,
        @SerializedName("p_line_for_display") val pLineForDisplay: String,
        @SerializedName("artist") val artist: String,
        @SerializedName("isrc") val isrc: String,
        @SerializedName("id") val id: Int,
        @SerializedName("album_title") val albumTitle: String,
        @SerializedName("upc_or_ean") val upcOrEan: String,
        @SerializedName("p_line") val pLine: String,
        @SerializedName("release_title") val releaseTitle: String
    )

    data class User(
        @SerializedName("id") val id: Int,
        @SerializedName("kind") override val kind: String,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("first_name") val firstName: String,
        @SerializedName("full_name") val fullName: String,
        @SerializedName("last_modified") val lastModified: String,
        @SerializedName("last_name") val lastName: String,
        @SerializedName("permalink") val permalink: String,
        @SerializedName("permalink_url") val permalinkUrl: String,
        @SerializedName("uri") val uri: String,
        @SerializedName("urn") val urn: String,
        @SerializedName("username") val username: String,
        @SerializedName("verified") val verified: Boolean,
        @SerializedName("city") val city: String,
        @SerializedName("country_code") val countryCode: String
    ): ResolvableResource

    // Serializer

    class ResolvableResourceAdapter: JsonDeserializer<ResolvableResource> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ResolvableResource {
            val jsonObj = json.asJsonObject
            return when (jsonObj["kind"].asString) {
                "user" -> context.deserialize<User>(json, User::class.java)
                "track" -> context.deserialize<Track>(json, Track::class.java)
                "playlist" -> context.deserialize<Playlist>(json, Playlist::class.java)
                else -> throw IllegalStateException()
            }
        }
    }

}