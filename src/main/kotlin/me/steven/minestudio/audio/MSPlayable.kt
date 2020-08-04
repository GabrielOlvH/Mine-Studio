package me.steven.minestudio.audio

import net.minecraft.nbt.CompoundTag

interface MSPlayable {
    fun fromTag(tag: CompoundTag) {}

    fun toTag(tag: CompoundTag): CompoundTag = tag

    fun isEmpty() = this == MSNote.EMPTY

    companion object {
        fun fromTag(tag: CompoundTag): MSPlayable {
            return if (tag.contains("soundId")) MSNote().also { it.fromTag(tag) } else MSNote.EMPTY
        }
    }
}