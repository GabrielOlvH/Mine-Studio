package me.steven.minestudio

import me.steven.minestudio.blocks.StudioBlock
import me.steven.minestudio.items.MSDiscItem
import me.steven.minestudio.utils.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object MineStudio : ModInitializer {
    override fun onInitialize() {
        identifier("disc_item").item(EMPTY_DISC)
        identifier("studio").block(STUDIO_BLOCK).item(STUDIO_BLOCK_ITEM)

        ServerSidePacketRegistry.INSTANCE.register(GIVE_DISC_PACKET) { ctx, packet ->
            val stack = packet.readItemStack()
            ctx.taskQueue.execute {
                ctx.player.inventory.insertStack(stack)
            }
        }
    }

    val MOD_GROUP: ItemGroup =
        FabricItemGroupBuilder.create(identifier("minestudio_group")).icon { ItemStack(EMPTY_DISC) }.build()
    const val MOD_ID = "minestudio"

    val PLAY_DISC_PACKET = identifier("play_disc_packet")
    val GIVE_DISC_PACKET = identifier("give_disc_packet")

    val EMPTY_DISC: Item = MSDiscItem(itemSettings())
    val STUDIO_BLOCK: Block = StudioBlock(blockSettings(Material.PISTON))
    val STUDIO_BLOCK_ITEM: BlockItem = BlockItem(STUDIO_BLOCK, itemSettings())
}