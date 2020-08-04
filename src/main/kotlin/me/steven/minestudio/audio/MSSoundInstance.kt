package me.steven.minestudio.audio

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.world.ClientWorld
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import kotlin.math.pow

class MSSoundInstance {

    val instruction: DefaultedList<MSNoteLayer> = DefaultedList.ofSize(50, MSNoteLayer())
    var delay: Int = 0
    var played = false
    private var currentDelay = 0
    private var soundId: Identifier = SoundManager.MISSING_SOUND.identifier
    private var currentIndex: Int = 0
    private var iterator: Iterator<MSPlayable>? = null

    fun tick() {
        currentDelay++
        if (currentDelay >= delay) {
            currentDelay = 0
            val notes = instruction[currentIndex++].notes
            iterator = notes.iterator()
            played = false
        }
    }

    @Environment(EnvType.CLIENT)
    fun play(pos: BlockPos, world: ClientWorld) {
        while (iterator?.hasNext() ?: return) {
            val next = iterator!!.next() as? MSNote ?: continue
            val sound = Registry.SOUND_EVENT.get(next.soundId)
            val pitch = 2.0.pow((next.note - 12) / 12.0).toFloat()
            world.playSound(pos, sound, SoundCategory.RECORDS, next.volume, pitch, true)
        }
        played = true
    }

    fun isDone(): Boolean = currentIndex >= instruction.size

    fun toTag(tag: CompoundTag): CompoundTag {
        val instruct = CompoundTag()
        val layers = ListTag()
        instruction.forEachIndexed { index, layer ->
            val noteTag = CompoundTag()
            noteTag.putInt("index", index)
            layers.add(layer.toTag(noteTag))
        }
        instruct.put("layers", layers)
        instruct.putInt("tempo", delay)
        tag.put("minestudio", instruct)
        return tag
    }

    fun fromTag(tag: CompoundTag) {
        val instruct = tag.getCompound("minestudio")
        val layers = instruct.getList("layers", 10)
        layers.forEach { layerTag ->
            val index = (layerTag as CompoundTag).getInt("index")
            val layer = MSNoteLayer()
            layer.fromTag(layerTag)
            instruction[index] = layer
        }
        delay = instruct.getInt("tempo")
    }

    override fun toString(): String = "SoundInstance[$soundId]"
}