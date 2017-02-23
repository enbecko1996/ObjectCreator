package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by enbec on 21.02.2017.
 */
public abstract class Face3D {

    final vec3 LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT;
    vec3 tmpTop = new vec3.DoubleVec();
    vec3 tmpBot = new vec3.DoubleVec();
    vec3 tmpLeft = new vec3.DoubleVec();
    vec3 tmpRight = new vec3.DoubleVec();
    //The definition of Face is a Rectangle in this case.
    //The face is not flat when no Block with a flat surface can be applied to it.
    final double isFaceFlatThreshold = 0.001;
    boolean isFlat;
    final double isFaceRectThreshold = 0.001;
    boolean isSymmetric;

    public Face3D(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
        this.LOW_LEFT = LOW_LEFT;
        this.LOW_RIGHT = LOW_RIGHT;
        this.TOP_RIGHT = TOP_RIGHT;
        this.TOP_LEFT = TOP_LEFT;
        this.updateIsFlat();
        this.updateIsSymmetric();
    }

    @Nullable
    public vec3 checkIfCrosses(RayTrace3D rayTrace3D) {
        /*
          TODO
          MAKE TWO TRIANGLE FACES AND CHECK IF RAYTRACE CROSSES ONE OF THEM.
         */
        if (this.isSymmetric()) {
            tmpBot.update(this.LOW_RIGHT).subFromThis(this.LOW_LEFT);
            tmpLeft.update(this.LOW_RIGHT).subFromThis(this.LOW_LEFT);
            Matrix.Matrix_NxN matrix = Matrix.NxN_FACTORY.makeMatrixFromColumns(tmpBot, tmpLeft, rayTrace3D.getVec().mulToThis(-1));
            vec3 rhs = (vec3) rayTrace3D.getOnPoint().subFromThis(this.LOW_LEFT);
            matrix.doLUDecomposition();
            matrix.solveLGS_fromLU(rhs);
        }
        return null;
    }

    public boolean isFlat() {
        return this.isFlat;
    }

    public boolean isSymmetric() {
        return this.isSymmetric;
    }

    public void updateIsFlat() {
        this.tmpTop.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
        this.tmpBot.update(this.LOW_RIGHT).subFromThis(this.LOW_LEFT);
        double fac = (tmpTop.getXD() != 0 && tmpBot.getXD() != 0) ? tmpBot.getXD() / tmpTop.getXD() : (tmpTop.getYD() != 0 && tmpBot.getYD() != 0) ? tmpBot.getYD() / tmpTop.getYD() : (tmpTop.getZD() != 0 && tmpBot.getZD() != 0) ? tmpBot.getZD() / tmpTop.getZD() : 0;
        if (fac != 0) {
            tmpTop.mulToThis(fac);
            System.out.println(tmpTop + " " + tmpBot);
            this.isFlat = tmpTop.subFromThis(tmpBot).length() < this.isFaceFlatThreshold;
        }
    }

    public void updateIsSymmetric() {
        this.tmpTop.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
        this.tmpBot.update(this.LOW_RIGHT).subFromThis(this.LOW_LEFT);
        this.tmpLeft.update(this.TOP_LEFT).subFromThis(this.LOW_LEFT);
        this.tmpBot.update(this.TOP_RIGHT).subFromThis(this.LOW_RIGHT);
        this.isSymmetric = tmpTop.subFromThis(tmpBot).length() < isFaceRectThreshold && tmpLeft.subFromThis(tmpRight).length() < isFaceRectThreshold;
    }

    public static class FaceAutoUpdateOnVecChange extends Face3D {
        public FaceAutoUpdateOnVecChange(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            super(LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT);
        }

        @Override
        public boolean isFlat() {
            this.tmpTop.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
            this.tmpBot.update(this.LOW_RIGHT).subFromThis(this.LOW_LEFT);
            double fac = (tmpTop.getXD() != 0 && tmpBot.getXD() != 0) ? tmpBot.getXD() / tmpTop.getXD() : (tmpTop.getYD() != 0 && tmpBot.getYD() != 0) ? tmpBot.getYD() / tmpTop.getYD() : (tmpTop.getZD() != 0 && tmpBot.getZD() != 0) ? tmpBot.getZD() / tmpTop.getZD() : 0;
            if (fac != 0) {
                tmpTop.mulToThis(fac);
                System.out.println(tmpTop + " " + tmpBot);
                return (this.isFlat = tmpTop.subFromThis(tmpBot).length() < this.isFaceFlatThreshold);
            }
            return false;
        }

        @Override
        public boolean isSymmetric() {
            this.tmpTop.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
            this.tmpBot.update(this.LOW_RIGHT).subFromThis(this.LOW_LEFT);
            this.tmpLeft.update(this.TOP_LEFT).subFromThis(this.LOW_LEFT);
            this.tmpBot.update(this.TOP_RIGHT).subFromThis(this.LOW_RIGHT);
            return (this.isSymmetric = tmpTop.subFromThis(tmpBot).length() < isFaceRectThreshold && tmpLeft.subFromThis(tmpRight).length() < isFaceRectThreshold);
        }
    }

    public static class FaceManualUpdate extends Face3D {
        public FaceManualUpdate(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            //super(new vec3(LOW_LEFT), new vec3(LOW_RIGHT), new vec3(TOP_RIGHT), new vec3(TOP_LEFT));
            super(vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_LEFT), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_RIGHT), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_RIGHT), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_LEFT));
        }

        public Face3D updateLowLeft(@Nonnull vec3 lowLeft) {
            this.LOW_LEFT.update(lowLeft);
            this.updateIsFlat();
            this.updateIsSymmetric();
            return this;
        }

        public Face3D updateLowRight(@Nonnull vec3 lowRight) {
            this.LOW_RIGHT.update(lowRight);
            this.updateIsFlat();
            this.updateIsSymmetric();
            return this;
        }

        public Face3D updateTopLeft(@Nonnull vec3 topLeft) {
            this.TOP_LEFT.update(topLeft);
            this.updateIsFlat();
            this.updateIsSymmetric();
            return this;
        }

        public Face3D updateTopRight(@Nonnull vec3 topRight) {
            this.TOP_RIGHT.update(topRight);
            this.updateIsFlat();
            this.updateIsSymmetric();
            return this;
        }

        public Face3D update(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            this.updateLowLeft(LOW_LEFT);
            this.updateLowRight(LOW_RIGHT);
            this.updateTopLeft(TOP_LEFT);
            this.updateTopRight(TOP_RIGHT);
            this.updateIsFlat();
            this.updateIsSymmetric();
            return this;
        }
    }

    public static class FaceNoUpdate extends Face3D {
        public FaceNoUpdate(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            //super(new vec3(LOW_LEFT), new vec3(LOW_RIGHT), new vec3(TOP_RIGHT), new vec3(TOP_LEFT));
            super(vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_LEFT).setChangeable(false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_RIGHT).setChangeable(false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_RIGHT).setChangeable(false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_LEFT).setChangeable(false));
        }
    }

    public static void main(String[] args) {
        Face3D face = new FaceAutoUpdateOnVecChange(new vec3.DoubleVec(0, 0, 0), new vec3.DoubleVec(1, 0, 1), new vec3.DoubleVec(3, 1, -1), new vec3.DoubleVec(2, 1, -2));
        System.out.println(face.isFlat());
    }
}
