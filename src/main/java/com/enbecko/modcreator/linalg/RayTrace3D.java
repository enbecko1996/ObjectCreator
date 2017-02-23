package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;

/**
 * Created by enbec on 21.02.2017.
 */
public class RayTrace3D extends Line3D.Line3DManualUpdate {
    private final boolean isEndless;
    private final vec3.Double vec;

    public RayTrace3D(vec3.Double onPoint, vec3.Double vec1, boolean isEndless) {
        super(onPoint, (vec3.Double) onPoint.addAndMakeNew(vec3.vecPrec.DOUBLE, vec1));
        this.vec = new vec3.Double(vec1);
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

    public void updateVec(vec3.Double vec) {
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

    public vec3.Double getVec() {
        return this.vec;
    }
}
