package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 07.01.2017.
 */
public class Bone {
    private vec3.DoubleVec rotPoint_global;
    private vec3.DoubleVec offset;
    private final float[] transformMat = new float[16], inv_transformMat = new float[16];

    public Bone() {

    }
}
