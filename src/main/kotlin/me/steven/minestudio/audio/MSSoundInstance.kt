package me.steven.minestudio.audio

import me.steven.minestudio.utils.NBTSerializable
import net.minecraft.client.sound.AbstractSoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.sound.TickableSoundInstance
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import kotlin.math.pow

class MSSoundInstance(
    val instruction: DefaultedList<MSPlayable> = DefaultedList.ofSize(100, MSNote.EMPTY),
    var tempo: Int = 0,
    var current: Int = -1
) : AbstractSoundInstance(null as Identifier?, SoundCategory.MUSIC), TickableSoundInstance, NBTSerializable {

    override fun getId(): Identifier = (instruction[current++] as? MSNote)?.soundId ?: SoundManager.MISSING_SOUND.identifier

    override fun getPitch(): Float {
        val note = instruction[current]
        if (note == MSNote.EMPTY) return super.getPitch()
        val n = (note as MSNote).note
        return 2.0.pow((n - 12) / 12.0).toFloat()
    }

    override fun getVolume(): Float = (instruction[current] as? MSNote)?.volume ?: super.getVolume()

    override fun isRepeatable(): Boolean = true

    override fun getRepeatDelay(): Int = tempo

    override fun isDone(): Boolean = current >= instruction.size

    override fun tick() {}

    override fun toTag(tag: CompoundTag): CompoundTag {
        val instruct = CompoundTag()
        val notes = ListTag()
        instruction.forEachIndexed { index, playable ->
            val noteTag = CompoundTag()
            noteTag.putInt("index", index)
            notes.add(playable.toTag(noteTag))
        }
        instruct.put("notes", notes)
        instruct.putInt("tempo", tempo)
        instruct.putInt("current", current)
        tag.put("minestudio", instruct)
        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        val instruct = tag.getCompound("minestudio")
        val notes = instruct.getList("notes", 10)
        notes.forEach { noteTag ->
            val index = (noteTag as CompoundTag).getInt("index")
            instruction[index] = MSPlayable.fromTag(noteTag)
        }
        tempo = instruct.getInt("tempo")
        current = instruct.getInt("current")
    }
}