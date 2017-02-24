package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 24.02.2017.
 */
public class Visible_Cube extends Primitive3D {
    private final int dimension;

    public Visible_Cube(Bone parentBone, vec3.IntVec positionInBone, int dimension) {
        super(parentBone, positionInBone, 8);
        this.dimension = dimension;
        this.init();
    }

    @Override
    public void init() {
        int size = this.dimension;
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

    public vec3.IntVec getPositionInBoneCoords() {
        return (vec3.IntVec) this.positionInBoneCoords;
    }

    public vec3.IntVec getCorner(int pos) {
        return (vec3.IntVec) this.cornersInBoneCoords[pos];
    }

    public boolean isColliding(Content content) {
        for (int k = 0; k < content.getCornerCount(); k++) {
            vec3 act = content.getCorner(k);
            if ((act.getYD() <= this.getCorner(3).getY() && act.getYD() >= this.getCorner(0).getY() && act.getXD() <= this.getCorner(4).getX() && act.getXD() >= this.getCorner(0).getX() && act.getZD() <= this.getCorner(1).getZ() && act.getZD() >= this.getCorner(0).getZ()))
                return true;
        }
        return false;
    }

    public boolean isFullInside(Content content) {
        for (int k = 0; k < content.getCornerCount(); k++) {
            vec3 act = content.getCorner(k);
            if (!(act.getYD() <= this.getCorner(3).getY() && act.getYD() >= this.getCorner(0).getY() && act.getXD() <= this.getCorner(4).getX() && act.getXD() >= this.getCorner(0).getX() && act.getZD() <= this.getCorner(1).getZ() && act.getZD() >= this.getCorner(0).getZ()))
                return false;
        }
        return true;
    }

    @Override
    public boolean isInside(vec3 vec) {
        if (!(vec.getYD() <= this.getCorner(3).getY() && vec.getYD() >= this.getCorner(0).getY() && vec.getXD() <= this.getCorner(4).getX() && vec.getXD() >= this.getCorner(0).getX() && vec.getZD() <= this.getCorner(1).getZ() && vec.getZD() >= this.getCorner(0).getZ()))
            return true;
        return false;
    }
}
