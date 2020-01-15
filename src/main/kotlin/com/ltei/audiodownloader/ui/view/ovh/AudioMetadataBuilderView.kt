package com.ltei.audiodownloader.ui.view.ovh

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.view.base.JodaDatePicker
import com.ltei.audiodownloader.ui.view.base.TextListField
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.add
import tornadofx.label

class AudioMetadataBuilderView(override var boundObject: AudioMetadata? = null) : VBox(), ObjectBuilderViewHolder<AudioMetadata> {

    override val rootNode: Node get() = this


    private val titleView = TextField()
    private val artistsView = TextListField()
    private val albumView = TextField()
    private val releaseDateView = JodaDatePicker()
    private val tagsView = TextListField()

    init {
        padding = UIConstants.BASE_INSETS

        add(BaseLabel("Title :"))
        add(titleView.apply {
            VBox.setMargin(this, Insets(0.0, 0.0, UIConstants.BASE_PADDING, 0.0))
        })
        add(BaseLabel("Artists :"))
        add(artistsView.apply {
            VBox.setMargin(this, Insets(0.0, 0.0, UIConstants.BASE_PADDING, 0.0))
        })
        add(BaseLabel("Album :"))
        add(albumView.apply {
            VBox.setMargin(this, Insets(0.0, 0.0, UIConstants.BASE_PADDING, 0.0))
        })
        add(BaseLabel("Release date :"))
        add(releaseDateView.apply {
            maxWidth = Double.MAX_VALUE
            VBox.setMargin(this, Insets(0.0, 0.0, UIConstants.BASE_PADDING, 0.0))
        })
        add(BaseLabel("Tags :"))
        add(tagsView.apply {
            VBox.setMargin(this, Insets(0.0, 0.0, UIConstants.BASE_PADDING, 0.0))
        })
    }

    override fun updateViewFromObject() {
        val item = boundObject
        titleView.text = item?.title
        artistsView.textList = item?.artists ?: listOf()
        albumView.text = item?.album
        releaseDateView.jodaDate = item?.releaseDate
        tagsView.textList = item?.tags ?: listOf()
    }

    override fun updateObjectFromView() {
        boundObject?.let { item ->
            item.title = titleView.text
            item.artists = artistsView.textList
            item.album = albumView.text
            item.releaseDate = releaseDateView.jodaDate
            item.tags = tagsView.textList
        }
    }

}