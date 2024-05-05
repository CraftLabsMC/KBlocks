package me.invalidjoker.kblocks

import me.invalidjoker.kblocks.config.KBlocksConfig
import me.invalidjoker.kblocks.i18n.TranslationManager
import org.bukkit.plugin.java.JavaPlugin

val KBlocksMainInstance: KBlocks get() = PluginInstance

/**
 * The main plugin instance. Less complicated name for internal usage.
 */
@PublishedApi
internal lateinit var PluginInstance: KBlocks
    private set

abstract class KBlocks : JavaPlugin() {
    /**
     * Called when the plugin was loaded
     */
    open fun load() {}

    /**
     * Called when the plugin was enabled
     */
    open fun startup() {}

    /**
     * Called when the plugin gets disabled
     */
    open fun shutdown() {}

    final override fun onLoad() {
        if (::PluginInstance.isInitialized) {
            server.logger.warning("The main instance of KPaper has been modified, even though it has already been set by another plugin!")
        }

        if (KBlocksConfig.Localization.enabled) {
            TranslationManager.loadAll()
            server.logger.info("Loading localization files...")
        }
        PluginInstance = this
        load()
    }

    final override fun onEnable() {
        startup()
    }

    final override fun onDisable() {
        shutdown()
    }
}