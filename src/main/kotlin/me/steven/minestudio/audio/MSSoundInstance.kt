package me.steven.minestudio.audio

import me.steven.minestudio.utils.NBTSerializable
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

class MSSoundInstance : NBTSerializable {

    val instruction: DefaultedList<MSNoteLayer> = DefaultedList.ofSize(100, MSNoteLayer())
    var delay: Int = 0
    private var currentDelay = 0
    private var previousSoundId: Identifier? = null
    private var soundId: Identifier = SoundManager.MISSING_SOUND.identifier
    private var currentIndex: Int = 0
    private var iterator: Iterator<MSPlayable>? = null
    private var current: MSPlayable? = null

    @Environment(EnvType.CLIENT)
    fun play(pos: BlockPos, world: ClientWorld) {
        val sound = Registry.SOUND_EVENT.get(soundId)
        world.playSound(pos, sound, SoundCategory.MUSIC, getVolume(), getPitch(), true)
        previousSoundId = soundId
    }

    fun getId(): Identifier {
        if (iterator?.hasNext() == true) {
            previousSoundId = soundId
            current = iterator?.next()
            soundId = (current as? MSNote)?.soundId ?: SoundManager.MISSING_SOUND.identifier
            return soundId
        }
        iterator = instruction[currentIndex].notes.iterator()
        return getId()
    }

    fun getPitch(): Float {
        if (current?.isEmpty() == true) return 1f
        val n = (current as? MSNote)?.note ?: return 1f
        return 2.0.pow((n - 12) / 12.0).toFloat()
    }

    fun getVolume(): Float = (current as? MSNote)?.volume ?: 1f

    fun isDone(): Boolean = currentIndex >= instruction.size

    fun tick() {
        currentDelay++
        if (currentDelay >= delay) {
            currentDelay = 0
            currentIndex++
            getId()
        }
    }

    fun shouldPlay(): Boolean = previousSoundId != soundId

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