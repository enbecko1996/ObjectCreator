package com.enbecko.modcreator.events.BlockSetModes;

import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.contentholder.RayTraceResult;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.minecraft.IGriddable;
import com.enbecko.modcreator.minecraft.ItemContent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;

import java.util.List;

import static com.enbecko.modcreator.events.BlockSetModes.BlockSetEnum.SINGLE_GRIDDED;

/**
 * Created by Niclas on 08.03.2017.
 */
public class SINGLE_GRIDDED_MODE extends BlockSetMode {
    SINGLE_GRIDDED_MODE() {
        super(BlockSetModes.SINGLE_GRIDDED);
    }

    @Override
    public BlockSetEnum getEnum() {
        return SINGLE_GRIDDED;
    }

    @Override
    public void clicked(BlockSetModeEvent event, RayTraceResult result, ItemStack toSet) {

    }

    @Override
    public void released(BlockSetModeEvent event, RayTraceResult result, ItemStack toSet) {
        if (toSet != null && toSet.getItem() instanceof IGriddable) {
            if (result != null) {
                switch (event) {
                    case MOUSE_RIGHT:
                        vec3 inverseRayTrace = (vec3) new vec3.DoubleVec(result.getTheRayTrace().getVec()).invert();
                        vec3 posOnFace = result.getFace().getCrossPosOnFace();
                        int nX = (int) Math.floor(posOnFace.getXD()), nY = (int) Math.floor(posOnFace.getYD()), nZ = (int) Math.floor(posOnFace.getZD()),
                                pX = (int) Math.ceil(posOnFace.getXD()), pY = (int) Math.ceil(posOnFace.getYD()), pZ = (int) Math.ceil(posOnFace.getZD());
                        double smallestR = Double.POSITIVE_INFINITY;
                        double tmp;
                        vec3.IntVec posInBone = new vec3.IntVec(nX, nY, nZ);
                        if ((tmp = ((nX - posOnFace.getXD()) / inverseRayTrace.getXD())) >= 0 && tmp < smallestR) {
                            smallestR = tmp;
                            posInBone.setX(nX - 1);
                        } else if ((tmp = ((pX - posOnFace.getXD()) / inverseRayTrace.getXD())) >= 0 && tmp < smallestR) {
                            smallestR = tmp;
                            posInBone.setX(pX);
                        }
                        if ((tmp = ((nY - posOnFace.getYD()) / inverseRayTrace.getYD())) >= 0 && tmp < smallestR) {
                            smallestR = tmp;
                            posInBone.setX(nX);
                            posInBone.setY(nY - 1);
                        } else if ((tmp = ((pY - posOnFace.getYD()) / inverseRayTrace.getYD())) >= 0 && tmp < smallestR) {
                            smallestR = tmp;
                            posInBone.setX(nX);
                            posInBone.setY(pY);
                        }
                        if ((tmp = ((nZ - posOnFace.getZD()) / inverseRayTrace.getZD())) >= 0 && tmp < smallestR) {
                            posInBone.setX(nX);
                            posInBone.setY(nY);
                            posInBone.setZ(nZ - 1);
                        } else if ((tmp = ((pZ - posOnFace.getZD()) / inverseRayTrace.getZD())) >= 0 && tmp < smallestR) {
                            posInBone.setX(nX);
                            posInBone.setY(nY);
                            posInBone.setZ(pZ);
                        }

                        if (toSet.getItem() instanceof ItemContent) {
                            ItemContent item = (ItemContent) toSet.getItem();
                            if (item.canBeSetHere(posInBone, this)) {
                                Content out = item.createNewContentAndReturn(result.getTheBone(), posInBone, new Cub);
                            }
                        }
                }
            } else {

            }
        }
    }

    @Override
    public void dispatchMouseMoved(MouseEvent event, RayTraceResult result, ItemStack toSet) {
        if (result != null) {

        } else {

        }
    }

    @Override
    public void tickDowns(List<BlockSetModeEvent> downs) {

    }
}