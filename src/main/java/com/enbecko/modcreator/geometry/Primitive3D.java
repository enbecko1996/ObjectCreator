package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 07.01.2017.
 */
public abstract class Primitive3D extends Content {
    protected Primitive3D(Bone parentBone, vec3.IntVec positionInBone, int size) {
        super(parentBone, positionInBone, size);
    }

    public void init() {

    }
}
