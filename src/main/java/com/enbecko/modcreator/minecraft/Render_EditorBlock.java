package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.LocalRenderSetting.CrossedByRayTrace;
import com.enbecko.modcreator.contentholder.RayTraceResult;
import com.enbecko.modcreator.events.EventDispatcher;
import com.enbecko.modcreator.events.IKeyListener;
import com.enbecko.modcreator.events.IMouseEventListener;
import com.enbecko.modcreator.linalg.Polygon3D;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import static com.enbecko.modcreator.GlobalRenderSetting.RenderOption.OVERLAY_GREEN;

/**
 * Created by enbec on 03.03.2017.
 */
public class Render_EditorBlock extends TileEntitySpecialRenderer <TE_Editor> implements IKeyListener, IMouseEventListener{
    private final vec3.DoubleVec eye = new vec3.DoubleVec(), look = new vec3.DoubleVec();
    private final RayTrace3D theRayTrace = new RayTrace3D(eye, look, 100, true);
    private RayTraceResult rayTraceResult;

    public Render_EditorBlock() {
        EventDispatcher.getTheEventDispatcher().addMouseListener(this);
        EventDispatcher.getTheEventDispatcher().addKeyListener(this);
    }

    @Override
    public void renderTileEntityAt(TE_Editor entity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.bindTexture(0);
        GlStateManager.translate(x, y, z);
        entity.getBoneAt(0).render();
        if (entity.isActive()) {
            EntityPlayer thePlayer = Minecraft.getMinecraft().thePlayer;
            this.theRayTrace.update(eye.update(thePlayer.getPositionEyes(partialTicks), false), look.update(thePlayer.getLook(partialTicks), false));
            this.rayTraceResult = entity.getBoneAt(0).getRayTraceResult(theRayTrace, Main_BlockHeroes.current_BlockSetMode);
            if (this.rayTraceResult != null)
                this.renderOverlays(new CrossedByRayTrace(theRayTrace, this.rayTraceResult, Main_BlockHeroes.current_BlockSetMode, OVERLAY_GREEN));
        }
        GlStateManager.popMatrix();
    }

    public void renderOverlays(LocalRenderSetting ... localRenderSettings) {
        for (LocalRenderSetting localRenderSetting : localRenderSettings) {
            switch (localRenderSetting.getType()) {
                case CROSSED_BY_RAYTRACE:
                    Polygon3D crossed = ((CrossedByRayTrace)localRenderSetting).getResult().getFace().getFace();
                    if (crossed != null && crossed.getRenderer() != null)
                        crossed.getRenderer().draw(Tessellator.getInstance().getBuffer(), 1, localRenderSetting);
                    break;
            }
        }
    }

    @Override
    public void onMouseEvent(MouseEvent event) {
        if (Main_BlockHeroes.current_BlockSetMode != null) {
            if(event.getButton() == -1)
                Main_BlockHeroes.current_BlockSetMode.dispatchMouseMoved(event, this.rayTraceResult, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
            else
                Main_BlockHeroes.current_BlockSetMode.dispatchMouseClickedReleased(event, this.rayTraceResult, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
        }
    }

    @Override
    public void onKeyEvent(InputEvent.KeyInputEvent event) {
        if (Main_BlockHeroes.current_BlockSetMode != null)
            Main_BlockHeroes.current_BlockSetMode.dispatchKeyEvent(event, this.rayTraceResult, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
    }
}
