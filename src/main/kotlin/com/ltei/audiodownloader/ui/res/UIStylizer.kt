package com.ltei.audiodownloader.ui.res

import com.ltei.audiodownloader.ui.misc.asBackground
import javafx.scene.layout.Region

object UIStylizer {

    fun setupCardLayout(region: Region) {
        region.background = UIColors.PRIMARY_DARK.asBackground(UIConstants.BASE_RADII)
        region.padding = UIConstants.BASE_INSETS
    }

}