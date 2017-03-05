package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.Rectangle;
import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec4;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

/**
 * Created by enbec on 03.03.2017.
 */
public class Render_EditorBlock extends TileEntitySpecialRenderer <TE_Editor> {
    GlobalRenderSetting renderPass;

    public Render_EditorBlock() {
        this.renderPass = new GlobalRenderSetting().putRenderMode(GlobalRenderSetting.RenderMode.DEBUG);
    }
    @Override
    public void renderTileEntityAt(TE_Editor entity, double x, double y, double z, float aFloat, int anInt) {
        GlStateManager.pushMatrix();
        GlStateManager.bindTexture(0);
        GlStateManager.translate(x, y, z);
        entity.getBoneAt(0).render(this.renderPass);
        GlStateManager.popMatrix();
    }
}
