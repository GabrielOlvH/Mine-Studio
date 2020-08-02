package me.steven.minestudio.utils

import me.steven.minestudio.MineStudio
import me.steven.minestudio.audio.MSNote
import me.steven.minestudio.audio.MSSoundInstance
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

fun identifier(id: String) = Identifier(MineStudio.MOD_ID, id)

fun Identifier.item(item: Item): Identifier {
    Registry.register(Registry.ITEM, this, item)
    return this
}

fun Identifier.block(block: Block): Identifier {
    Registry.register(Registry.BLOCK, this, block)
    return this
}

fun itemSettings(): Item.Settings = Item.Settings().group(MineStudio.MOD_GROUP)

fun createTestDisc(): ItemStack {
    val instance = MSSoundInstance()
    instance.delay = 20
    instance.instruction.forEach { layer ->
        for (i in layer.notes.indices) {
            val sound = SOUNDS.random()
            if (sound != null) {
                val note = MSNote()
                note.soundId = sound.id
                note.note = 12
                note.volume = 1f
                layer.notes[i] = note
            }
        }
    }
    val stack = ItemStack(MineStudio.EMPTY_DISC)
    stack.tag = instance.toTag(CompoundTag())
    return stack
}

private val SOUNDS = arrayOf(SoundEvents.BLOCK_NOTE_BLOCK_BANJO, SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundEvents.BLOCK_NOTE_BLOCK_BELL, SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, SoundEvents.BLOCK_NOTE_BLOCK_BIT, SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL, SoundEvents.BLOCK_NOTE_BLOCK_FLUTE, SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundEvents.BLOCK_NOTE_BLOCK_HARP, SoundEvents.BLOCK_NOTE_BLOCK_PLING)