package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.events.ManipulatingEvent;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec4;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static com.enbecko.modcreator.GlobalRenderSetting.RenderOption.OVERLAY_GREEN;

/**
 * Created by Niclas on 07.03.2017.
 */
public class MCBlockProxy extends Gridded_CUBE {
    private final IBlockState blockState;

    public MCBlockProxy(Bone parentBone, vec3.IntVec positonInBoneCoords, IBlockState state) {
        super(parentBone, positonInBoneCoords, 1);
        this.blockState = state;
        if (!BlockStateRenderer.isStateRegistered(this.blockState))
            BlockStateRenderer.registerBlockState(this.blockState);
    }

    @Override
    public void manipulateMe(ManipulatingEvent event, RayTrace3D rayTrace3D) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(VertexBuffer buffer, LocalRenderSetting... localRenderSettings) {
        GL11.glTranslatef(this.positionInBoneCoords.getXF(), this.positionInBoneCoords.getYF(), this.positionInBoneCoords.getZF());
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.color(1, 1, 1, 1);
        List<TexturedQuad> quads = BlockStateRenderer.getQuadsForState(this.blockState);
        for (TexturedQuad quad : quads) {
            quad.draw(buffer, 1);
        }
        for (LocalRenderSetting renderSetting : localRenderSettings) {
            switch (renderSetting.getOption()) {
                case OVERLAY_GREEN:
                    GlStateManager.bindTexture(0);
                    vec4.FloatVec col = OVERLAY_GREEN.getColor();
                    GlStateManager.color(col.getR(), col.getG(), col.getB(), col.getA());
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    for (TexturedQuad quad : quads) {
                        quad.draw(buffer, 1);
                    }
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    break;
            }
        }
        GL11.glTranslatef(-this.positionInBoneCoords.getXF(), -this.positionInBoneCoords.getYF(), -this.positionInBoneCoords.getZF());
    }

    @Override
    public MCBlockProxy createBoundingGeometry() {
        this.makeHexahedralEdgesAndFacesNoUpdate();
        return this;
    }

    public String toString() {
        return "MCBlockProxy: " + this.getGeometryInfo();
    }
}
