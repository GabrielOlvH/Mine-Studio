package me.steven.minestudio.gui.wigets

import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvent
import net.minecraft.text.LiteralText
import net.minecraft.text.StringRenderable
import net.minecraft.text.TranslatableText
import kotlin.math.roundToInt

class NoteButton(val sound: SoundEvent) : WButton() {
    var note = 12
    var volume = 1.0f

    override fun onClick(x: Int, y: Int, button: Int) {
        MinecraftClient.getInstance().soundManager.play(PositionedSoundInstance.music(sound))
    }

    override fun onMouseDrag(x: Int, y: Int, button: Int, deltaX: Double, deltaY: Double) {
        setLocation(this.x + deltaX.toInt(), this.y + deltaY.toInt())
    }

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        alignNote(this)
        return super.onMouseUp(x, y, button)
    }

    override fun addTooltip(tooltip: MutableList<StringRenderable>?) {
        tooltip?.add(TranslatableText("gui.minestudio.sound", TranslatableText(sound.id.path)))
        tooltip?.add(TranslatableText("gui.minestudio.pitch", LiteralText(note.toString())))
        tooltip?.add(TranslatableText("gui.minestudio.volume", LiteralText(volume.toString())))
    }

    private fun alignNote(noteButton: NoteButton) {
        val x = (noteButton.x / width.toDouble()).roundToInt().coerceIn(0, 9)
        val y = (noteButton.y / height.toDouble()).roundToInt().coerceIn(0, 4)
        noteButton.setLocation(x * 16, y * 16)
    }
}