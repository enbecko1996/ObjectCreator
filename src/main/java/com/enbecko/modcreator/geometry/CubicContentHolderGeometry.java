package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.Main_ModCreator;
import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 25.02.2017.
 */
public abstract class CubicContentHolderGeometry extends Content.CubicContent {
    private byte order;
    private boolean isMaxOrder;
    private int size;

    protected CubicContentHolderGeometry(Bone parentBone, vec3.IntVec positionInBoneCoords, byte order, boolean isMaxOrder) {
        super(parentBone, positionInBoneCoords, (int) Math.pow(Main_ModCreator.contentCubesPerCube, order));
        this.setCanChangePosition(false);
        this.size = (int) Math.pow(Main_ModCreator.contentCubesPerCube, order);
        this.order = order;
        this.isMaxOrder = isMaxOrder;
    }

    public void setMaxOrder(boolean order) {
        this.isMaxOrder = order;
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
        return (vec3.IntVec) this.cornersInBoneCoords[pos];
    }

    @Override
    public void createGeometry() {
        this.makeCorners(false);
        this.makeCubicEdgesAndFacesNoUpdate();
    }

    @Override
    public void makeCorners(boolean changeable) {
        vec3.IntVec vec = this.getPositionInBoneCoords();
        //FRONT FACE COUNTERCLOCKWISE (CCW)
        this.cornersInBoneCoords[0] = new vec3.IntVec(vec.getX(), vec.getY(), vec.getZ(), false).setChangeable(changeable);
        this.cornersInBoneCoords[1] = new vec3.IntVec(vec.getX(), vec.getY(), vec.getZ() + zSize, false).setChangeable(changeable);
        this.cornersInBoneCoords[2] = new vec3.IntVec(vec.getX(), vec.getY() + ySize, vec.getZ() + zSize, false).setChangeable(changeable);
        this.cornersInBoneCoords[3] = new vec3.IntVec(vec.getX(), vec.getY() + ySize, vec.getZ(), false).setChangeable(changeable);

        //BACK FACE CLOCKWISE (CW)
        this.cornersInBoneCoords[4] = new vec3.IntVec(vec.getX() + xSize, vec.getY(), vec.getZ() + zSize, false).setChangeable(changeable);
        this.cornersInBoneCoords[5] = new vec3.IntVec(vec.getX() + xSize, vec.getY(), vec.getZ(), false).setChangeable(changeable);
        this.cornersInBoneCoords[6] = new vec3.IntVec(vec.getX() + xSize, vec.getY() + ySize, vec.getZ(), false).setChangeable(changeable);
        this.cornersInBoneCoords[7] = new vec3.IntVec(vec.getX() + xSize, vec.getY() + ySize, vec.getZ() + zSize, false).setChangeable(changeable);
    }

    public int getSize() {
        return this.size;
    }
}
