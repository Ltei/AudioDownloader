package com.ltei.audiodownloader.model.audiometadata

import com.ltei.ljuutils.datamanager.DefaultListPropertyManager
import com.ltei.ljuutils.datamanager.ManagedByClass
import com.ltei.ljuutils.datamanager.ObjectManagerFactory

class AudioArtist(
    @ManagedByClass(AudioMetadataUtils.NamePropertyManager::class)
    var name: String? = null,

    @ManagedByClass(AudioMetadataUtils.UrlListPropertyManager::class)
    var images: List<String>? = null
) {
    companion object {
        val manager = ObjectManagerFactory().create<AudioArtist>()

        val propertyManager = manager.toPropertyManager().toIdentifiable { a, b ->
            a.name.equals(b.name, ignoreCase = true)
        }

        val propertyListManager = DefaultListPropertyManager(propertyManager)
    }
}