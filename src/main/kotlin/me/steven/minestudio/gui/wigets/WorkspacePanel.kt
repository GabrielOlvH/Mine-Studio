package me.steven.minestudio.gui.wigets

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import me.steven.minestudio.utils.identifier
import net.minecraft.client.util.math.MatrixStack

class WorkspacePanel : WGridPanel() {
    override fun paint(matrices: MatrixStack?, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        ScreenDrawing.texturedRect(x, y, width, height, BACKGROUND_ID, -1)

        for (child in children) {
            child.paint(matrices, x + child.x, y + child.y, mouseX - child.x, mouseY - child.y)
        }
    }

    override fun canResize(): Boolean = false

    companion object {
        val BACKGROUND_ID = identifier("textures/gui/background.png")
    }
}