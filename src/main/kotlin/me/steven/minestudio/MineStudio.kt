package me.steven.minestudio

import me.steven.minestudio.audio.MSSoundInstance
import me.steven.minestudio.items.MSDiscItem
import me.steven.minestudio.utils.identifier
import me.steven.minestudio.utils.item
import me.steven.minestudio.utils.itemSettings
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

object MineStudio : ModInitializer {
    override fun onInitialize() {
        identifier("disc_item").item(EMPTY_DISC)
        ServerTickEvents.END_WORLD_TICK.register(ServerTickEvents.EndWorldTick { world ->
            val iterator = PLAYING_AUDIOS.entries.iterator()
            iterator.forEachRemaining { entry ->
                val instance = entry.value
                val pos = entry.key
                instance.tick()
                if (instance.shouldPlay()) instance.play(pos, world)
                if (instance.isDone()) iterator.remove()
            }
        })
    }

    val MOD_GROUP: ItemGroup =
        FabricItemGroupBuilder.create(identifier("minestudio_group")).icon { ItemStack(EMPTY_DISC) }.build()
    const val MOD_ID = "minestudio"

    val EMPTY_DISC: Item = MSDiscItem(itemSettings())

    val PLAYING_AUDIOS = mutableMapOf<BlockPos, MSSoundInstance>()
}