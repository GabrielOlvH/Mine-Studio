package me.steven.minestudio.audio

import me.steven.minestudio.utils.NBTSerializable
import net.minecraft.nbt.CompoundTag

interface MSPlayable : NBTSerializable {
    override fun fromTag(tag: CompoundTag) {}

    override fun toTag(tag: CompoundTag): CompoundTag = tag

    fun isEmpty() = this == MSNote.EMPTY

    companion object {
        fun fromTag(tag: CompoundTag): MSPlayable {
            return if (tag.contains("soundId")) MSNote().also { it.fromTag(tag) } else MSNote.EMPTY
        }
    }
}