package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.events.RayTraceDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Niclas on 17.03.2017.
 */
public class CustomEntityRenderer extends EntityRenderer {
    public CustomEntityRenderer(Minecraft mcIn, IResourceManager resourceManagerIn) {
        super(mcIn, resourceManagerIn);
    }

    public void getMouseOver(float partialTicks) {
        super.getMouseOver(partialTicks);
        if (RayTraceDispatcher.getTheRayTraceDispatcher().getRayTraceResult() != null) {
            Minecraft.getMinecraft().objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, new Vec3d(0,0,0), EnumFacing.DOWN, Main_BlockHeroes.active_Editor_Block != null ? Main_BlockHeroes.active_Editor_Block.getPos() : new BlockPos(0, 0, 0));
        }
    }

}
