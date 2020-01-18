package com.ltei.audiodownloader.ui.view.ovh

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.view.base.InputBlockableView
import com.ltei.audiodownloader.ui.view.base.JodaDatePicker
import com.ltei.audiodownloader.ui.view.base.TextListField
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.layout.VBox

class AudioMetadataBuilderView(override var boundObject: AudioMetadata? = null) : VBox(),
    ObjectBuilderViewHolder<AudioMetadata>, InputBlockableView {

    override var isInputBlocked: Boolean = false
        set(value) {
            field = value
            titleView.isDisable = value
            artistsView.isDisable = value
            albumView.isDisable = value
            releaseDateView.isDisable = value
            tagsView.isDisable = value
        }

    override val rootNode: Node get() = this


    private val titleView = TextField()
    private val artistsView = TextListField()
    private val albumView = TextField()
    private val releaseDateView = JodaDatePicker()
    private val tagsView = TextListField()

    init {
        spacing = UIConstants.BASE_SPACING

        children.add(BaseLabel("Title :"))
        children.add(titleView)

        children.add(BaseLabel("Artists :"))
        children.add(artistsView)

        children.add(BaseLabel("Album :"))
        children.add(albumView)

        children.add(BaseLabel("Release date :"))
        children.add(releaseDateView.apply {
            maxWidth = Double.MAX_VALUE
        })

        children.add(BaseLabel("Tags :"))
        children.add(tagsView)
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