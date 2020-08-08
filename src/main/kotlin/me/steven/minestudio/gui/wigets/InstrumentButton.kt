package me.steven.minestudio.gui.wigets

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.icon.Icon
import me.steven.minestudio.gui.Instrument
import me.steven.minestudio.gui.StudioGui
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.AbstractSoundInstance
import net.minecraft.client.sound.SoundInstance
import net.minecraft.sound.SoundCategory
import net.minecraft.text.LiteralText
import net.minecraft.text.StringRenderable

class InstrumentButton(private val instrument: Instrument, private val gui: StudioGui) : WButton() {
    init {
        if (instrument.iconIdentifier == null)
        label = StringRenderable.plain(instrument.toString().toCharArray()[0].toString())
        else
            icon = Icon { matrices, x, y, size ->
                ScreenDrawing.texturedRect(x, y, 32, 32, instrument.iconIdentifier, -1)
            }
    }

    override fun setSize(x: Int, y: Int) {
        this.width = x
        this.height = y
    }

    override fun addTooltip(tooltip: MutableList<StringRenderable>?) {
        tooltip?.add(LiteralText(instrument.toString()))
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        super.onClick(x, y, button)
        val sound = object : AbstractSoundInstance(instrument.sound, SoundCategory.RECORDS) {
            init {
                this.attenuationType = SoundInstance.AttenuationType.NONE
            }
        }
        MinecraftClient.getInstance().soundManager.play(sound)
        val noteButton = NoteButton(instrument.sound, gui.workspacePanel)
        gui.noteButtons.add(noteButton)
        gui.workspacePanel.add(noteButton, 0, 0)
        noteButton.setSize(gui.workspacePanel.width / 25, gui.workspacePanel.height / 5)
    }
}