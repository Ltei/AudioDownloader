package com.ltei.audiodownloader.model.audiometadata

import com.ltei.ljuutils.datamanager.ManagedByClass
import com.ltei.ljuutils.datamanager.ObjectManagerFactory

class AudioAlbum(
    @ManagedByClass(AudioMetadataUtils.NamePropertyManager::class)
    var title: String? = null,

    @ManagedByClass(AudioMetadataUtils.UrlListPropertyManager::class)
    var images: List<String>? = null
) {
    companion object {
        val manager = ObjectManagerFactory().create<AudioAlbum>()
        val propertyManager = manager.toPropertyManager()
    }
}