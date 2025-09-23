package com.inf1nlty.soulmending.client;

import com.inf1nlty.soulmending.block.tileentity.ITotemTileEntity;
import com.inf1nlty.soulmending.block.tileentity.TileEntityEmptySoulTotem;
import com.inf1nlty.soulmending.block.tileentity.TileEntitySoulTotem;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class TileEntityTotemRenderer extends TileEntitySpecialRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("soulmending:textures/blocks/soul_totem.png");
    private static final ResourceLocation EMPTY_TEXTURE = new ResourceLocation("soulmending:textures/blocks/empty_soul_totem.png");
    private static final ResourceLocation ENCHANT_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");

    public static void registerTotemTESR() {
        TileEntityRenderer.instance.addSpecialRendererForClass(TileEntitySoulTotem.class, new TileEntityTotemRenderer());
        TileEntityRenderer.instance.addSpecialRendererForClass(TileEntityEmptySoulTotem.class, new TileEntityTotemRenderer());
    }

    private static final float[][][] ELEMENT_UVS = new float[][][] {

            // 0. base2 - [u1, v1, u2, v2] for each face (north, east, south, west, up, down)
            {
                    {9f, 4.5f, 6.5f, 4.75f},     // north
                    {9f, 4.5f, 6.5f, 4.75f},     // east
                    {9f, 4.5f, 6.5f, 4.75f},     // south
                    {9f, 4.5f, 6.5f, 4.75f},     // west
                    {2.5f, 2.5f, 0f, 0f},        // up
                    {2.5f, 2.5f, 0f, 5f}         // down
            },
            // 1. base1
            {
                    {8.5f, 5f, 6.5f, 5.25f},     // north
                    {8.5f, 5f, 6.5f, 5.25f},     // east
                    {8.5f, 5f, 6.5f, 5.25f},     // south
                    {8.5f, 5f, 6.5f, 5.25f},     // west
                    {4.5f, 2f, 2.5f, 0f},        // up
                    {4.5f, 0f, 2.5f, 2f}         // down
            },
            // 2. body
            {
                    {4f, 6f, 2.5f, 7.5f},        // north
                    {4f, 6f, 2.5f, 7.5f},        // east
                    {4f, 6f, 2.5f, 7.5f},        // south
                    {4f, 6f, 2.5f, 7.5f},        // west
                    {4f, 6f, 2.5f, 7.5f},        // up
                    {4f, 6f, 2.5f, 7.5f}         // down
            },
            // 3. head
            {
                    {4.5f, 2f, 2.5f, 4f},        // north
                    {4.5f, 4f, 2.5f, 6f},        // east
                    {6.5f, 0f, 4.5f, 2f},        // south
                    {4.5f, 4f, 2.5f, 6f},        // west
                    {6.5f, 4f, 4.5f, 2f},        // up
                    {6.5f, 4f, 4.5f, 6f}         // down
            },
            // 4. nose
            {
                    {7f, 5.25f, 6.5f, 6.25f},    // north
                    {5.25f, 6.75f, 4.75f, 7.75f}, // east
                    {5.75f, 6.75f, 5.25f, 7.75f}, // south
                    {6.25f, 6.75f, 5.75f, 7.75f}, // west
                    {2f, 7.5f, 1.5f, 7f},        // up
                    {2.5f, 7f, 2f, 7.5f}         // down
            },
            // 5. arm base [3, 4, 11] to [13, 6, 13]
            {
                    {6.5f, 6f, 4f, 6.5f},        // north
                    {7.5f, 5.75f, 7f, 6.25f},    // east
                    {9f, 0f, 6.5f, 0.5f},        // south
                    {0.5f, 7.25f, 0f, 7.75f},    // west
                    {9f, 1f, 6.5f, 0.5f},        // up
                    {9f, 1f, 6.5f, 1.5f}         // down
            },
            // 6. left arm lower [3, 5, 10] to [5, 7, 12]
            {
                    {1f, 7.25f, 0.5f, 7.75f},    // north
                    {1.5f, 7.25f, 1f, 7.75f},    // east
                    {7.75f, 3.5f, 7.25f, 4f},    // south
                    {7.75f, 4f, 7.25f, 4.5f},    // west
                    {7.75f, 7f, 7.25f, 6.5f},    // up
                    {7.75f, 7f, 7.25f, 7.5f}     // down
            },
            // 7. right arm lower [11, 5, 10] to [13, 7, 12]
            {
                    {2f, 7.5f, 1.5f, 8f},        // north
                    {8f, 1.5f, 7.5f, 2f},        // east
                    {2.5f, 7.5f, 2f, 8f},        // south
                    {8f, 2f, 7.5f, 2.5f},        // west
                    {3f, 8f, 2.5f, 7.5f},        // up
                    {8f, 2.5f, 7.5f, 3f}         // down
            },
            // 8. left arm upper [3, 6, 9] to [5, 8, 11]
            {
                    {3.5f, 7.5f, 3f, 8f},        // north
                    {8f, 3f, 7.5f, 3.5f},        // east
                    {4f, 7.5f, 3.5f, 8f},        // south
                    {4.5f, 7.5f, 4f, 8f},        // west
                    {8f, 6.25f, 7.5f, 5.75f},    // up
                    {7.75f, 8f, 7.25f, 7.5f}     // down
            },
            // 9. right arm upper [11, 6, 9] to [13, 8, 11]
            {
                    {0.5f, 7.75f, 0f, 8.25f},    // north
                    {1f, 7.75f, 0.5f, 8.25f},    // east
                    {1.5f, 7.75f, 1f, 8.25f},    // south
                    {8.25f, 3.5f, 7.75f, 4f},    // west
                    {8.25f, 4.5f, 7.75f, 4f},    // up
                    {5f, 7.75f, 4.5f, 8.25f}     // down
            },
            // 10. small front protrusion [7, 3, 4] to [9, 4, 5]
            {
                    {8.5f, 2.25f, 8f, 2.5f},     // north
                    {4.75f, 7.5f, 4.5f, 7.75f},  // east
                    {3f, 8f, 2.5f, 8.25f},       // south
                    {7.25f, 7.75f, 7f, 8f},      // west
                    {8.5f, 2.75f, 8f, 2.5f},     // up
                    {8.5f, 2.75f, 8f, 3f}        // down
            },
            // 11. large front protrusion [5, 4, 4] to [11, 5, 5]
            {
                    {8f, 6.25f, 6.5f, 6.5f},     // north
                    {8.25f, 3.25f, 8f, 3.5f},    // east
                    {1.5f, 7f, 0f, 7.25f},       // south
                    {3.75f, 8f, 3.5f, 8.25f},    // west
                    {8.5f, 5.5f, 7f, 5.25f},     // up
                    {8.5f, 5.5f, 7f, 5.75f}      // down
            },
            // 12. wing base [3, 5, 4] to [13, 6, 8]
            {
                    {7.25f, 6.5f, 4.75f, 6.75f}, // north
                    {6f, 7.75f, 5f, 8f},         // east
                    {9f, 4.75f, 6.5f, 5f},       // south
                    {7f, 7.75f, 6f, 8f},         // west
                    {2.5f, 6f, 0f, 5f},          // up
                    {2.5f, 6f, 0f, 7f}           // down
            },
            // 13. left wing 1 [2, 6, 4] to [6, 7, 8]
            {
                    {8.75f, 6.5f, 7.75f, 6.75f}, // north
                    {8.75f, 6.75f, 7.75f, 7f},   // east
                    {8.75f, 7f, 7.75f, 7.25f},   // south
                    {8.75f, 7.25f, 7.75f, 7.5f}, // west
                    {7.5f, 1.5f, 6.5f, 2.5f},    // up
                    {7.5f, 2.5f, 6.5f, 3.5f}     // down
            },
            // 14. right wing 1 [10, 6, 4] to [14, 7, 8]
            {
                    {8.75f, 6.5f, 7.75f, 6.75f}, // north
                    {8.75f, 7.25f, 7.75f, 7.5f}, // east
                    {8.75f, 7f, 7.75f, 7.25f},   // south
                    {8.75f, 6.75f, 7.75f, 7f},   // west
                    {7.5f, 1.5f, 6.5f, 2.5f},    // up
                    {7.5f, 2.5f, 6.5f, 3.5f}     // down
            },
            // 15. right wing 2 [12, 7, 4] to [15, 8, 8]
            {
                    {8.75f, 1.75f, 8f, 2f},      // north
                    {8.75f, 7.5f, 7.75f, 7.75f}, // east
                    {8.75f, 2f, 8f, 2.25f},      // south
                    {8.75f, 7.75f, 7.75f, 8f},   // west
                    {7.25f, 4.5f, 6.5f, 3.5f},   // up
                    {4.75f, 6.5f, 4f, 7.5f}      // down
            },
            // 16. left wing 2 [1, 7, 4] to [4, 8, 8]
            {
                    {8.75f, 1.75f, 8f, 2f},      // north
                    {8.75f, 7.75f, 7.75f, 8f},   // east
                    {8.75f, 2f, 8f, 2.25f},      // south
                    {8.75f, 7.5f, 7.75f, 7.75f}, // west
                    {7.25f, 4.5f, 6.5f, 3.5f},   // up
                    {4.75f, 6.5f, 4f, 7.5f}      // down
            },
            // 17. right wing 3 [14, 8, 4] to [16, 9, 8]
            {
                    {3.5f, 8f, 3f, 8.25f},       // north
                    {2.5f, 8f, 1.5f, 8.25f},     // east
                    {8.5f, 3f, 8f, 3.25f},       // south
                    {9f, 1.5f, 8f, 1.75f},       // west
                    {6.75f, 7.75f, 6.25f, 6.75f}, // up
                    {7.25f, 6.75f, 6.75f, 7.75f}  // down
            },
            // 18. left wing 3 [0, 8, 4] to [2, 9, 8]
            {
                    {3.5f, 8f, 3f, 8.25f},       // north
                    {9f, 1.5f, 8f, 1.75f},       // east
                    {8.5f, 3f, 8f, 3.25f},       // south
                    {2.5f, 8f, 1.5f, 8.25f},     // west
                    {6.25f, 7.75f, 6.75f, 6.75f}, // up
                    {6.75f, 6.75f, 7.25f, 7.75f}  // down
            }
    };

    private static final double[][] ELEMENT_COORDS = new double[][] {
            {3/16.0 - 0.5, 0/16.0, 3/16.0 - 0.5, 13/16.0 - 0.5, 1/16.0, 13/16.0 - 0.5},    // 0. base2
            {4/16.0 - 0.5, 1/16.0, 4/16.0 - 0.5, 12/16.0 - 0.5, 2/16.0, 12/16.0 - 0.5},    // 1. base1
            {5/16.0 - 0.5, 2/16.0, 5/16.0 - 0.5, 11/16.0 - 0.5, 8/16.0, 11/16.0 - 0.5},    // 2. body
            {4/16.0 - 0.5, 8/16.0, 4/16.0 - 0.5, 12/16.0 - 0.5, 16/16.0, 12/16.0 - 0.5},   // 3. head
            {7/16.0 - 0.5, 6/16.0, 12/16.0 - 0.5, 9/16.0 - 0.5, 10/16.0, 14/16.0 - 0.5},   // 4. nose
            {3/16.0 - 0.5, 4/16.0, 11/16.0 - 0.5, 13/16.0 - 0.5, 6/16.0, 13/16.0 - 0.5},   // 5. arm base
            {3/16.0 - 0.5, 5/16.0, 10/16.0 - 0.5, 5/16.0 - 0.5, 7/16.0, 12/16.0 - 0.5},    // 6. left arm lower
            {11/16.0 - 0.5, 5/16.0, 10/16.0 - 0.5, 13/16.0 - 0.5, 7/16.0, 12/16.0 - 0.5},  // 7. right arm lower
            {3/16.0 - 0.5, 6/16.0, 9/16.0 - 0.5, 5/16.0 - 0.5, 8/16.0, 11/16.0 - 0.5},     // 8. left arm upper
            {11/16.0 - 0.5, 6/16.0, 9/16.0 - 0.5, 13/16.0 - 0.5, 8/16.0, 11/16.0 - 0.5},   // 9. right arm upper
            {7/16.0 - 0.5, 3/16.0, 4/16.0 - 0.5, 9/16.0 - 0.5, 4/16.0, 5/16.0 - 0.5},      // 10. small front
            {5/16.0 - 0.5, 4/16.0, 4/16.0 - 0.5, 11/16.0 - 0.5, 5/16.0, 5/16.0 - 0.5},     // 11. large front
            {3/16.0 - 0.5, 5/16.0, 4/16.0 - 0.5, 13/16.0 - 0.5, 6/16.0, 8/16.0 - 0.5},     // 12. wing base
            {2/16.0 - 0.5, 6/16.0, 4/16.0 - 0.5, 6/16.0 - 0.5, 7/16.0, 8/16.0 - 0.5},      // 13. left wing 1
            {10/16.0 - 0.5, 6/16.0, 4/16.0 - 0.5, 14/16.0 - 0.5, 7/16.0, 8/16.0 - 0.5},    // 14. right wing 1
            {12/16.0 - 0.5, 7/16.0, 4/16.0 - 0.5, 15/16.0 - 0.5, 8/16.0, 8/16.0 - 0.5},    // 15. right wing 2
            {1/16.0 - 0.5, 7/16.0, 4/16.0 - 0.5, 4/16.0 - 0.5, 8/16.0, 8/16.0 - 0.5},      // 16. left wing 2
            {14/16.0 - 0.5, 8/16.0, 4/16.0 - 0.5, 16/16.0 - 0.5, 9/16.0, 8/16.0 - 0.5},    // 17. right wing 3
            {0/16.0 - 0.5, 8/16.0, 4/16.0 - 0.5, 2/16.0 - 0.5, 9/16.0, 8/16.0 - 0.5}       // 18. left wing 3
    };

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        if (!(tileEntity instanceof ITotemTileEntity tile)) return;

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);

        if (tileEntity instanceof TileEntitySoulTotem) {
            this.bindTexture(TEXTURE);

        } else if (tileEntity instanceof TileEntityEmptySoulTotem) {
            this.bindTexture(EMPTY_TEXTURE);
        }

        int facing = 2;

        if(tileEntity.hasWorldObj()) {
            facing = tileEntity.getBlockMetadata();
        }

        GL11.glRotatef(180, 0, 1, 0);
        switch(facing) {
            case 2: break;
            case 3: GL11.glRotatef(180, 0, 1, 0); break;
            case 4: GL11.glRotatef(90, 0, 1, 0); break;
            case 5: GL11.glRotatef(-90, 0, 1, 0); break;
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        for (int i = 0; i < ELEMENT_COORDS.length; i++) {
            double[] coords = ELEMENT_COORDS[i];
            float[][] uvs = ELEMENT_UVS[i];
            this.renderBoxWithCorrectUV(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], uvs);
        }

        if (tile.getEnchantTag() != null && tile.getEnchantTag().tagCount() > 0) {
            renderBlockEnchantmentGlint((TileEntity) tile, partialTicks);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();

        this.renderFloatingItem(tile, x, y, z, partialTicks);
    }

    private void renderBoxWithCorrectUV(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float[][] faceUVs) {
        Tessellator tessellator = Tessellator.instance;

        // North (Z-)
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        float[] northUV = faceUVs[0];
        this.addVertexWithUV16(tessellator, minX, minY, minZ, northUV, 0, 1);
        this.addVertexWithUV16(tessellator, minX, maxY, minZ, northUV, 0, 0);
        this.addVertexWithUV16(tessellator, maxX, maxY, minZ, northUV, 1, 0);
        this.addVertexWithUV16(tessellator, maxX, minY, minZ, northUV, 1, 1);
        tessellator.draw();

        // East (X+)
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        float[] eastUV = faceUVs[1];
        this.addVertexWithUV16(tessellator, maxX, minY, minZ, eastUV, 0, 1);
        this.addVertexWithUV16(tessellator, maxX, maxY, minZ, eastUV, 0, 0);
        this.addVertexWithUV16(tessellator, maxX, maxY, maxZ, eastUV, 1, 0);
        this.addVertexWithUV16(tessellator, maxX, minY, maxZ, eastUV, 1, 1);
        tessellator.draw();

        // South (Z+)
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        float[] southUV = faceUVs[2];
        this.addVertexWithUV16(tessellator, maxX, minY, maxZ, southUV, 0, 1);
        this.addVertexWithUV16(tessellator, maxX, maxY, maxZ, southUV, 0, 0);
        this.addVertexWithUV16(tessellator, minX, maxY, maxZ, southUV, 1, 0);
        this.addVertexWithUV16(tessellator, minX, minY, maxZ, southUV, 1, 1);
        tessellator.draw();

        // West (X-)
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        float[] westUV = faceUVs[3];
        this.addVertexWithUV16(tessellator, minX, minY, maxZ, westUV, 0, 1);
        this.addVertexWithUV16(tessellator, minX, maxY, maxZ, westUV, 0, 0);
        this.addVertexWithUV16(tessellator, minX, maxY, minZ, westUV, 1, 0);
        this.addVertexWithUV16(tessellator, minX, minY, minZ, westUV, 1, 1);
        tessellator.draw();

        // Top (Y+)
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        float[] upUV = faceUVs[4];
        this.addVertexWithUV16(tessellator, minX, maxY, minZ, upUV, 0, 0);
        this.addVertexWithUV16(tessellator, minX, maxY, maxZ, upUV, 0, 1);
        this.addVertexWithUV16(tessellator, maxX, maxY, maxZ, upUV, 1, 1);
        this.addVertexWithUV16(tessellator, maxX, maxY, minZ, upUV, 1, 0);
        tessellator.draw();

        // Bottom (Y-)
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        float[] downUV = faceUVs[5];
        this.addVertexWithUV16(tessellator, minX, minY, maxZ, downUV, 0, 0);
        this.addVertexWithUV16(tessellator, minX, minY, minZ, downUV, 0, 1);
        this.addVertexWithUV16(tessellator, maxX, minY, minZ, downUV, 1, 1);
        this.addVertexWithUV16(tessellator, maxX, minY, maxZ, downUV, 1, 0);
        tessellator.draw();
    }

    private void addVertexWithUV16(Tessellator tessellator, double x, double y, double z, float[] uv, int cornerU, int cornerV) {

        float u1 = uv[0] / 16.0f;
        float v1 = uv[1] / 16.0f;
        float u2 = uv[2] / 16.0f;
        float v2 = uv[3] / 16.0f;

        float u = (cornerU == 0) ? u1 : u2;
        float v = (cornerV == 0) ? v1 : v2;

        tessellator.addVertexWithUV(x, y, z, u, v);
    }

    private void renderFloatingItem(ITotemTileEntity tile, double x, double y, double z, float partialTicks) {
        ItemStack stack = tile.getStackInSlot(0);
        if (stack != null) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 1.07, z + 0.5);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            long time = ((TileEntity) tile).getWorldObj().getTotalWorldTime();
            GL11.glRotatef((time + partialTicks) * 6 % 360, 0, 1, 0);
            EntityItem entityItem = new EntityItem(((TileEntity) tile).getWorldObj(), 0, 0, 0, stack.copy());
            entityItem.hoverStart = 0f;
            RenderManager.instance.renderEntityWithPosYaw(entityItem, 0, 0, 0, 0, 0);
            GL11.glPopMatrix();
        }
    }

    private void renderBlockEnchantmentGlint(TileEntity tile, float partialTicks) {
        GL11.glDepthFunc(GL11.GL_EQUAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.bindTexture(ENCHANT_GLINT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(0.6F, 0.2F, 0.8F, 0.36F);

        long worldTime = tile.getWorldObj().getTotalWorldTime();
        float animTime = worldTime + partialTicks;
        float speed = 0.01F;

        for (int k = 0; k < 2; ++k) {
            GL11.glPushMatrix();
            float scale = 0.3333F;
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();

            float offset = animTime * speed + (k == 0 ? 0 : 0.5F);

            GL11.glScalef(scale, scale, scale);
            GL11.glRotatef(k == 0 ? 30F : -30F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(offset, 0.0F, 0.0F);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);

            for (int i = 0; i < ELEMENT_COORDS.length; i++) {
                double[] coords = ELEMENT_COORDS[i];
                float[][] uvs = ELEMENT_UVS[i];
                this.renderBoxWithCorrectUV(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5], uvs);
            }

            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }
}