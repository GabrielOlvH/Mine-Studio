package me.steven.minestudio.utils

import net.minecraft.nbt.CompoundTag

interface NBTSerializable {
    fun toTag(tag: CompoundTag): CompoundTag
    fun fromTag(tag: CompoundTag)
}