package me.invalidjoker.kblocks.extensions

import me.invalidjoker.kblocks.config.KBlocksConfig
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun Player.sendPrefixed(message: String) {
    this.sendMessage(text(KBlocksConfig.prefix) + cmp(message))
}

fun CommandSender.sendMessageBlock(vararg lines: String) {
    lines.forEach { sendMessage(text(it)) }
}
fun CommandSender.sendEmtpyLine() = sendMessage(text(" "))

fun Player.sendEmptyLine() = sendPrefixed(" ")

fun Player.sendMessageBlock(vararg lines: String) {
    lines.forEach { sendPrefixed(it) }
}

fun CommandSender.sendPrefixed(message: String) {
    sendMessage(text(KBlocksConfig.prefix) + cmp(message))
}