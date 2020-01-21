package com.ltei.audiodownloader.web.jamendo

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

enum class HasImage(val value: String) {
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