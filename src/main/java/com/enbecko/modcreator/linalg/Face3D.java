package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by enbec on 21.02.2017.
 */
public abstract class Face3D {

    final vec3 LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT;
    private vec3 tmpTop = new vec3.DoubleVec();
    private vec3 tmpBot = new vec3.DoubleVec();
    private vec3 tmpLeft = new vec3.DoubleVec();
    private vec3 tmpRight = new vec3.DoubleVec();
    private vec3 tmpDiag = new vec3.DoubleVec();
    private vec3 tmpRhs = new vec3.DoubleVec();
    //For example if objects should bounce of this surface.
    //Normals are only nonnull if this isPhysical;
    private boolean isPhysical;
    private vec3 normTriang1, normTriang2;
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

    /**
     *
     * @param rayTrace3D's vecs are changed during the process. Should be made back to normal afterwards.
     * @return null if no cross. crossposition as vec3 otherwise.
     */
    @Nullable
    public vec3 checkIfCrosses(RayTrace3D rayTrace3D) {
        /*
          TODO
          MAKE TWO TRIANGLE FACES AND CHECK IF RAYTRACE CROSSES ONE OF THEM.
         */
        synchronized (rayTrace3D) {
            if (this.isSymmetric()) {
                tmpTop.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
                tmpLeft.update(this.LOW_LEFT).subFromThis(this.TOP_LEFT);
                Matrix.Matrix_NxN matrix = Matrix.NxN_FACTORY.makeMatrixFromColumns(tmpTop, tmpLeft, rayTrace3D.getVec().mulToThis(-1));
                rayTrace3D.getVec().mulToThis(-1);
                tmpRhs.update(rayTrace3D.getOnPoint()).subFromThis(this.TOP_LEFT);
                matrix.doLUDecomposition();
                vec_n_DOUBLE r_s_t = matrix.solveLGS_fromLU(tmpRhs);
                double[] rst = r_s_t.getVecD();
                if (rst[2] >  0 && rst[2] < rayTrace3D.getLimit() && rst[0] >= 0 && rst[0] <= 1 && rst[1] >= 0 && rst[1] <= 1) {
                    return rayTrace3D.advanceOnVecAndReturnPosition(rst[2]);
                }
            } else {
                tmpTop.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
                tmpLeft.update(this.LOW_LEFT).subFromThis(this.TOP_LEFT);
                this.tmpDiag.update(this.LOW_RIGHT).subFromThis(this.TOP_LEFT);
                Matrix.Matrix_NxN matrix = Matrix.NxN_FACTORY.makeMatrixFromColumns(tmpLeft, tmpDiag, rayTrace3D.getVec().mulToThis(-1));
                rayTrace3D.getVec().mulToThis(-1);
                matrix.doLUDecomposition();
                tmpRhs.update(rayTrace3D.getOnPoint()).subFromThis(this.TOP_LEFT);
                vec_n_DOUBLE r_s_t_1 = matrix.solveLGS_fromLU(tmpRhs);
                double[] rst = r_s_t_1.getVecD();
                if (rst[2] >  0 && rst[2] < rayTrace3D.getLimit() && rst[0] >= 0 && rst[0] <= 1 && rst[1] >= 0 && rst[1] <= 1) {
                    return rayTrace3D.advanceOnVecAndReturnPosition(rst[2]);
                } else {
                    matrix.setColumn(0, tmpDiag);
                    matrix.setColumn(1, tmpTop);
                    matrix.doLUDecomposition();
                    vec_n_DOUBLE r_s_t_2 = matrix.solveLGS_fromLU(tmpRhs);
                    rst = r_s_t_2.getVecD();
                    if (rst[2] >  0 && rst[2] < rayTrace3D.getLimit() && rst[0] >= 0 && rst[0] <= 1 && rst[1] >= 0 && rst[1] <= 1) {
                        return rayTrace3D.advanceOnVecAndReturnPosition(rst[2]);
                    }
                }
            }
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
            this.isFlat = tmpTop.subFromThis(tmpBot).length() < this.isFaceFlatThreshold;
        }
    }

    public void updateIsSymmetric() {
        this.tmpTop.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
        this.tmpBot.update(this.LOW_RIGHT).subFromThis(this.LOW_LEFT);
        this.tmpLeft.update(this.TOP_LEFT).subFromThis(this.LOW_LEFT);
        this.tmpRight.update(this.TOP_RIGHT).subFromThis(this.LOW_RIGHT);
        this.isSymmetric = tmpTop.subFromThis(tmpBot).length() < isFaceRectThreshold && tmpLeft.subFromThis(tmpRight).length() < isFaceRectThreshold;
    }

    public void calculateNormals(vec3 left, vec3 diag, vec3 top) {
        this.normTriang1 = ((vec3) left.mulToThis(-1)).cross(diag);
        left.mulToThis(-1);
        this.normTriang2 = diag.cross(top);
    }

    public void calculateNormals() {
        this.tmpTop.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
        this.tmpDiag.update(this.LOW_RIGHT).subFromThis(this.TOP_LEFT);
        this.tmpLeft.update(this.TOP_LEFT).subFromThis(this.LOW_LEFT);
        this.calculateNormals(tmpLeft, tmpDiag, tmpTop);
    }

    public void setPhysical(boolean physical) {
        this.isPhysical = physical;
        if (this.isPhysical)
            this.calculateNormals();
        else {
            this.normTriang1 = null;
            this.normTriang2 = null;
        }
    }

    public vec3 getTriangleNormal1() {
        if (this.isPhysical) {
            return this.normTriang1;
        } else
            throw new RuntimeException("You can't get the normals of a Face which is not physical: " + this);
    }

    public vec3 getTriangleNormal2() {
        if (this.isPhysical) {
            return this.normTriang2;
        } else
            throw new RuntimeException("You can't get the normals of a Face which is not physical: " + this);
    }

    public boolean isPhysical() {
        return this.isPhysical;
    }

    @Deprecated
    public static class FaceAutoUpdateOnVecChange extends FaceManualUpdate {
        public FaceAutoUpdateOnVecChange(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            super(LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT);
        }

        public vec3 getTriangleNormal1() {
            this.calculateNormals();
            return super.getTriangleNormal1();
        }

        public vec3 getTriangleNormal2() {
            this.calculateNormals();
            return super.getTriangleNormal2();
        }

        @Override
        public boolean isFlat() {
            this.updateIsFlat();
            return super.isFlat();
        }

        @Override
        public boolean isSymmetric() {
            this.updateIsSymmetric();
            return super.isSymmetric();
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
            if (this.isPhysical())
                this.calculateNormals();
            return this;
        }

        public Face3D updateLowRight(@Nonnull vec3 lowRight) {
            this.LOW_RIGHT.update(lowRight);
            this.updateIsFlat();
            this.updateIsSymmetric();
            if (this.isPhysical())
                this.calculateNormals();
            return this;
        }

        public Face3D updateTopLeft(@Nonnull vec3 topLeft) {
            this.TOP_LEFT.update(topLeft);
            this.updateIsFlat();
            this.updateIsSymmetric();
            if (this.isPhysical())
                this.calculateNormals();
            return this;
        }

        public Face3D updateTopRight(@Nonnull vec3 topRight) {
            this.TOP_RIGHT.update(topRight);
            this.updateIsFlat();
            this.updateIsSymmetric();
            if (this.isPhysical())
                this.calculateNormals();
            return this;
        }

        public Face3D update(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            this.updateLowLeft(LOW_LEFT);
            this.updateLowRight(LOW_RIGHT);
            this.updateTopLeft(TOP_LEFT);
            this.updateTopRight(TOP_RIGHT);
            this.updateIsFlat();
            this.updateIsSymmetric();
            if (this.isPhysical())
                this.calculateNormals();
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
        Face3D face = new FaceAutoUpdateOnVecChange(new vec3.DoubleVec(0, 5, 0), new vec3.DoubleVec(1, 5, 0), new vec3.DoubleVec(1, 6, 0), new vec3.DoubleVec(0, 6, 0));
        face.setPhysical(true);
        RayTrace3D rayTrace3D = new RayTrace3D(new vec3.DoubleVec(-0, 5.5, 1), new vec3.DoubleVec(0, 0, -1), 100, true);
        long startTime = System.currentTimeMillis();
        int testCount = 100;
        for (int k = 0; k < testCount; k++)
            face.checkIfCrosses(rayTrace3D);
        System.out.println("Time: " + (System.currentTimeMillis() - startTime));
    }
}
