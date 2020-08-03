package me.steven.minestudio.gui

import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.netty.buffer.Unpooled
import me.steven.minestudio.MineStudio
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.network.PacketByteBuf

class StudioScreen(private val studioGui: StudioGui) : CottonClientScreen(studioGui) {
    override fun onClose() {
        super.onClose()
        val stack = studioGui.buildDiscStack()
        val packet = PacketByteBuf(Unpooled.buffer())
        packet.writeItemStack(stack)
        ClientSidePacketRegistry.INSTANCE.sendToServer(MineStudio.GIVE_DISC_PACKET, packet)
    }

    override fun isPauseScreen(): Boolean = false
}