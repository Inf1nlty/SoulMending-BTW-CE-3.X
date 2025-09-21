package com.inf1nlty.soulmending.client;

import com.inf1nlty.soulmending.block.tileentity.TileEntityISoulTotem;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class TileEntitySoulTotemRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        if (!(te instanceof TileEntityISoulTotem tile)) return;

        ItemStack stack = tile.getStackInSlot(0);

        if (stack != null) {
            GL11.glPushMatrix();

            GL11.glTranslated(x + 0.5, y + 1.07, z + 0.5);

            GL11.glScalef(1.0F, 1.0F, 1.0F);

            long time = tile.getWorldObj().getTotalWorldTime();
            GL11.glRotatef((time + partialTicks) * 6 % 360, 0, 1, 0);

            EntityItem entityItem = new EntityItem(tile.getWorldObj(), 0, 0, 0, stack.copy());
            entityItem.hoverStart = 0f;

            RenderManager.instance.renderEntityWithPosYaw(entityItem, 0, 0, 0, 0, 0);

            GL11.glPopMatrix();
        }
    }
}