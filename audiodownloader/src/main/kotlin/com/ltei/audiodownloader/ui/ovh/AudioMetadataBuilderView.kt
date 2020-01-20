package com.ltei.audiodownloader.ui.ovh

import com.ltei.audiodownloader.model.AudioMetadata
import com.ltei.audiodownloader.ui.UIConstants
import com.ltei.audiodownloader.ui.view.JodaDatePicker
import com.ltei.lteijfxutils.misc.InputBlockableView
import com.ltei.lteijfxutils.misc.ObjectBuilderViewHolder
import com.ltei.lteijfxutils.view.BaseLabel
import com.ltei.lteijfxutils.view.StringListBuilder
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
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
    private val artistsView = StringListBuilder()
    private val albumView = TextField()
    private val releaseDateView = JodaDatePicker()
    private val tagsView = StringListBuilder()

    init {
        spacing = UIConstants.BASE_SPACING

        children.add(BaseLabel("Title :"))
        children.add(titleView)

        children.add(BaseLabel("Artists :"))
        children.add(artistsView.apply {
            VBox.setVgrow(this, Priority.ALWAYS)
            minHeight = 250.0
            prefHeight = 250.0
        })

        children.add(BaseLabel("Album :"))
        children.add(albumView)

        children.add(BaseLabel("Release date :"))
        children.add(releaseDateView.apply {
            maxWidth = Double.MAX_VALUE
        })

        children.add(BaseLabel("Tags :"))
        children.add(tagsView.apply {
            VBox.setVgrow(this, Priority.ALWAYS)
            minHeight = 250.0
            prefHeight = 250.0
        })
    }

    override fun updateViewFromObject() {
        val item = boundObject
        titleView.text = item?.title
        artistsView.boundObject = item?.artists?.toMutableList()
        artistsView.updateViewFromObject()
        albumView.text = item?.album
        releaseDateView.jodaDate = item?.releaseDate
        tagsView.boundObject = item?.tags?.toMutableList()
        tagsView.updateViewFromObject()
    }

    override fun updateObjectFromView() {
        boundObject?.let { item ->
            item.title = titleView.text
            artistsView.updateObjectFromView()
            item.artists = artistsView.boundObject
            item.album = albumView.text
            item.releaseDate = releaseDateView.jodaDate
            tagsView.updateObjectFromView()
            item.tags = tagsView.boundObject
        }
    }

}