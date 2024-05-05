package me.invalidjoker.kblocks.i18n

import me.invalidjoker.kblocks.config.KBlocksConfig
import me.invalidjoker.kblocks.extensions.*
import org.bukkit.configuration.file.YamlConfiguration
import java.util.Locale
import java.nio.file.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock


object TranslationManager {
    private val translations: MutableMap<Locale, Map<String, String>> = mutableMapOf()

    object JarFileSystemProvider {
        private val fileSystems = ConcurrentHashMap<String, FileSystem>()

        fun getFileSystem(jarPath: String): FileSystem {
            return fileSystems.computeIfAbsent(jarPath) { path ->
                FileSystems.newFileSystem(Paths.get(path))
            }
        }
    }

    private val lock: ReadWriteLock = ReentrantReadWriteLock()

    private fun listResourcesInJar(pathInJar: String): List<Path> {
        val jarPath = Paths.get(this::class.java.protectionDomain.codeSource.location.toURI()).toString()
        val fs = JarFileSystemProvider.getFileSystem(jarPath)
        val path = fs.getPath(pathInJar)
        Files.list(path).use { paths ->
            return paths.toList()
        }
    }

    fun loadAll() {
        if (!KBlocksConfig.Localization.enabled) {
            getLogger().info("Localization is disabled.")
            return
        }
        try {
            lock.writeLock().lock()
            if (listResourcesInJar("locale").isEmpty()) {
                getLogger().error("No translations found in jar!")
                return
            }

            val translations = mutableMapOf<Locale, Map<String, String>>()
            listResourcesInJar("locale").forEach { path ->
                val locale = Locale.forLanguageTag(path.fileName.toString())
                val yaml = YamlConfiguration.loadConfiguration(Files.newBufferedReader(path))
                val keys = yaml.getKeys(true)
                val map = mutableMapOf<String, String>()
                keys.forEach { key ->
                    map[key] = yaml.getString(key) ?: ""
                }
                translations[locale] = map
            }
            this.translations.clear()
            this.translations.putAll(translations)
        } catch (e: Exception) {
            getLogger().error("Error while loading translations", e)
        } finally {
            lock.writeLock().unlock()
        }
    }

    val languages: List<Locale>
        get() = translations.keys.toList()

    fun getTranslation(locale: Locale, key: String, placeholders: Map<String, Any?> = emptyMap()): String {
        try {
            lock.readLock().lock()
            val translation = translations[locale] ?: return key
            val value = translation[key] ?: return key
            for ((placeholder, replacement) in placeholders) {
                value.replace("%$placeholder%", replacement.toString())
            }
            return value
        } finally {
            lock.readLock().unlock()
        }
    }

    fun getTranslation(locale: Locale, key: String, vararg placeholders: Pair<String, Any?>): String {
        return getTranslation(locale, key, placeholders.toMap())
    }

    fun containsTranslation(locale: Locale, key: String): Boolean {
        try {
            lock.readLock().lock()
            return translations[locale]?.containsKey(key) ?: false
        } finally {
            lock.readLock().unlock()
        }
    }
}