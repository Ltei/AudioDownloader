package com.ltei.audiodownloader.ui.view

import com.ltei.audiodownloader.model.Preferences
import com.ltei.audiodownloader.ui.Application
import com.ltei.audiodownloader.ui.UIConstants
import com.ltei.audiodownloader.ui.UIStylizer
import com.ltei.lteijfxutils.misc.InputBlockableView
import com.ltei.lteijfxutils.utils.stringBinding
import com.ltei.lteijfxutils.view.BaseButton
import com.ltei.lteijfxutils.view.BaseLabel
import javafx.event.EventHandler
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.DirectoryChooser
import java.awt.Desktop

class OutputDirectoryView : VBox(), InputBlockableView {

    override var isInputBlocked: Boolean = false
        set(value) {
            field = value
            selectButton.isDisable = value
            openButton.isDisable = value
        }

    private val outputDirectoryLabel = BaseLabel()

    private val selectButton = BaseButton(
        "Select",
        onMouseClicked = EventHandler {
            val chooser = DirectoryChooser()
            chooser.initialDirectory = Preferences.instance.outputDirectory.value
            val directory = chooser.showDialog(Application.stage)
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

}