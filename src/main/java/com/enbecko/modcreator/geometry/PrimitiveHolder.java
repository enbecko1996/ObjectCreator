package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.Main_ModCreator;
import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public class PrimitiveHolder extends CubicContentHolder {
    private List<Primitive3D> content = new ArrayList<Primitive3D>();

    public PrimitiveHolder(Bone parentBone, vec3.ByteVec positionInParentInOrdersOfEdgeLength, vec3.IntVec positionInBoneCoords, boolean isMaxOrder) {
        super(parentBone, positionInParentInOrdersOfEdgeLength, positionInBoneCoords, (byte) 1, isMaxOrder);
    }

    @Override
    public List<Primitive3D> getContent() {
        return this.content;
    }
}
