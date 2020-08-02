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
        soundId = Identifier(tag.getString("soundId"))
        note = tag.getInt("note")
        volume = tag.getFloat("volume")
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        tag.putString("soundId", soundId.toString())
        tag.putInt("note", note)
        tag.putFloat("volume", volume)
        return tag
    }

    companion object {
        val EMPTY = object : MSPlayable {}
    }
}