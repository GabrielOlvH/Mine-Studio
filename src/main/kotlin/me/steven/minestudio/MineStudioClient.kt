package me.steven.minestudio

import me.steven.minestudio.audio.MSSoundInstance
import me.steven.minestudio.items.MSDiscItem.Companion.isEmpty
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.text.LiteralText
import net.minecraft.util.math.BlockPos

object MineStudioClient : ClientModInitializer {
    override fun onInitializeClient() {
        ClientTickEvents.END_WORLD_TICK.register(ClientTickEvents.EndWorldTick { world ->
            val iterator = PLAYING_AUDIOS.entries.iterator()
            iterator.forEachRemaining { entry ->
                val instance = entry.value
                val pos = entry.key
                instance.tick()
                if (!instance.played) instance.play(pos, world)
                if (instance.isDone()) iterator.remove()
            }
        })

        ClientSidePacketRegistry.INSTANCE.register(MineStudio.PLAY_DISC_PACKET) { ctx, packet ->
            val tag = packet.readCompoundTag() ?: return@register
            val name = packet.readString()
            val pos = packet.readBlockPos()
            ctx.taskQueue.execute {
                if (!isEmpty(tag)) {
                    val instance = MSSoundInstance()
                    instance.fromTag(tag)
                    PLAYING_AUDIOS[pos] = instance
                    val display = LiteralText(name)
                    MinecraftClient.getInstance().inGameHud.setRecordPlayingOverlay(display)
                }
            }
        }
    }

    val PLAYING_AUDIOS = mutableMapOf<BlockPos, MSSoundInstance>()
}