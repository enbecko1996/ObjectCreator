package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.Rectangle;
import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec4;
import net.minecraft.client.Minecraft;
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
    private Rectangle.Colored rectangle;

    public Render_EditorBlock() {
        Face3D face = new Face3D.FaceAutoUpdateOnVecChange(new vec3.FloatVec(0,0,0),
                new vec3.FloatVec(0,0,1), new vec3.FloatVec(0,1,1), new vec3.FloatVec(0,1,0));
        this.rectangle = new Rectangle.Colored(face, new vec4.FloatVec(1, 0, 0, 1), true);
    }
    @Override
    public void renderTileEntityAt(TE_Editor entity, double x, double y, double z, float aFloat, int anInt) {
        super.renderTileEntityAt(entity, x, y, z, aFloat, anInt);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.rectangle.draw(Tessellator.getInstance().getBuffer(), 1 / 16F);
        GlStateManager.popMatrix();
    }
}
