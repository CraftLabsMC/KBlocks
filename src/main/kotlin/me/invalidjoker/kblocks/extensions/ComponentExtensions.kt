package me.invalidjoker.kblocks.extensions

import me.invalidjoker.kblocks.config.KBlocksConfig
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

val miniMessageSerializer = MiniMessage.miniMessage()

fun String.toComponent(): Component = Component.text(this)

fun Component.plainText(): String = PlainTextComponentSerializer.plainText().serialize(this);

val String.asStyledComponent: TextComponent
    get() = Component.text().append(miniMessageSerializer.deserializeOr(this, Component.empty())!!).build()

fun text(string: String): Component = string.asStyledComponent

fun cmp(text: String, color: TextColor = KBlocksConfig.textColor, bold: Boolean = false, italic: Boolean = false, strikethrough: Boolean = false, underlined: Boolean = false): Component =
    text(text).color(color)
        .decorations(
            mapOf(
                TextDecoration.BOLD to TextDecoration.State.byBoolean(bold),
                TextDecoration.ITALIC to TextDecoration.State.byBoolean(italic),
                TextDecoration.STRIKETHROUGH to TextDecoration.State.byBoolean(strikethrough),
                TextDecoration.UNDERLINED to TextDecoration.State.byBoolean(underlined)
            )
        )


operator fun Component.plus(other: Component): Component = append(other)
