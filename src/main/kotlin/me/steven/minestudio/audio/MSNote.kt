package me.steven.minestudio.audio

import me.steven.minestudio.utils.NBTSerializable
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Identifier

data class MSNote(
    var soundId: Identifier? = null,
    var note: Int = 0,
    var volume: Float = 0f
) : NBTSerializable, MSPlayable {

    override fun fromTag(tag: CompoundTag) {
        soundId = Identifier(tag.getString("0"))
        note = tag.getInt("1")
        volume = tag.getFloat("2")
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        tag.putString("0", soundId.toString())
        tag.putInt("1", note)
        tag.putFloat("2", volume)
        return tag
    }

    companion object {
        val EMPTY = object : MSPlayable {}
    }
}