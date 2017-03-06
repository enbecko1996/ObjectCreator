package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;

/**
 * Created by Niclas on 19.11.2016.
 */
public class Line3D {
    final vec3 onPoint;
    final vec3 endPoint;

    public Line3D(vec3 onPoint, vec3 end) {
        this.onPoint = onPoint;
        this.endPoint = end;
    }

    public vec3 getOnPoint() {
        return onPoint;
    }

    public vec3 getEndPoint() {
        return endPoint;
    }

    public vec3 getVec() {
        return (vec3) this.endPoint.subAndMakeNew(this.endPoint.getPrecision(), this.onPoint, false);
    }

    public static class Line3DAutoUpdateOnVecChange extends Line3D{
        public Line3DAutoUpdateOnVecChange(vec3 onPoint, vec3 end) {
            super(onPoint, end);
        }
    }

    public static class Line3DManualUpdate extends Line3D {
        public Line3DManualUpdate(vec3 onPoint, vec3 end) {
            //super(new vec3(onPoint), new vec3(end));
            super(vec3.newVecWithPrecision(onPoint.getPrecision(), onPoint, false), vec3.newVecWithPrecision(end.getPrecision(), end, false));
        }

        public Line3D updateOnPoint(vec3 onPoint) {
            this.onPoint.update(onPoint, false);
            return this;
        }

        public void updateEndPoint(vec3 end) {
            this.endPoint.update(end, false);
        }

        public Line3D update(vec3 onPoint, @Nonnull vec3 end) {
            this.onPoint.update(onPoint, false);
            this.endPoint.update(end, false);
            return this;
        }
    }

    public static class Line3DNoUpdate extends Line3D {
        public Line3DNoUpdate(vec3 onPoint, vec3 end) {
            //super(new vec3(onPoint), new vec3(end));
            super((vec3) vec3.newVecWithPrecision(onPoint.getPrecision(), onPoint, false).setChangeable(false), (vec3) vec3.newVecWithPrecision(end.getPrecision(), end, false).setChangeable(false));
        }
    }
}
