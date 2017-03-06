package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.GlobalRenderSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/**
 * Created by enbec on 03.03.2017.
 */
public class Render_EditorBlock extends TileEntitySpecialRenderer <TE_Editor> {

    public Render_EditorBlock() {
    }

    @Override
    public void renderTileEntityAt(TE_Editor entity, double x, double y, double z, float aFloat, int anInt) {
        GlStateManager.pushMatrix();
        GlStateManager.bindTexture(0);
        GlStateManager.translate(x, y, z);
        Minecraft.getMinecraft().thePlayer.getLookVec();
        entity.getBoneAt(0).render();
        GlStateManager.popMatrix();
    }
}
