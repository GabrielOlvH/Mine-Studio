package me.steven.minestudio

import me.steven.minestudio.utils.identifier
import me.steven.minestudio.utils.item
import me.steven.minestudio.utils.itemSettings
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object MineStudio : ModInitializer {
    override fun onInitialize() {
        identifier("disc_item").item(EMPTY_DISC)
    }

    val MOD_GROUP: ItemGroup =
        FabricItemGroupBuilder.create(identifier("minestudio_group")).icon { ItemStack(EMPTY_DISC) }.build()
    const val MOD_ID = "minestudio"

    val EMPTY_DISC: Item = Item(itemSettings())
}