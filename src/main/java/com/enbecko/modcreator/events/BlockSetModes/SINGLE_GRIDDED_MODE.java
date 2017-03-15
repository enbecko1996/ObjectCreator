package com.enbecko.modcreator.events.BlockSetModes;

import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.Log.LogEnums;
import com.enbecko.modcreator.Visible.MCBlockProxy;
import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.contentholder.RayTraceResult;
import com.enbecko.modcreator.events.RayTraceDispatcher;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.minecraft.ContentOption;
import com.enbecko.modcreator.minecraft.IGriddable;
import com.enbecko.modcreator.minecraft.ItemContent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

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
    public void releasedMouse(MouseEvent mouseEvent, BlockSetModeEvent event, RayTraceResult result, ItemStack toSet) {
        Item item;
        if (result != null) {
            switch (event) {
                case MOUSE_RIGHT:
                    if (toSet != null && (((item = toSet.getItem()) instanceof ItemContent && item instanceof IGriddable) || item instanceof ItemBlock)) {
                        Log.d(LogEnums.BLOCKSETTING, "HI :)");
                        vec3 inverseRayTrace = (vec3) new vec3.DoubleVec(result.getTheRayTrace().getVec()).invert();
                        vec3 posOnFace = result.getFace().getCrossPosOnFace();
                        int nX = (int) Math.floor(posOnFace.getXD()), nY = (int) Math.floor(posOnFace.getYD()), nZ = (int) Math.floor(posOnFace.getZD()),
                                pX = (int) Math.ceil(posOnFace.getXD()), pY = (int) Math.ceil(posOnFace.getYD()), pZ = (int) Math.ceil(posOnFace.getZD());
                        double smallestR = Double.POSITIVE_INFINITY;
                        double tmp;
                        vec3.IntVec posInBone = new vec3.IntVec(nX, nY, nZ);
                        if (inverseRayTrace.getXD() < 0 && (tmp = ((nX - posOnFace.getXD()) / inverseRayTrace.getXD())) >= 0 && tmp < smallestR) {
                            smallestR = tmp;
                            posInBone.setX(nX - 1);
                        } else if (inverseRayTrace.getXD() > 0 && (tmp = ((pX - posOnFace.getXD()) / inverseRayTrace.getXD())) >= 0 && tmp < smallestR) {
                            smallestR = tmp;
                            posInBone.setX(pX);
                        }
                        if (inverseRayTrace.getYD() < 0 && (tmp = ((nY - posOnFace.getYD()) / inverseRayTrace.getYD())) >= 0 && tmp < smallestR) {
                            smallestR = tmp;
                            posInBone.setX(nX);
                            posInBone.setY(nY - 1);
                        } else if (inverseRayTrace.getYD() > 0 && (tmp = ((pY - posOnFace.getYD()) / inverseRayTrace.getYD())) >= 0 && tmp < smallestR) {
                            smallestR = tmp;
                            posInBone.setX(nX);
                            posInBone.setY(pY);
                        }
                        if (inverseRayTrace.getZD() < 0 && (tmp = ((nZ - posOnFace.getZD()) / inverseRayTrace.getZD())) >= 0 && tmp < smallestR) {
                            posInBone.setX(nX);
                            posInBone.setY(nY);
                            posInBone.setZ(nZ - 1);
                        } else if (inverseRayTrace.getZD() > 0 && (tmp = ((pZ - posOnFace.getZD()) / inverseRayTrace.getZD())) >= 0 && tmp < smallestR) {
                            posInBone.setX(nX);
                            posInBone.setY(nY);
                            posInBone.setZ(pZ);
                        }
                        Log.d(LogEnums.BLOCKSETTING, posOnFace + " " + inverseRayTrace + " " + posInBone + " " + nX + " " + nY + " " + nZ);
                        this.dispatchBuildItemStack(toSet, result, posInBone);
                        //mouseEvent.setCanceled(true);
                    }
                    break;
                case MOUSE_LEFT:
                    this.dispatchRemoveContent(result.getTheBone(), result.getResult());
                    //mouseEvent.setCanceled(true);
                    break;
            }
        } else {

        }
    }

    @Override
    public void clickedMouse(MouseEvent mouseEvent, BlockSetModeEvent event, RayTraceResult result, ItemStack toSet) {

    }

    @Override
    public void clickedKey(InputEvent.KeyInputEvent keyEvent, BlockSetModeEvent event, RayTraceResult result, ItemStack toSet) {

    }

    @Override
    public void releasedKey(InputEvent.KeyInputEvent keyEvent, BlockSetModeEvent event, RayTraceResult result, ItemStack toSet) {

    }

    protected void dispatchBuildItemStack(ItemStack toSet, RayTraceResult result, vec3 posInBone) {
        Item item = toSet.getItem();
        Log.d(LogEnums.BLOCKSETTING, item);
        if (item instanceof ItemContent) {
            ItemContent itemContent = (ItemContent) toSet.getItem();
            if (itemContent.canBeSetHere(posInBone, this)) {
                result.getTheBone().addContent(itemContent.createNewContentAndReturn(result.getTheBone(), posInBone, ContentOption.newContentOptionFromNBT(toSet.getTagCompound())).createBoundingGeometry());
                RayTraceDispatcher.getTheRayTraceDispatcher().forceReRayTrace();
            }
        } else if (item instanceof ItemBlock) {
            ItemBlock itemBlock = (ItemBlock) toSet.getItem();
            Block block = itemBlock.getBlock();
            if (block == Blocks.ACACIA_DOOR) {

            } else {
                result.getTheBone().addContent(new MCBlockProxy(result.getTheBone(), new vec3.IntVec(posInBone), block.getDefaultState()).createBoundingGeometry());
            }
        }
    }

    protected void dispatchRemoveContent(Bone bone, Content content) {
        bone.removeContent(content);
        RayTraceDispatcher.getTheRayTraceDispatcher().forceReRayTrace();
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
