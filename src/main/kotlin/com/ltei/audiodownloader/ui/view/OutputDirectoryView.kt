package com.ltei.audiodownloader.ui.view

import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.RootView
import com.ltei.audiodownloader.ui.misc.stringBinding
import com.ltei.audiodownloader.ui.view.base.BaseButton
import com.ltei.audiodownloader.ui.view.base.BaseLabel
import com.ltei.audiodownloader.ui.res.UIConstants
import com.ltei.audiodownloader.ui.res.UIStylizer
import javafx.event.EventHandler
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import java.awt.Desktop

class OutputDirectoryView : VBox() {

    private val outputDirectoryLabel = BaseLabel()

    private val selectButton = BaseButton(
        "Select",
        onMouseClicked = EventHandler {
            val chooser = DirectoryChooser()
            chooser.initialDirectory = Preferences.instance.outputDirectory.value
            val directory = chooser.showDialog(RootView.instance.stage)
            if (directory != null && directory.isDirectory) {
                Preferences.instance.outputDirectory.value = directory
            }
        }
    )

    private val openButton = BaseButton(
        "Open",
        onMouseClicked = EventHandler {
            Desktop.getDesktop().open(Preferences.instance.outputDirectory.value)
        }
    )

    init {
        UIStylizer.setupCardLayout(this)
        spacing = UIConstants.BASE_SPACING

        children.add(outputDirectoryLabel)

        children.add(HBox().apply {
            spacing = 10.0

            selectButton.maxWidth = Double.MAX_VALUE
            HBox.setHgrow(selectButton, Priority.ALWAYS)

            openButton.maxWidth = Double.MAX_VALUE
            HBox.setHgrow(openButton, Priority.ALWAYS)

            children.add(selectButton)
            children.add(openButton)
        })


        outputDirectoryLabel.textProperty().bind(Preferences.instance.outputDirectory.stringBinding(op = {
            "Output directory : ${it?.absolutePath}"
        }))
    }

    fun setDisabledState(isDisabled: Boolean) {
        selectButton.isDisable = isDisabled
        openButton.isDisable = isDisabled
    }

}