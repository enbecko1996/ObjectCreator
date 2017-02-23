package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;

/**
 * Created by enbec on 21.02.2017.
 */
public class RayTrace3D extends Line3D.Line3DManualUpdate {
    private final boolean isEndless;
    private final vec3.DoubleVec vec;

    public RayTrace3D(vec3.DoubleVec onPoint, vec3.DoubleVec vec1, boolean isEndless) {
        super(onPoint, (vec3.DoubleVec) onPoint.addAndMakeNew(vec_n.vecPrec.DOUBLE, vec1));
        this.vec = new vec3.DoubleVec(vec1);
        this.isEndless = isEndless;
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
}
