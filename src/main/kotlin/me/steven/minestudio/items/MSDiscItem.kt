package me.steven.minestudio.items

import io.netty.buffer.Unpooled
import me.steven.minestudio.MineStudioClient
import me.steven.minestudio.utils.createTestDisc
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.block.Blocks
import net.minecraft.block.JukeboxBlock
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand

class MSDiscItem(settings: Settings) : Item(settings) {

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val blockPos = context.blockPos
        val blockState = world.getBlockState(blockPos)
        return if (blockState.isOf(Blocks.JUKEBOX) && !blockState.get(JukeboxBlock.HAS_RECORD)) {
            val itemStack = context.stack
            if (!world.isClient) {
                (Blocks.JUKEBOX as JukeboxBlock).setRecord(world, blockPos, blockState, itemStack)
                val packet = PacketByteBuf(Unpooled.buffer())
                packet.writeCompoundTag(itemStack.orCreateTag)
                packet.writeString(if (itemStack.hasCustomName()) itemStack.name.asString() else "MineStudio Disc")
                packet.writeBlockPos(blockPos)
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(context.player, MineStudioClient.PLAY_DISC_PACKET, packet)
                itemStack.decrement(1)
                context.player?.incrementStat(Stats.PLAY_RECORD)
            }
            ActionResult.success(world.isClient)
        } else ActionResult.PASS
    }

    override fun postHit(stack: ItemStack?, target: LivingEntity?, attacker: LivingEntity?): Boolean {
        attacker?.setStackInHand(Hand.MAIN_HAND, createTestDisc())
        return super.postHit(stack, target, attacker)
    }

    companion object {
        fun isEmpty(tag: CompoundTag?): Boolean = tag?.contains("minestudio") == false
    }
}