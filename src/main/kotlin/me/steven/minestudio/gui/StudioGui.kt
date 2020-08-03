package me.steven.minestudio.gui

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import me.steven.minestudio.MineStudio
import me.steven.minestudio.audio.MSNote
import me.steven.minestudio.audio.MSNoteLayer
import me.steven.minestudio.audio.MSSoundInstance
import me.steven.minestudio.gui.wigets.InstrumentButton
import me.steven.minestudio.gui.wigets.NoteButton
import me.steven.minestudio.gui.wigets.WorkspacePanel
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag

class StudioGui : LightweightGuiDescription() {
    val noteButtons = mutableListOf<NoteButton>()
    val workspacePanel: WorkspacePanel = WorkspacePanel()
    init {
        val panel = WGridPanel()
        rootPanel = panel
        rootPanel.setSize(400, 200)

        workspacePanel.setSize(520, 200)

        panel.add(workspacePanel, 1, 2)

        Instrument.values().forEachIndexed { index, instrument ->
            val button = InstrumentButton(instrument, this)
            panel.add(button, index * 2, 0)
        }

        rootPanel.validate(this)
    }

    fun buildDiscStack(): ItemStack {
        val instance = MSSoundInstance()
        for (layerIndex in instance.instruction.indices) {
            val layer = MSNoteLayer()
            for (noteIndex in layer.notes.indices) {
                var note = MSNote.EMPTY
                val button = noteButtons.firstOrNull { it.x / it.width == layerIndex && it.y / it.height == noteIndex }
                if (button != null) {
                    note = MSNote()
                    note.soundId = button.sound.id
                    note.note = button.note
                    note.volume = button.volume
                }
                layer.notes[noteIndex] = note
            }
            instance.instruction[layerIndex] = layer
        }
        val itemStack = ItemStack(MineStudio.EMPTY_DISC)
        itemStack.tag = instance.toTag(CompoundTag())
        return itemStack
    }
}