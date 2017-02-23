package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.Main_ModCreator;
import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.vec3;
import sun.applet.Main;

import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class CubicContentHolder <T extends Content> extends CubicContent implements ContentHolder{
    private List<CubicContentHolder> content;
    private byte order;
    final vec3.IntVec positionInBoneCoords;
    final vec3.ByteVec positionInParentInOrdersOfEdgeLength;
    boolean isMaxOrder;

    public CubicContentHolder(Bone parentBone, vec3.ByteVec positionInParentInOrdersOfEdgeLength, vec3.IntVec positionInBoneCoords, byte order, boolean isMaxOrder) {
        super(parentBone);
        this.order = order;
        this.positionInParentInOrdersOfEdgeLength = (vec3.ByteVec) new vec3.ByteVec(positionInParentInOrdersOfEdgeLength.getX(), positionInParentInOrdersOfEdgeLength.getY(), positionInParentInOrdersOfEdgeLength.getZ()).setChangeable(false);
        this.positionInBoneCoords = (vec3.IntVec) new vec3.IntVec(positionInBoneCoords.getX(), positionInBoneCoords.getY(), positionInBoneCoords.getZ()).setChangeable(false);
        this.isMaxOrder = isMaxOrder;
    }

    public void setMaxOrder(boolean order) {
        this.isMaxOrder = order;
    }

    public byte getOrder() {
        return this.order;
    }

    public vec3.IntVec getPositionInBoneCoords() {
        return this.positionInBoneCoords;
    }

    public vec3.ByteVec getPositionInParentInOrdersOfEdgeLength() {
        return this.positionInParentInOrdersOfEdgeLength;
    }

    @SuppressWarnings("unchecked")
    public List<T> getContent() {
        return (List<T>) this.content;
    }

    @Override
    public void init() {
        int size = (int) Math.pow(Main_ModCreator.contentCubesPerCube, order);
        //FRONT FACE COUNTERCLOCKWISE (CCW)
        this.corners[0] = new vec3.IntVec(this.positionInBoneCoords.getX(), this.positionInBoneCoords.getY(), this.positionInBoneCoords.getZ()).setChangeable(false);
        this.corners[1] = new vec3.IntVec(this.positionInBoneCoords.getX(), this.positionInBoneCoords.getY(), this.positionInBoneCoords.getZ() + size).setChangeable(false);
        this.corners[2] = new vec3.IntVec(this.positionInBoneCoords.getX(), this.positionInBoneCoords.getY() + size, this.positionInBoneCoords.getZ() + size).setChangeable(false);
        this.corners[3] = new vec3.IntVec(this.positionInBoneCoords.getX(), this.positionInBoneCoords.getY() + size, this.positionInBoneCoords.getZ()).setChangeable(false);

        //BACK FACE CLOCKWISE (CW)
        this.corners[4] = new vec3.IntVec(this.positionInBoneCoords.getX() + size, this.positionInBoneCoords.getY(), this.positionInBoneCoords.getZ() + size).setChangeable(false);
        this.corners[5] = new vec3.IntVec(this.positionInBoneCoords.getX() + size, this.positionInBoneCoords.getY(), this.positionInBoneCoords.getZ()).setChangeable(false);
        this.corners[6] = new vec3.IntVec(this.positionInBoneCoords.getX() + size, this.positionInBoneCoords.getY() + size, this.positionInBoneCoords.getZ()).setChangeable(false);
        this.corners[7] = new vec3.IntVec(this.positionInBoneCoords.getX() + size, this.positionInBoneCoords.getY() + size, this.positionInBoneCoords.getZ() + size).setChangeable(false);
        super.makeCubicEdgesAndFaces();
    }
}


