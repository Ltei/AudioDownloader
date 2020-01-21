package com.ltei.audiodownloader.web

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoClient {

    @GET("$API_VERSION/tracks/file")
    fun getTrackFile(
        @Query("client_id") clientId: String,
        @Query("fullcount") fullCount: Boolean,
        @Query("audioformat") audioFormat: AudioFormat,
        @Query("action") action: Action,
        @Query("id") id: Int
    ): Call<ResponseBody>

    @GET("$API_VERSION/tracks")
    fun getTrack(
        @Query("client_id") clientId: String,
        @Query("format") format: String? = null,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: String = "all",
        @Query("order") order: String? = null,
        @Query("fullcount") fullCount: Boolean? = null,
        @Query("id") id: String? = null,
        @Query("name") name: String? = null,
        @Query("namesearch") nameSearch: String? = null,
        @Query("type") type: String? = null,
        @Query("album_id") albumId: Int? = null,
        @Query("album_name") albumName: String? = null,
        @Query("artist_id") artistId: Int? = null,
        @Query("artist_name") artistName: String? = null,
        @Query("datebetween") dateBetween: String? = null,
        @Query("featured") featured: String? = null,
        @Query("imagesize") imageSize: Int? = null,
        @Query("audioformat") audioFormat: String? = null,
        @Query("audiodlformat") audioDlFormat: String? = null,
        @Query("tags") tags: String? = null,
        @Query("fuzzytags") fuzzyTags: String? = null,
        @Query("acousticelectric") acousticElectric: String? = null,
        @Query("vocalinstrumental") vocalInstrumental: String? = null,
        @Query("gender") gender: String? = null,
        @Query("speed") speed: String? = null,
        @Query("lang") lang: String? = null,
        @Query("durationbetween") durationBetween: String? = null,
        @Query("xartist") xArtist: String? = null,
        @Query("search") search: String? = null,
//        @Query("prolicensing") proLicensing: Boolean? = null,
//        @Query("probackground") proBackground: Boolean? = null,
        @Query("ccsa") ccsa: Boolean? = null,
        @Query("ccnd") ccnd: Boolean? = null,
        @Query("ccnc") ccnc: Boolean? = null,
        @Query("include") include: String? = null,
        @Query("groupby") groupBy: String? = null,
        @Query("boost") boost: String? = null
    ): Call<ResponseBody>

    // Static

    companion object {
        private const val HOST = "http://api.jamendo.com/"
        private const val API_VERSION = "v3.0"
        val IMAGE_SIZES = listOf(25, 35, 50, 55, 60, 65, 70, 75, 85, 100, 130, 150, 200, 300, 400, 500, 600)
        val instance: JamendoClient = WebGlobals.buildRetrofitClient(HOST)

        fun getTrack(
            clientId: String,
            format: Format? = null,
            offset: Int = 0,
            limit: Int? = null,
            order: Order? = null,
            fullCount: Boolean? = null,
            id: String? = null,
            name: String? = null,
            nameSearch: String? = null,
            type: Type? = null,
            albumId: Int? = null,
            albumName: String? = null,
            artistId: Int? = null,
            artistName: String? = null,
            dateBetween: String? = null,
            featured: Featured? = null,
            imageSize: Int? = null,
            audioFormat: AudioFormat? = null,
            audioDlFormat: AudioFormat? = null,
            tags: String? = null,
            fuzzyTags: String? = null,
            acousticElectric: AcousticElectric? = null,
            vocalInstrumental: VocalInstrumental? = null,
            gender: Gender? = null,
            speed: Speed? = null,
            lang: String? = null,
            durationBetween: String? = null,
            xArtist: String? = null,
            search: String? = null,
//            proLicensing: Boolean? = null,
//            proBackground: Boolean? = null,
            ccsa: Boolean? = null,
            ccnd: Boolean? = null,
            ccnc: Boolean? = null,
            include: Include? = null,
            groupBy: GroupBy? = null,
            boost: Boost? = null
        ): Call<ResponseBody> = instance.getTrack(
            clientId = clientId,
            format = format?.value,
            offset = offset,
            limit = if (limit == null || limit < 0) "all" else limit.toString(),
            order = order?.value,
            fullCount = fullCount,
            id = id,
            name = name,
            nameSearch = nameSearch,
            type = type?.value,
            albumId = albumId,
            albumName = albumName,
            artistId = artistId,
            artistName = artistName,
            dateBetween = dateBetween,
            featured = featured?.value,
            imageSize = imageSize,
            audioFormat = audioFormat?.value,
            audioDlFormat = audioDlFormat?.value,
            tags = tags,
            fuzzyTags = fuzzyTags,
            acousticElectric = acousticElectric?.value,
            vocalInstrumental = vocalInstrumental?.value,
            gender = gender?.value,
            speed = speed?.value,
            lang = lang,
            durationBetween = durationBetween,
            xArtist = xArtist,
            search = search,
//            proLicensing = proLicensing,
//            proBackground = proBackground,
            ccsa = ccsa,
            ccnd = ccnd,
            ccnc = ccnc,
            include = include?.value,
            groupBy = groupBy?.value,
            boost = boost?.value
        )
    }

    enum class Order(val value: String) {
        Relevance("relevance"),
        BuzzRate("buzzrate"),
        DownloadsWeek("downloads_week"),
        DownloadsMonth("downloads_month"),
        DownloadsTotal("downloads_total"),
        ListensWeek("listens_week"),
        ListensMonth("listens_month"),
        ListensTotal("listens_total"),
        PopularityWeek("popularity_week"),
        PopularityMonth("popularity_month"),
        PopularityTotal("popularity_total"),
        Name("name"),
        AlbumName("album_name"),
        ArtistName("artist_name"),
        ReleaseDate("releasedate"),
        Duration("duration"),
        Id("id")
    }

    enum class Type(val value: String) {
        Single("single"),
        AlbumTrack("albumtrack")
    }

    enum class Featured(val value: String) {
        True("true"),
        One("1")
    }

    enum class AudioFormat(val value: String) {
        MP31("mp31"),
        MP32("mp32"),
        OGG("ogg"),
        FLAC("flac")
    }

    enum class Action(val value: String) {
        DOWNLOAD("download"),
        STREAM("stream")
    }

    enum class Format(val value: String) {
        XML("xml"),
        JSON("json"),
        JSON_PRETTY("jsonpretty")
    }


    enum class AcousticElectric(val value: String) {
        Acoustic("acoustic"),
        Electric("electric")
    }

    enum class VocalInstrumental(val value: String) {
        Vocal("vocal"),
        Instrumental("instrumental")
    }

    enum class Gender(val value: String) {
        Male("male"),
        Female("female")
    }

    enum class Speed(val value: String) {
        VeryLow("verylow"),
        Low("low"),
        Medium("medium"),
        High("high"),
        VeryHigh("veryhigh")
    }

    enum class Include(val value: String) {
        Licenses("licenses"),
        MusicInfo("musicinfo"),
        Stats("stats"),
        Lyrics("lyrics")
    }

    enum class GroupBy(val value: String) {
        ArtistId("artist_id"),
        AlbumId("album_id")
    }

    enum class Boost(val value: String) {
        BuzzRate("buzzrate"),
        DownloadsWeek("downloads_week"),
        DownloadsMonth("downloads_month"),
        DownloadsTotal("downloads_total"),
        ListensWeek("listens_week"),
        ListensMonth("listens_month"),
        ListensTotal("listens_total"),
        PopularityWeek("popularity_week"),
        PopularityMonth("popularity_month"),
        PopularityTotal("popularity_total")
    }

}