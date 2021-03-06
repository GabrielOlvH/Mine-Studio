package me.steven.minestudio.gui

import me.steven.minestudio.utils.identifier
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents.*
import net.minecraft.util.Identifier

enum class Instrument(val sound: SoundEvent, val iconIdentifier: Identifier? = null) {
    BANJO(BLOCK_NOTE_BLOCK_BANJO, identifier("textures/instruments/banjo.png")),
    BASS(BLOCK_NOTE_BLOCK_BASS),
    BELL(BLOCK_NOTE_BLOCK_BELL),
    BASEDRUM(BLOCK_NOTE_BLOCK_BASEDRUM),
    BIT(BLOCK_NOTE_BLOCK_BIT),
    COW_BELL(BLOCK_NOTE_BLOCK_COW_BELL),
    FLUTE(BLOCK_NOTE_BLOCK_FLUTE),
    DIDGERIDOO(BLOCK_NOTE_BLOCK_DIDGERIDOO),
    HARP(BLOCK_NOTE_BLOCK_HARP),
    PLING(BLOCK_NOTE_BLOCK_PLING),
    IRON_XYLOPHONE(BLOCK_NOTE_BLOCK_IRON_XYLOPHONE),
    XYLOPHONE(BLOCK_NOTE_BLOCK_XYLOPHONE),
    HAT(BLOCK_NOTE_BLOCK_HAT),
    SNARE(BLOCK_NOTE_BLOCK_SNARE),
    GUITAR(BLOCK_NOTE_BLOCK_GUITAR),
    CHIME(BLOCK_NOTE_BLOCK_CHIME)
}