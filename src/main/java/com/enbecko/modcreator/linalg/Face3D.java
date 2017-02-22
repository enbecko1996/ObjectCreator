package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by enbec on 21.02.2017.
 */
public class Face3D {

    final vec3 LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT;

    public Face3D(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
        this.LOW_LEFT = LOW_LEFT;
        this.LOW_RIGHT = LOW_RIGHT;
        this.TOP_RIGHT = TOP_RIGHT;
        this.TOP_LEFT = TOP_LEFT;
    }

    @Nullable
    public vec3 checkIfCrosses(RayTrace3D rayTrace3D) {
        /*
          TODO
          MAKE TWO TRIANGLE FACES AND CHECK IF RAYTRACE CROSSES ONE OF THEM.
         */

        return null;
    }

    public static class FaceAutoUpdateOnVecChange extends Face3D {
        public FaceAutoUpdateOnVecChange(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            super(LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT);
        }
    }

    public static class FaceManualUpdate extends Face3D {
        public FaceManualUpdate(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            //super(new vec3(LOW_LEFT), new vec3(LOW_RIGHT), new vec3(TOP_RIGHT), new vec3(TOP_LEFT));
            super(vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_LEFT), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_RIGHT), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_RIGHT), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_LEFT));
        }

        public Face3D updateLowLeft(@Nonnull vec3 lowLeft) {
            this.LOW_LEFT.update(lowLeft);
            return this;
        }

        public Face3D updateLowRight(@Nonnull vec3 lowRight) {
            this.LOW_RIGHT.update(lowRight);
            return this;
        }

        public Face3D updateTopLeft(@Nonnull vec3 topLeft) {
            this.TOP_LEFT.update(topLeft);
            return this;
        }

        public Face3D updateTopRight(@Nonnull vec3 topRight) {
            this.TOP_RIGHT.update(topRight);
            return this;
        }

        public Face3D update(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            this.updateLowLeft(LOW_LEFT);
            this.updateLowRight(LOW_RIGHT);
            this.updateTopLeft(TOP_LEFT);
            this.updateTopRight(TOP_RIGHT);
            return this;
        }
    }

    public static class FaceNoUpdate extends Face3D {
        public FaceNoUpdate(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            //super(new vec3(LOW_LEFT), new vec3(LOW_RIGHT), new vec3(TOP_RIGHT), new vec3(TOP_LEFT));
            super(vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_LEFT).setChangeable(false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_RIGHT).setChangeable(false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_RIGHT).setChangeable(false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_LEFT).setChangeable(false));
        }
    }
}
