package me.steven.minestudio.gui.wigets

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WWidget
import me.steven.minestudio.utils.identifier
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.AbstractSoundInstance
import net.minecraft.client.sound.SoundInstance
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.text.LiteralText
import net.minecraft.text.StringRenderable
import net.minecraft.text.TranslatableText
import kotlin.math.pow
import kotlin.math.roundToInt

class NoteButton(val sound: SoundEvent, val workspacePanel: WorkspacePanel) : WButton() {
    var note = 12
    var volume = 1.0f

    override fun setSize(x: Int, y: Int) {
        this.width = x
        this.height = y
    }

    override fun paint(matrices: MatrixStack?, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        val percentage = 1f - (note / 24f)
        val red = ((if (percentage > 0.5) 1 - 2 * (percentage - 0.5) else 1.0) * 255).toInt()
        val green = ((if (percentage > 0.5) 1.0 else 2.0 * percentage) * 255).toInt()
        val rgb = red.shl(16).xor(green.shl(8)).xor(0)
        ScreenDrawing.texturedRect(x + (width / 4), y + (height / 4), 16, 16, NOTE_TEXTURE, rgb)
        if (this.x !in -16..workspacePanel.width || this.y !in -16..workspacePanel.height)
            ScreenDrawing.texturedRect(x - 8, y + 8, 16, 16, TRASH_TEXTURE, rgb)
    }

    override fun onMouseScroll(x: Int, y: Int, amount: Double) {
        if (amount > 0) note++
        else note--
        if (note > 24) note = 0
        else if (note < 0) note = 24
        super.onMouseScroll(x, y, amount)
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        val sound = object : AbstractSoundInstance(sound, SoundCategory.RECORDS) {
            init {
                this.pitch = 2.0.pow((note - 12) / 12.0).toFloat()
                this.volume = this@NoteButton.volume
                this.attenuationType = SoundInstance.AttenuationType.NONE
            }
        }
        MinecraftClient.getInstance().soundManager.play(sound)
    }

    override fun onMouseDrag(x: Int, y: Int, button: Int, deltaX: Double, deltaY: Double) {
        val newX = (this.x + deltaX.toInt())
        val newY = (this.y + deltaY.toInt())
        setLocation(newX, newY)
    }

    override fun onMouseUp(x: Int, y: Int, button: Int): WWidget {
        if (this.x !in -16..workspacePanel.width || this.y !in -16..workspacePanel.height)
            workspacePanel.remove(this)
        else
            alignNote()
        return super.onMouseUp(x, y, button)
    }

    private fun alignNote() {
        val x = (x / width.toDouble()).roundToInt().coerceIn(0, 25)
        val y = (y / height.toDouble()).roundToInt().coerceIn(0, 4)
        setLocation(x * width, y * height)
    }


    override fun addTooltip(tooltip: MutableList<StringRenderable>?) {
        tooltip?.add(TranslatableText("gui.minestudio.sound", TranslatableText(sound.id.path)))
        tooltip?.add(TranslatableText("gui.minestudio.pitch", LiteralText(note.toString())))
        tooltip?.add(TranslatableText("gui.minestudio.volume", LiteralText(volume.toString())))
    }

    companion object {
        val NOTE_TEXTURE = identifier("textures/gui/musical_note.png")
        val TRASH_TEXTURE = identifier("textures/gui/trash.png")
    }
}