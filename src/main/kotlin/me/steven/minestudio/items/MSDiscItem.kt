package me.steven.minestudio.items

import net.minecraft.block.Blocks
import net.minecraft.block.JukeboxBlock
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult

class MSDiscItem(settings: Settings) : Item(settings) {

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val blockPos = context.blockPos
        val blockState = world.getBlockState(blockPos)
        return if (blockState.isOf(Blocks.JUKEBOX) && !blockState.get(JukeboxBlock.HAS_RECORD)) {
            val itemStack = context.stack
            if (!world.isClient) {
                (Blocks.JUKEBOX as JukeboxBlock).setRecord(world, blockPos, blockState, itemStack)
                world.syncWorldEvent(null, 1010, blockPos, -1)
                itemStack.decrement(1)
                context.player?.incrementStat(Stats.PLAY_RECORD)
            }
            ActionResult.success(world.isClient)
        } else ActionResult.PASS
    }

    companion object {
        fun isEmpty(itemStack: ItemStack): Boolean =
            itemStack.item is MSDiscItem && itemStack.tag?.contains("minestudio") == true
    }
}