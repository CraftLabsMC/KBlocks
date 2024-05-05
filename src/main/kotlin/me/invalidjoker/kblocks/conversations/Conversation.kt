package me.invalidjoker.kblocks.conversations

import io.papermc.paper.event.player.AsyncChatEvent
import me.invalidjoker.kblocks.event.listen
import me.invalidjoker.kblocks.event.unregister
import me.invalidjoker.kblocks.extensions.plainText
import me.invalidjoker.kblocks.extensions.tasks.task
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class Conversation(
    private val player: Player,
    maxSeconds: Int,
    initMessage: Component,
    private val callback: (message: String?) -> Unit
) {
    private var seconds = maxSeconds

    init {
        player.closeInventory()
        player.sendMessage(initMessage)
    }

    private val scheduler = task(false, 0, 20) run@{
        if (seconds <= 0) {
            stop(null)
            return@run
        }
        seconds--
    }

    private fun stop(message: String?) {
        scheduler.cancel()
        onChat.unregister()
        callback.invoke(message)
    }

    private val onChat = listen<AsyncChatEvent> {
        if (it.player != player) return@listen
        it.isCancelled = true
        val message = it.message()
        stop(message.plainText())
    }

}