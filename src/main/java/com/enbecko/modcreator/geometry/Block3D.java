package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 07.01.2017.
 */
public class Block3D {

    public final int dimension;
    public final vec3.Int position;

    public Block3D(vec3.Int position, int dimension) {
        this.dimension = dimension;
        this.position = position;
    }
}
