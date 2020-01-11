package com.ltei.audiodownloader.ui

import javafx.scene.layout.Region
import tornadofx.asBackground

object UIStylizer {

    fun setupCardLayout(region: Region) {
        region.background = UIColors.GRAY.asBackground(UIConstants.BASE_RADII)
        region.padding = UIConstants.BASE_INSETS
    }

}