package me.steven.minestudio.gui.wigets

import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import net.minecraft.client.util.math.MatrixStack

class WorkspacePanel : WGridPanel() {
    override fun paint(matrices: MatrixStack?, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        ScreenDrawing.coloredRect(x, y, width, height, 0xff000000.toInt())

        for (child in children) {
            child.paint(matrices, x + child.x, y + child.y, mouseX - child.x, mouseY - child.y)
        }
    }

    override fun canResize(): Boolean = false
}