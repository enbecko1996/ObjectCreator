package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.minecraft.Main_BlockHeroes;
import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 25.02.2017.
 */
public abstract class CubicContentHolderGeometry extends Content.CubicContent {
    private byte order;
    private boolean isMaxOrder;
    private int size;

    protected CubicContentHolderGeometry(Bone parentBone, vec3.IntVec positionInBoneCoords, byte order, boolean isMaxOrder) {
        super(parentBone, positionInBoneCoords, (int) Math.pow(Main_BlockHeroes.contentCubesPerCube, order));
        this.setCanChangePosition(false);
        this.size = (int) Math.pow(Main_BlockHeroes.contentCubesPerCube, order);
        this.order = order;
        this.isMaxOrder = isMaxOrder;
    }

    public CubicContentHolderGeometry setMaxOrder(boolean order) {
        this.isMaxOrder = order;
        return this;
    }

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
        this.makeCorners(false);
        this.makeCubicEdgesAndFacesNoUpdate();
        return this;
    }

    @Override
    public void makeCorners(boolean changeable) {
        vec3.IntVec vec = this.getPositionInBoneCoords();
        //FRONT_X FACE COUNTERCLOCKWISE (CCW)
        this.boundingCornersInBoneCoords[0] = new vec3.IntVec(vec.getX(), vec.getY(), vec.getZ(), false).setChangeable(changeable);
        this.boundingCornersInBoneCoords[1] = new vec3.IntVec(vec.getX(), vec.getY(), vec.getZ() + zSize, false).setChangeable(changeable);
        this.boundingCornersInBoneCoords[2] = new vec3.IntVec(vec.getX(), vec.getY() + ySize, vec.getZ() + zSize, false).setChangeable(changeable);
        this.boundingCornersInBoneCoords[3] = new vec3.IntVec(vec.getX(), vec.getY() + ySize, vec.getZ(), false).setChangeable(changeable);

        //BACK_X FACE CLOCKWISE (CW)
        this.boundingCornersInBoneCoords[4] = new vec3.IntVec(vec.getX() + xSize, vec.getY(), vec.getZ() + zSize, false).setChangeable(changeable);
        this.boundingCornersInBoneCoords[5] = new vec3.IntVec(vec.getX() + xSize, vec.getY(), vec.getZ(), false).setChangeable(changeable);
        this.boundingCornersInBoneCoords[6] = new vec3.IntVec(vec.getX() + xSize, vec.getY() + ySize, vec.getZ(), false).setChangeable(changeable);
        this.boundingCornersInBoneCoords[7] = new vec3.IntVec(vec.getX() + xSize, vec.getY() + ySize, vec.getZ() + zSize, false).setChangeable(changeable);
    }

    public int getSize() {
        return this.size;
    }
}
