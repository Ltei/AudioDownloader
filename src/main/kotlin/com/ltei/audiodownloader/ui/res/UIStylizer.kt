package com.ltei.audiodownloader.ui.res

import javafx.scene.layout.Region
import tornadofx.asBackground

object UIStylizer {

    fun setupCardLayout(region: Region) {
        region.background = UIColors.PRIMARY_DARK.asBackground(UIConstants.BASE_RADII)
        region.padding = UIConstants.BASE_INSETS
    }

}