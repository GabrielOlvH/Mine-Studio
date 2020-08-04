package me.steven.minestudio.audio

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag

class MSNoteLayer(val notes: Array<MSPlayable> = Array(5) { MSNote.EMPTY }) {

    fun fromTag(tag: CompoundTag) {
        val notesTag = tag.getList("notes", 10)
        notesTag.forEach { note ->
            val index = (note as CompoundTag).getInt("index")
            notes[index] = MSPlayable.fromTag(note)
        }
    }

    fun toTag(tag: CompoundTag): CompoundTag {
        val notesTag = ListTag()
        notes.forEachIndexed { index, note ->
            val noteTag = CompoundTag()
            noteTag.putInt("index", index)
            note.toTag(noteTag)
            notesTag.add(noteTag)
        }
        tag.put("notes", notesTag)
        return tag
    }
}