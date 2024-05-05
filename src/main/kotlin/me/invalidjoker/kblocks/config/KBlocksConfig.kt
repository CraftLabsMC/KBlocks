package me.invalidjoker.kblocks.config

import me.invalidjoker.kblocks.KColors
import net.kyori.adventure.text.format.TextColor
import java.util.Locale

object KBlocksConfig {
    val prefix: String = "<blue>[KBlocks]</blue> "
    val textColor: TextColor = KColors.WHITE
    val highlightColor: TextColor = KColors.YELLOW


    object Messages {
        val noPermission: String = "<red>You do not have permission to do this.</red>"
        val playerOnly: String = "<red>This command can only be executed by a player.</red>"
        val invalidUsage: String = "<red>Invalid usage. Correct usage: %s</red>"
    }

    object Localization {
        val enabled: Boolean = false
        var defaultLocale: Locale = Locale.ENGLISH
    }
}