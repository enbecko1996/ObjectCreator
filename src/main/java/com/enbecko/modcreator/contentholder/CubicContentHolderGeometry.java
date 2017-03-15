package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.events.ManipulatingEvent;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec_n;
import com.enbecko.modcreator.minecraft.Main_BlockHeroes;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 25.02.2017.
 */
public abstract class CubicContentHolderGeometry extends Content.CubicContent {
    private byte order;
    private boolean isMaxOrder;
    private int size;

    protected CubicContentHolderGeometry(Bone parentBone, vec3.IntVec positionInBoneCoords, byte order, boolean isMaxOrder) {
        super(parentBone, positionInBoneCoords, (int) Math.pow(Main_BlockHeroes.contentCubesPerCube, order), vec_n.vecPrec.INT);
        this.setCanChangePosition(false);
        this.size = (int) Math.pow(Main_BlockHeroes.contentCubesPerCube, order);
        this.order = order;
        this.isMaxOrder = isMaxOrder;
    }

    public CubicContentHolderGeometry setMaxOrder(boolean order) {
        this.isMaxOrder = order;
        return this;
    }

    public abstract Content getRayTraceResult(RayTrace3D rayTrace3D);

    public byte getOrder() {
        return this.order;
    }

    @Override
    public vec3.IntVec getPositionInBoneCoords() {
        return (vec3.IntVec) this.positionInBoneCoords;
    }

    @Override
    public vec3.IntVec getCorner(int pos) {
        return (vec3.IntVec) this.boundingCornersInBoneCoords[pos];
    }

    @Override
    public CubicContentHolderGeometry createBoundingGeometry() {
        this.makeHexahedralEdgesAndFacesNoUpdate();
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void manipulateMe(ManipulatingEvent event, RayTrace3D rayTrace3D) {

    }

    /**
     * @Override public void makeCorners(boolean changeable) {
     * vec3.IntVec vec = this.getPositionInBoneCoords();
     * //FRONT_X FACE COUNTERCLOCKWISE (CCW)
     * this.boundingCornersInBoneCoords[0] = new vec3.IntVec(vec.getX(), vec.getY(), vec.getZ(), false).setChangeable(changeable);
     * this.boundingCornersInBoneCoords[1] = new vec3.IntVec(vec.getX(), vec.getY(), vec.getZ() + zSize, false).setChangeable(changeable);
     * this.boundingCornersInBoneCoords[2] = new vec3.IntVec(vec.getX(), vec.getY() + ySize, vec.getZ() + zSize, false).setChangeable(changeable);
     * this.boundingCornersInBoneCoords[3] = new vec3.IntVec(vec.getX(), vec.getY() + ySize, vec.getZ(), false).setChangeable(changeable);
     * <p>
     * //BACK_X FACE CLOCKWISE (CW)
     * this.boundingCornersInBoneCoords[4] = new vec3.IntVec(vec.getX() + xSize, vec.getY(), vec.getZ() + zSize, false).setChangeable(changeable);
     * this.boundingCornersInBoneCoords[5] = new vec3.IntVec(vec.getX() + xSize, vec.getY(), vec.getZ(), false).setChangeable(changeable);
     * this.boundingCornersInBoneCoords[6] = new vec3.IntVec(vec.getX() + xSize, vec.getY() + ySize, vec.getZ(), false).setChangeable(changeable);
     * this.boundingCornersInBoneCoords[7] = new vec3.IntVec(vec.getX() + xSize, vec.getY() + ySize, vec.getZ() + zSize, false).setChangeable(changeable);
     * }
     */

    public int getSize() {
        return this.size;
    }

    public abstract void renderContentWithExceptions(VertexBuffer buffer, @Nullable List<Content> exceptions, LocalRenderSetting ... localRenderSettings);
}
