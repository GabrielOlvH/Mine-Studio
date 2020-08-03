package me.steven.minestudio.audio

import me.steven.minestudio.utils.NBTSerializable
import net.minecraft.client.sound.SoundManager
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import kotlin.math.pow

class MSSoundInstance : NBTSerializable {

    val instruction: DefaultedList<MSNoteLayer> = DefaultedList.ofSize(100, MSNoteLayer())
    var delay: Int = 0
    private var currentDelay = 0
    private var played = false
    private var soundId: Identifier = SoundManager.MISSING_SOUND.identifier
    private var currentIndex: Int = 0
    private var iterator: Iterator<MSPlayable>? = null

    fun play(pos: BlockPos, world: ServerWorld) {
        while (iterator?.hasNext() ?: return) {
            val next = iterator!!.next() as? MSNote ?: continue
            val sound = Registry.SOUND_EVENT.get(next.soundId)
            val pitch = 2.0.pow((next.note - 12) / 12.0).toFloat()
            world.playSound(null, pos, sound, SoundCategory.MUSIC, next.volume, pitch)
        }
        played = true
    }
    fun isDone(): Boolean = currentIndex >= instruction.size

    fun tick() {
        currentDelay++
        if (currentDelay >= delay) {
            currentDelay = 0
            val notes = instruction[currentIndex++].notes
            iterator = notes.iterator()
            played = false
        }
    }

    fun shouldPlay(): Boolean = !played

    override fun toTag(tag: CompoundTag): CompoundTag {
        val instruct = CompoundTag()
        val layers = ListTag()
        instruction.forEachIndexed { index, layer ->
            val noteTag = CompoundTag()
            noteTag.putInt("index", index)
            layers.add(layer.toTag(noteTag))
        }
        instruct.put("layers", layers)
        instruct.putInt("tempo", delay)
        //instruct.putInt("current", currentIndex)
        tag.put("minestudio", instruct)
        return tag
    }

    override fun fromTag(tag: CompoundTag) {
        val instruct = tag.getCompound("minestudio")
        val layers = instruct.getList("layers", 10)
        layers.forEach { layerTag ->
            val index = (layerTag as CompoundTag).getInt("index")
            val layer = MSNoteLayer()
            layer.fromTag(layerTag)
            instruction[index] = layer
        }
        delay = instruct.getInt("tempo")
        //currentIndex = instruct.getInt("current")
    }

    override fun toString(): String = "SoundInstance[$soundId]"
}