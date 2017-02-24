package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.Main_ModCreator;
import com.enbecko.modcreator.linalg.vec3;

import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class CubicContentHolder <T extends Content> extends CubicContent implements ContentHolder{
    private List<CubicContentHolder> content;
    private byte order;
    final vec3.ByteVec positionInParentInOrdersOfEdgeLength;
    boolean isMaxOrder;

    public CubicContentHolder(Bone parentBone, vec3.ByteVec positionInParentInOrdersOfEdgeLength, vec3.IntVec positionInBoneCoords, byte order, boolean isMaxOrder) {
        super(parentBone, positionInBoneCoords);
        this.order = order;
        this.positionInParentInOrdersOfEdgeLength = (vec3.ByteVec) new vec3.ByteVec(positionInParentInOrdersOfEdgeLength.getX(), positionInParentInOrdersOfEdgeLength.getY(), positionInParentInOrdersOfEdgeLength.getZ(), false).setChangeable(false);
        this.isMaxOrder = isMaxOrder;
    }

    public void setMaxOrder(boolean order) {
        this.isMaxOrder = order;
    }

    public byte getOrder() {
        return this.order;
    }

    public vec3.IntVec getPositionInBoneCoords() {
        return (vec3.IntVec) this.positionInBoneCoords;
    }

    public vec3.ByteVec getPositionInParentInOrdersOfEdgeLength() {
        return this.positionInParentInOrdersOfEdgeLength;
    }

    @SuppressWarnings("unchecked")
    public List<CubicContentHolder> getContent() {
        return this.content;
    }

    @Override
    public void init() {
        int size = this.getSize();
        vec3.IntVec pos = this.getPositionInBoneCoords();
        //FRONT FACE COUNTERCLOCKWISE (CCW)
        this.cornersInBoneCoords[0] = new vec3.IntVec(pos.getX(), pos.getY(), pos.getZ(), false).setChangeable(false);
        this.cornersInBoneCoords[1] = new vec3.IntVec(pos.getX(), pos.getY(), pos.getZ() + size, false).setChangeable(false);
        this.cornersInBoneCoords[2] = new vec3.IntVec(pos.getX(), pos.getY() + size, pos.getZ() + size, false).setChangeable(false);
        this.cornersInBoneCoords[3] = new vec3.IntVec(pos.getX(), pos.getY() + size, pos.getZ(), false).setChangeable(false);

        //BACK FACE CLOCKWISE (CW)
        this.cornersInBoneCoords[4] = new vec3.IntVec(pos.getX() + size, pos.getY(), pos.getZ() + size, false).setChangeable(false);
        this.cornersInBoneCoords[5] = new vec3.IntVec(pos.getX() + size, pos.getY(), pos.getZ(), false).setChangeable(false);
        this.cornersInBoneCoords[6] = new vec3.IntVec(pos.getX() + size, pos.getY() + size, pos.getZ(), false).setChangeable(false);
        this.cornersInBoneCoords[7] = new vec3.IntVec(pos.getX() + size, pos.getY() + size, pos.getZ() + size, false).setChangeable(false);
    }

    public vec3.IntVec[] getCornersInBoneCoords() {
        return (vec3.IntVec[]) this.cornersInBoneCoords;
    }

    public boolean isColliding(Content content) {
        vec3[] corners = content.getCornersInBoneCoords();
        vec3.IntVec[] myCorns = this.getCornersInBoneCoords();
        for (int k = 0; k < corners.length; k++) {
            vec3 act = corners[k];
            if (act.getYD() <= myCorns[3].getY() && act.getYD() >= myCorns[0].getY() && act.getXD() <= myCorns[4].getX() && act.getXD() >= myCorns[0].getX() && act.getZD() <= myCorns[1].getZ() && act.getZD() >= myCorns[0].getZ())
                return true;
        }
        return false;
    }

    public boolean isFullInside(Content content) {
        vec3[] corners = content.getCornersInBoneCoords();
        vec3.IntVec[] myCorns = this.getCornersInBoneCoords();
        for (int k = 0; k < corners.length; k++) {
            vec3 act = corners[k];
            if (!(act.getYD() <= myCorns[3].getY() && act.getYD() >= myCorns[0].getY() && act.getXD() <= myCorns[4].getX() && act.getXD() >= myCorns[0].getX() && act.getZD() <= myCorns[1].getZ() && act.getZD() >= myCorns[0].getZ()))
                return false;
        }
        return true;
    }

    @Override
    public boolean isInside(vec3 vec) {
        vec3.IntVec[] myCorns = this.getCornersInBoneCoords();
        if (vec.getYD() <= myCorns[3].getY() && vec.getYD() >= myCorns[0].getY() && vec.getXD() <= myCorns[4].getX() && vec.getXD() >= myCorns[0].getX() && vec.getZD() <= myCorns[1].getZ() && vec.getZD() >= myCorns[0].getZ())
            return true;
        return false;
    }

    public int getSize() {
        return (int) Math.pow(Main_ModCreator.contentCubesPerCube, order);
    }
}


