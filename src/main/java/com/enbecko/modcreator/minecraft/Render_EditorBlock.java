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
    private Rectangle.Colored rectangle;
    GlobalRenderSetting renderPass;

    public Render_EditorBlock() {
        Face3D face = new Face3D.FaceAutoUpdateOnVecChange(new vec3.FloatVec(0,0,0),
                new vec3.FloatVec(0,0,1), new vec3.FloatVec(0,1,1), new vec3.FloatVec(0,1,0));
        this.rectangle = new Rectangle.Colored(face, new vec4.FloatVec(1, 0.5F, 0, 1), true);
        this.renderPass = new GlobalRenderSetting().putRenderMode(GlobalRenderSetting.RenderMode.DEBUG);
    }
    @Override
    public void renderTileEntityAt(TE_Editor entity, double x, double y, double z, float aFloat, int anInt) {
        super.renderTileEntityAt(entity, x, y, z, aFloat, anInt);
        GlStateManager.pushMatrix();
        GlStateManager.bindTexture(0);
        GlStateManager.translate(x, y, z);
        entity.getBoneAt(0).render(this.renderPass);
        GlStateManager.popMatrix();
    }
}
