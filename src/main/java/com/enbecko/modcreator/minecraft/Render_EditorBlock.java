package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.LocalRenderSetting.CrossedByRayTrace;
import com.enbecko.modcreator.contentholder.RayTraceResult;
import com.enbecko.modcreator.events.RayTraceDispatcher;
import com.enbecko.modcreator.linalg.Polygon3D;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;

import static com.enbecko.modcreator.GlobalRenderSetting.RenderOption.OVERLAY_GREEN;
import static java.util.Arrays.asList;

/**
 * Created by enbec on 03.03.2017.
 */
public class Render_EditorBlock extends TileEntitySpecialRenderer<TE_Editor> {
    private RayTraceDispatcher rayTraceDispatcher = RayTraceDispatcher.getTheRayTraceDispatcher();
    private vec3 eye = new vec3.DoubleVec(), look = new vec3.DoubleVec();
    EntityPlayer thePlayer = null;

    public Render_EditorBlock() {
    }

    @Override
    public void renderTileEntityAt(TE_Editor entity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (thePlayer == null)
            thePlayer = Minecraft.getMinecraft().thePlayer;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        VertexBuffer buffer = Tessellator.getInstance().getBuffer();
        if (!entity.isActive()) {
            entity.renderBones(buffer);
        } else {
            if (this.eye.updateAndGetDelta(thePlayer.getPositionEyes(partialTicks), false) != 0) {
                this.look.update(thePlayer.getLook(partialTicks), false);
                rayTraceDispatcher.doRayTrace(this.eye, this.look);
            } else if (this.look.updateAndGetDelta(thePlayer.getLook(partialTicks), false) != 0) {
                this.eye.update(thePlayer.getPositionEyes(partialTicks), false);
                rayTraceDispatcher.doRayTrace(this.eye, this.look);
            }
            if (rayTraceDispatcher.getRayTraceResult() != null) {
                entity.renderBonesWithExceptions(buffer, asList(rayTraceDispatcher.getRayTraceResult().getResult()));
                rayTraceDispatcher.getRayTraceResult().getResult().render(buffer, new CrossedByRayTrace(rayTraceDispatcher.getTheRayTrace(), rayTraceDispatcher.getRayTraceResult(), Main_BlockHeroes.current_BlockSetMode, OVERLAY_GREEN));
                //this.renderOverlays(buffer, new CrossedByRayTrace(rayTraceDispatcher.getTheRayTrace(), rayTraceDispatcher.getRayTraceResult(), Main_BlockHeroes.current_BlockSetMode, OVERLAY_GREEN));
            } else {
                entity.renderBones(buffer);
            }
        }
        GlStateManager.popMatrix();
    }

    public void renderOverlays(VertexBuffer buffer, LocalRenderSetting... localRenderSettings) {
        for (LocalRenderSetting localRenderSetting : localRenderSettings) {
            switch (localRenderSetting.getType()) {
                case CROSSED_BY_RAYTRACE:
                    RayTraceResult result;
                    if ((result = ((CrossedByRayTrace) localRenderSetting).getResult()) != null) {
                        Polygon3D crossed = result.getFace().getFace();
                        result.getResult().render(buffer, localRenderSetting);
                    }
                    break;
            }
        }
    }

    public boolean isGlobalRenderer(TE_Editor te) {
        return true;
    }
}
