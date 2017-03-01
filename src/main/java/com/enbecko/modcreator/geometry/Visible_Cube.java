package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 24.02.2017.
 */
public class Visible_Cube extends Content.CubicContent implements Visible3D {
    private final int dimension;

    public Visible_Cube(Bone parentBone, vec3.IntVec positionInBone, int dimension) {
        super(parentBone, positionInBone, dimension);
        this.dimension = dimension;
    }

    @Override
    public void createGeometry() {
        this.makeCorners(true);
        this.makeCubicEdgesAndFacesAutoUpdate();
    }

    @Override
    public vec3.IntVec getPositionInBoneCoords() {
        return (vec3.IntVec) this.positionInBoneCoords;
    }

    @Override
    public vec3.IntVec getCorner(int pos) {
        return (vec3.IntVec) this.cornersInBoneCoords[pos];
    }
}
