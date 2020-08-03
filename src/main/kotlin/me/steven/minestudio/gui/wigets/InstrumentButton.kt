package me.steven.minestudio.gui.wigets

import io.github.cottonmc.cotton.gui.widget.WButton
import me.steven.minestudio.gui.Instrument
import me.steven.minestudio.gui.StudioGui
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.text.LiteralText
import net.minecraft.text.StringRenderable

class InstrumentButton(private val instrument: Instrument, private val gui: StudioGui) : WButton() {
    init {
        label = StringRenderable.plain(instrument.toString().toCharArray()[0].toString())
    }

    override fun addTooltip(tooltip: MutableList<StringRenderable>?) {
        tooltip?.add(LiteralText(instrument.toString()))
    }

    override fun onClick(x: Int, y: Int, button: Int) {
        super.onClick(x, y, button)
        MinecraftClient.getInstance().soundManager.play(PositionedSoundInstance.music(instrument.sound))
        val noteButton = NoteButton(instrument.sound, gui.workspacePanel)
        gui.noteButtons.add(noteButton)
        gui.workspacePanel.add(noteButton, 0, 0)
        noteButton.setSize(gui.workspacePanel.width / 25, gui.workspacePanel.height / 5)
    }
}