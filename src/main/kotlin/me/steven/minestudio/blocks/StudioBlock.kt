package me.steven.minestudio.blocks

import me.steven.minestudio.gui.StudioGui
import me.steven.minestudio.gui.StudioScreen
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class StudioBlock(settings: Settings) : Block(settings) {
    override fun onUse(state: BlockState?, world: World?, pos: BlockPos?, player: PlayerEntity?, hand: Hand?, hit: BlockHitResult?): ActionResult {
        if (world?.isClient == true) {
            MinecraftClient.getInstance().openScreen(StudioScreen(StudioGui()))
            return ActionResult.SUCCESS
        }
        return super.onUse(state, world, pos, player, hand, hit)
    }
}