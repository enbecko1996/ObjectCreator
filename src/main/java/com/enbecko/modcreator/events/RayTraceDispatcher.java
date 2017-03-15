package com.enbecko.modcreator.events;

import com.enbecko.modcreator.contentholder.RayTraceResult;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.minecraft.Main_BlockHeroes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * Created by Niclas on 09.03.2017.
 */
public class RayTraceDispatcher implements IKeyListener, IMouseEventListener{
    private final RayTrace3D theRayTrace = new RayTrace3D(new vec3.DoubleVec(), new vec3.DoubleVec(), 100, true);
    private RayTraceResult rayTraceResult;
    private static RayTraceDispatcher theRayTraceDispatcher;

    @Override
    public void onMouseEvent(MouseEvent event) {
        if (Main_BlockHeroes.active_Editor_Block != null) {
            if (Main_BlockHeroes.current_BlockSetMode != null) {
                if (event.getButton() == -1)
                    Main_BlockHeroes.current_BlockSetMode.dispatchMouseMoved(event, this.rayTraceResult, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
                else
                    Main_BlockHeroes.current_BlockSetMode.dispatchMouseClickedReleased(event, this.rayTraceResult, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
            }
        }
    }

    @Override
    public void onKeyEvent(InputEvent.KeyInputEvent event) {
        if (Main_BlockHeroes.current_BlockSetMode != null)
            Main_BlockHeroes.current_BlockSetMode.dispatchKeyEvent(event, this.rayTraceResult, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem());
    }

    public void forceReRayTrace() {
        if (Main_BlockHeroes.active_Editor_Block != null) {
            this.rayTraceResult = Main_BlockHeroes.active_Editor_Block.getRayTraceResult(theRayTrace, Main_BlockHeroes.current_BlockSetMode);
        }
    }

    public void doRayTrace(vec3 eye, vec3 look) {
        if (Main_BlockHeroes.active_Editor_Block != null) {
            this.theRayTrace.update(eye, look);
            /**
             * TODO
             */
            this.rayTraceResult = Main_BlockHeroes.active_Editor_Block.getRayTraceResult(theRayTrace, Main_BlockHeroes.current_BlockSetMode);
        }
    }

    public RayTrace3D getTheRayTrace() {
        return theRayTrace;
    }

    public RayTraceResult getRayTraceResult() {
        return this.rayTraceResult;
    }

    public static RayTraceDispatcher getTheRayTraceDispatcher() {
        if (theRayTraceDispatcher == null)
            theRayTraceDispatcher = new RayTraceDispatcher();
        return theRayTraceDispatcher;
    }
}
