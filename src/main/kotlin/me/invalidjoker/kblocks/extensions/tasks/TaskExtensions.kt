package me.invalidjoker.kblocks.extensions.tasks

import me.invalidjoker.kblocks.PluginInstance
import org.bukkit.Bukkit

fun sync(runnable: () -> Unit) = Bukkit.getScheduler().runTask(PluginInstance, runnable)

fun async(runnable: () -> Unit) = Bukkit.getScheduler().runTaskAsynchronously(PluginInstance, runnable)

fun task(
    sync: Boolean = true,
    delay: Long = 0,
    period: Long? = null,
    runnable: () -> Unit
) = if (sync) {
    if (period != null) {
        Bukkit.getScheduler().runTaskTimer(PluginInstance, runnable, delay, period)
    } else {
        Bukkit.getScheduler().runTaskLater(PluginInstance, runnable, delay)
    }
} else {
    if (period != null) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(PluginInstance, runnable, delay, period)
    } else {
        Bukkit.getScheduler().runTaskLaterAsynchronously(PluginInstance, runnable, delay)
    }
}