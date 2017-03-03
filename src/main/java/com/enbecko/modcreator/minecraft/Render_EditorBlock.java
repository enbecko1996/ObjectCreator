package com.enbecko.modcreator.minecraft;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by enbec on 03.03.2017.
 */
public class Render_EditorBlock extends TileEntitySpecialRenderer <TE_Editor> {
    @Override
    public void renderTileEntityAt(TE_Editor entity, double x, double y, double z, float aFloat, int anInt) {
        super.renderTileEntityAt(entity, x, y, z, aFloat, anInt);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        GlStateManager.popMatrix();
    }

    /**
     * VERTEXBUFFER!!!!!!
     */
    @SideOnly(Side.CLIENT)
    private void compileDisplayList(float p_compileDisplayList_1_) {

        /**
        this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, 4864);
        VertexBuffer vertexbuffer = Tessellator.getInstance().getBuffer();

        for(int i = 0; i < this.cubeList.size(); ++i) {
            ((ModelBox)this.cubeList.get(i)).render(vertexbuffer, p_compileDisplayList_1_);
        }

        GlStateManager.glEndList();
        this.compiled = true;
         */
    }
}
