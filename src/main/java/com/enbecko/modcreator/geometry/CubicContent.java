package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.Parallelogram3D;
import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;

/**
 * Created by enbec on 21.02.2017.
 */
public abstract class CubicContent implements Content {
    @Nonnull
    private final Bone parentBone;

    protected vec3[] corners = new vec3[8];
    protected Line3D[] edges = new Line3D[12];
    protected Face3D[] faces = new Face3D[6];

    protected CubicContent(Bone parentBone) {
        this.parentBone = parentBone;
    }

    public abstract void init();

    public void makeCubicEdgesAndFaces() {
        //FRONT FACE FROM LOW_LEFT CCW TO LOW_LEFT
        this.edges[0] = new Line3D.Line3DNoUpdate(this.corners[0], this.corners[1]);
        this.edges[1] = new Line3D.Line3DNoUpdate(this.corners[1], this.corners[2]);
        this.edges[2] = new Line3D.Line3DNoUpdate(this.corners[2], this.corners[3]);
        this.edges[3] = new Line3D.Line3DNoUpdate(this.corners[3], this.corners[0]);

        //BACK FACE FROM LOW_LEFT CW TO LOW_LEFT
        this.edges[4] = new Line3D.Line3DNoUpdate(this.corners[4], this.corners[5]);
        this.edges[5] = new Line3D.Line3DNoUpdate(this.corners[5], this.corners[6]);
        this.edges[6] = new Line3D.Line3DNoUpdate(this.corners[6], this.corners[7]);
        this.edges[7] = new Line3D.Line3DNoUpdate(this.corners[7], this.corners[4]);

        //EDGE FROM FRONT TO BACK BEGINNING IN FRONT LOW LEFT
        //GOING CCW BACK TO LOW LEFT
        this.edges[8] = new Line3D.Line3DNoUpdate(this.corners[0], this.corners[5]);
        this.edges[9] = new Line3D.Line3DNoUpdate(this.corners[1], this.corners[4]);
        this.edges[10] = new Line3D.Line3DNoUpdate(this.corners[2], this.corners[7]);
        this.edges[11] = new Line3D.Line3DNoUpdate(this.corners[3], this.corners[6]);

        //FRONT, RIGHT, BACK, LEFT, TOP, BOTTOM
        this.faces[0] = new Face3D.FaceNoUpdate(this.corners[0], this.corners[1], this.corners[2], this.corners[3]);
        this.faces[0] = new Face3D.FaceNoUpdate(this.corners[1], this.corners[4], this.corners[7], this.corners[2]);
        this.faces[0] = new Face3D.FaceNoUpdate(this.corners[4], this.corners[5], this.corners[6], this.corners[7]);
        this.faces[0] = new Face3D.FaceNoUpdate(this.corners[5], this.corners[0], this.corners[3], this.corners[6]);
        this.faces[0] = new Face3D.FaceNoUpdate(this.corners[3], this.corners[2], this.corners[7], this.corners[6]);
        this.faces[0] = new Face3D.FaceNoUpdate(this.corners[5], this.corners[4], this.corners[1], this.corners[0]);
    }

    @Nonnull
    public Bone getParentBone() {
        return this.parentBone;
    }
}
