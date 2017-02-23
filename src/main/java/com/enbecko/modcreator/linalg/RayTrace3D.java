package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;

/**
 * Created by enbec on 21.02.2017.
 */
public class RayTrace3D extends Line3D.Line3DManualUpdate {
    private final boolean isEndless;
    private final vec3.DoubleVec vec;
    private final float limit;

    public RayTrace3D(vec3.DoubleVec onPoint, vec3.DoubleVec vec1, float limit, boolean isEndless) {
        super(onPoint, (vec3.DoubleVec) onPoint.addAndMakeNew(vec_n.vecPrec.DOUBLE, vec1));
        this.vec = new vec3.DoubleVec(vec1);
        this.isEndless = isEndless;
        if(limit > 0)
            this.limit = limit;
        else
            throw new RuntimeException("Can't create Raycast with negative or 0 limit.");
    }

    public vec3 advanceOnVecAndReturnPosition(double advance) {
        return (vec3) new vec3.DoubleVec(this.onPoint).addToThis(new vec3.DoubleVec(this.vec).mulToThis(advance));
    }

    @Override
    public Line3D updateOnPoint(vec3 onPoint) {
        this.onPoint.update(onPoint);
        return this;
    }

    @Override
    public void updateEndPoint(vec3 end) {
        this.endPoint.update(end);
        this.vec.update(this.endPoint).subFromThis(this.onPoint);
    }

    public void updateVec(vec3.DoubleVec vec) {
        this.vec.update(vec);
        this.endPoint.update(this.onPoint.addToThis(vec));
        this.onPoint.update(this.endPoint.subFromThis(vec));
    }

    @Override
    public Line3D update(vec3 onPoint, @Nonnull vec3 end) {
        this.onPoint.update(onPoint);
        this.endPoint.update(end);
        this.vec.update(this.endPoint).subFromThis(this.onPoint);
        return this;
    }

    public vec3.DoubleVec getVec() {
        return this.vec;
    }

    public boolean isEndless() {
        return this.isEndless;
    }

    public float getLimit() {
        return this.limit;
    }
}
