package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.vec3;

/**
 * Created by enbec on 21.02.2017.
 */
public abstract class CubicContent extends Content {
    protected CubicContent(Bone parentBone, vec3.IntVec positonInBoneCoords) {
        super(parentBone, positonInBoneCoords, 8);
    }

    @Deprecated
    public void makeCubicEdgesAndFaces() {
        //FRONT FACE FROM LOW_LEFT CCW TO LOW_LEFT
        this.edgesInBoneCoords[0] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[0], this.cornersInBoneCoords[1]);
        this.edgesInBoneCoords[1] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[1], this.cornersInBoneCoords[2]);
        this.edgesInBoneCoords[2] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[2], this.cornersInBoneCoords[3]);
        this.edgesInBoneCoords[3] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[3], this.cornersInBoneCoords[0]);

        //BACK FACE FROM LOW_LEFT CW TO LOW_LEFT
        this.edgesInBoneCoords[4] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[4], this.cornersInBoneCoords[5]);
        this.edgesInBoneCoords[5] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[5], this.cornersInBoneCoords[6]);
        this.edgesInBoneCoords[6] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[6], this.cornersInBoneCoords[7]);
        this.edgesInBoneCoords[7] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[7], this.cornersInBoneCoords[4]);

        //EDGE FROM FRONT TO BACK BEGINNING IN FRONT LOW LEFT
        //GOING CCW BACK TO LOW LEFT
        this.edgesInBoneCoords[8] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[0], this.cornersInBoneCoords[5]);
        this.edgesInBoneCoords[9] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[1], this.cornersInBoneCoords[4]);
        this.edgesInBoneCoords[10] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[2], this.cornersInBoneCoords[7]);
        this.edgesInBoneCoords[11] = new Line3D.Line3DNoUpdate(this.cornersInBoneCoords[3], this.cornersInBoneCoords[6]);

        //FRONT, RIGHT, BACK, LEFT, TOP, BOTTOM
        this.facesInBoneCoords[0] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[0], this.cornersInBoneCoords[1], this.cornersInBoneCoords[2], this.cornersInBoneCoords[3]);
        this.facesInBoneCoords[1] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[1], this.cornersInBoneCoords[4], this.cornersInBoneCoords[7], this.cornersInBoneCoords[2]);
        this.facesInBoneCoords[2] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[4], this.cornersInBoneCoords[5], this.cornersInBoneCoords[6], this.cornersInBoneCoords[7]);
        this.facesInBoneCoords[3] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[5], this.cornersInBoneCoords[0], this.cornersInBoneCoords[3], this.cornersInBoneCoords[6]);
        this.facesInBoneCoords[4] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[3], this.cornersInBoneCoords[2], this.cornersInBoneCoords[7], this.cornersInBoneCoords[6]);
        this.facesInBoneCoords[5] = new Face3D.FaceNoUpdate(this.cornersInBoneCoords[5], this.cornersInBoneCoords[4], this.cornersInBoneCoords[1], this.cornersInBoneCoords[0]);
    }
}
