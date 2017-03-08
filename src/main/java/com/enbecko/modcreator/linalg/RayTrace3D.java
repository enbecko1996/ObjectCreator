package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;

/**
 * Created by enbec on 21.02.2017.
 */
public class RayTrace3D extends Line3D.Line3DManualUpdate {
    private final boolean isEndless;
    private final vec3.DoubleVec vec;
    private final float limit;
    vec4 tmp = new vec4.DoubleVec();

    public RayTrace3D(vec3 onPoint, vec3 vec1, float limit, boolean isEndless) {
        super(onPoint, (vec3) onPoint.addAndMakeNew(vec1.getPrecision(), vec1, false));
        this.vec = new vec3.DoubleVec(vec1, false);
        this.isEndless = isEndless;
        if(limit > 0)
            this.limit = limit;
        else
            throw new RuntimeException("Can't create Raycast with negative or 0 limit.");
    }

    public RayTrace3D(RayTrace3D other) {
        this(other.getOnPoint(), other.getVec(), other.getLimit(), other.isEndless());
    }

    public RayTrace3D transform(Matrix.Matrix_NxN matrix) {
        double[] vec = this.onPoint.getVecD();
        matrix.multiplyWithVector(tmp, vec[0], vec[1], vec[2], 1);
        this.onPoint.update(tmp, true, false);
        vec = this.endPoint.getVecD();
        matrix.multiplyWithVector(tmp, vec[0], vec[1], vec[2], 1);
        this.endPoint.update(tmp, true, false);
        this.vec.update(this.endPoint, false).subFromThis(this.onPoint);
        return this;
    }

    public vec3 advanceOnVecAndReturnPosition(double advance) {
        return (vec3) new vec3.DoubleVec(this.onPoint, false).addToThis(new vec3.DoubleVec(this.vec, false).mulToThis(advance));
    }

    @Override
    public Line3D updateOnPoint(vec3 onPoint) {
        this.onPoint.update(onPoint, false);
        return this;
    }

    @Override
    public void updateEndPoint(vec3 end) {
        this.endPoint.update(end, false);
        this.vec.update(this.endPoint, false).subFromThis(this.onPoint);
    }

    public void updateVec(vec3.DoubleVec vec) {
        this.vec.update(vec, false);
        this.endPoint.update(this.onPoint.addToThis(vec), false);
        this.onPoint.update(this.endPoint.subFromThis(vec), false);
    }

    @Override
    public RayTrace3D update(vec3 onPoint, @Nonnull vec3 vec) {
        this.onPoint.update(onPoint, false);
        this.vec.update(vec);
        this.endPoint.update(this.onPoint).addToThis(vec);
        return this;
    }

    public vec3 getVec() {
        return this.vec;
    }

    public boolean isEndless() {
        return this.isEndless;
    }

    public float getLimit() {
        return this.limit;
    }

    public String toString() {
        return "RAYTRACE: onpoint = " + this.onPoint + ", vec = " + this.vec;
    }
}
