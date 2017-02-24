package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.Main_ModCreator;
import com.enbecko.modcreator.linalg.Matrix;
import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 07.01.2017.
 */
public class Bone {
    private CubicContentHolder boneContent = null;
    private vec3.DoubleVec rotPoint_global;
    private vec3.DoubleVec offset;
    private final Matrix.Matrix_NxN transform = Matrix.NxN_FACTORY.makeIdent(4);
    private final Matrix.Matrix_NxN inverseTransform = Matrix.NxN_FACTORY.makeIdent(4);

    public Bone() {

    }

    public void addContent(Content content, Content ... adjacent) {
        if (this.boneContent == null) {
            vec3 pos = content.getPositionInBoneCoords();
            vec3 posInOrder1 = new vec3.IntVec((vec3) new vec3.DoubleVec(pos, false).divToThis(Main_ModCreator.contentCubesPerCube), true);
        } else if ( !this.boneContent.isInside(content.getPositionInBoneCoords())) {

        } else {

        }
    }
}
