package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by Niclas on 07.03.2017.
 */
public interface IGridded {
    vec3.IntVec getPositionInBoneInt();

    int getXDim();

    int getYDim();

    int getZDim();
}
