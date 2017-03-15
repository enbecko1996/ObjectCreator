package com.enbecko.modcreator.linalg;

import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.Log.LogEnums;
import com.enbecko.modcreator.RenderQuadrilateral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by enbec on 21.02.2017.
 */
public abstract class Quadrilateral3D extends Polygon3D<RenderQuadrilateral> {

    public final vec3 LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT;
    //For example if objects should bounce of this surface.
    //Normals are only nonnull if this isPhysical;
    private vec3 tmpRhs = new vec3.DoubleVec();
    private boolean isPhysical;
    private vec3 normTriang1, normTriang2;
    //The definition of Face is a Rectangle in this case.
    //The face is not flat when no Block with a flat surface can be applied to it.
    final double isFaceFlatThreshold = 0.001;
    boolean isFlat;
    final double isFaceRectThreshold = 0.001;
    boolean isSymmetric;
    protected vec3 criticalConvexOnPoint = new vec3.DoubleVec();
    protected vec3 criticalConvexFirst = new vec3.DoubleVec();
    protected vec3 criticalConvexSec = new vec3.DoubleVec();
    protected vec3 criticalConvexDiag = new vec3.DoubleVec();
    boolean isConvex;

    public Quadrilateral3D(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
        super(LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT);
        this.LOW_LEFT = LOW_LEFT;
        this.LOW_RIGHT = LOW_RIGHT;
        this.TOP_RIGHT = TOP_RIGHT;
        this.TOP_LEFT = TOP_LEFT;
        this.updateIsFlat();
        this.updateIsSymmetric();
        this.updateIsConvex();
        this.calculateNormals();
    }

    public abstract void makeCriticalVecsForRayTrace();

    /**
     * @param rayTrace3D's vecs are changed during the process. Should be made back to normal afterwards.
     * @return null if no cross. crossposition as vec3 otherwise.
     */
    @Override
    @Nullable
    public vec3 checkIfCrosses(RayTrace3D rayTrace3D) {
        synchronized (rayTrace3D) {
            this.makeCriticalVecsForRayTrace();
            if (this.isSymmetric()) {
                Matrix.Matrix_NxN matrix = Matrix.NxN_FACTORY.makeMatrixFromColumns(this.criticalConvexSec, this.criticalConvexFirst, rayTrace3D.getVec().mulToThis(-1));
                rayTrace3D.getVec().mulToThis(-1);
                tmpRhs.update(rayTrace3D.getOnPoint(), false).subFromThis(this.criticalConvexOnPoint);
                matrix.doLUDecomposition();
                vec_n_DOUBLE r_s_t = matrix.solveLGS_fromLU(tmpRhs);
                double[] rst = r_s_t.getVecD();
                if (rst[2] > 0 && rst[2] < rayTrace3D.getLimit() && rst[0] >= 0 && rst[0] <= 1 && rst[1] >= 0 && rst[1] <= 1) {
                    return rayTrace3D.advanceOnVecAndReturnPosition(rst[2]);
                }
            } else {
                Matrix.Matrix_NxN matrix = Matrix.NxN_FACTORY.makeMatrixFromColumns(this.criticalConvexFirst, this.criticalConvexDiag, rayTrace3D.getVec().mulToThis(-1));
                rayTrace3D.getVec().mulToThis(-1);
                matrix.doLUDecomposition();
                tmpRhs.update(rayTrace3D.getOnPoint(), false).subFromThis(this.criticalConvexOnPoint);
                vec_n_DOUBLE r_s_t_1 = matrix.solveLGS_fromLU(tmpRhs);
                //Log.d(Log.LogEnums.GEOMETRY, matrix + ""+tmpRhs +""+ r_s_t_1);
                double[] rst = r_s_t_1.getVecD();
                if (rst[2] > 0 && rst[2] < rayTrace3D.getLimit() && rst[0] >= 0 && rst[0] <= 1 && rst[1] >= 0 && rst[1] <= 1 && rst[0] + rst[1] <= 1) {
                    return rayTrace3D.advanceOnVecAndReturnPosition(rst[2]);
                } else {
                    matrix.setColumn(0, this.criticalConvexDiag);
                    matrix.setColumn(1, this.criticalConvexSec);
                    matrix.doLUDecomposition();
                    vec_n_DOUBLE r_s_t_2 = matrix.solveLGS_fromLU(tmpRhs);
                    //Log.d(Log.LogEnums.GEOMETRY, matrix + ""+tmpRhs +""+ r_s_t_2);
                    rst = r_s_t_2.getVecD();
                    if (rst[2] > 0 && rst[2] < rayTrace3D.getLimit() && rst[0] >= 0 && rst[0] <= 1 && rst[1] >= 0 && rst[1] <= 1 && rst[0] + rst[1] <= 1) {
                        return rayTrace3D.advanceOnVecAndReturnPosition(rst[2]);
                    }
                }
            }
        }
        return null;
    }

    /*
    *   TODO
     */
    public vec3 getNormal() {
        if (this.isFlat())
            return this.normTriang1;
        else
            return null;
    }

    /*
    *  TODO
    */
    public double getAngle(RayTrace3D rayTrace3D, vec3 normal) {
        if (this.isFlat())
            return this.getTriangleNormal1().angleTo360(rayTrace3D.getVec(), normal);
        else
            return 0;
    }

    /*
    *  TODO
    */
    @Nullable
    public vec4 getAngleAndAngleNormal(RayTrace3D rayTrace3D) {
        if (this.isFlat())
            return this.getTriangleNormal1().angleTo360(rayTrace3D.getVec());
        return null;
    }

    public boolean isFlat() {
        return this.isFlat;
    }

    public boolean isSymmetric() {
        return this.isSymmetric;
    }

    public boolean isConvex() {
        return this.isConvex;
    }

    public void updateIsFlat() {
        vec3 tmpTop = new vec3.DoubleVec();
        vec3 tmpBot = new vec3.DoubleVec();
        vec3 tmpLeft = new vec3.DoubleVec();
        tmpTop.update(this.TOP_RIGHT, false).subFromThis(this.TOP_LEFT);
        tmpBot.update(this.LOW_LEFT, false).subFromThis(this.LOW_RIGHT);
        tmpLeft.update(this.LOW_LEFT, false).subFromThis(this.TOP_LEFT);
        vec3 cross1 = (vec3) tmpTop.cross(tmpLeft, false).normalize();
        vec3 cross2 = (vec3) tmpLeft.cross(tmpBot, false).normalize();
        this.isFlat = cross1.subFromThis(cross2).length() < isFaceFlatThreshold;
    }

    public void updateIsSymmetric() {
        vec3 tmpTop = new vec3.DoubleVec();
        vec3 tmpBot = new vec3.DoubleVec();
        vec3 tmpLeft = new vec3.DoubleVec();
        vec3 tmpRight = new vec3.DoubleVec();
        tmpTop.update(this.TOP_RIGHT, false).subFromThis(this.TOP_LEFT);
        tmpBot.update(this.LOW_RIGHT, false).subFromThis(this.LOW_LEFT);
        tmpLeft.update(this.TOP_LEFT, false).subFromThis(this.LOW_LEFT);
        tmpRight.update(this.TOP_RIGHT, false).subFromThis(this.LOW_RIGHT);
        this.isSymmetric = tmpTop.subFromThis(tmpBot).length() < isFaceRectThreshold && tmpLeft.subFromThis(tmpRight).length() < isFaceRectThreshold;
    }

    public void updateIsConvex() {
        vec3 tmp = (vec3) new vec3.DoubleVec(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
        vec3 tmp2 = (vec3) new vec3.DoubleVec(this.LOW_RIGHT).subFromThis(this.TOP_RIGHT);
        vec3 normal = (vec3) tmp.cross(tmp2, false).normalize();
        double ang1 = tmp.angleTo360(tmp2, normal);
        tmp.update(this.LOW_LEFT).subFromThis(this.LOW_RIGHT);
        double ang2 = tmp2.angleTo360(tmp, normal);
        tmp2.update(this.TOP_LEFT).subFromThis(this.LOW_LEFT);
        double ang3 = tmp.angleTo360(tmp2, normal);
        tmp.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
        double ang4 = tmp2.angleTo360(tmp, normal);
        if ((ang1 > Math.PI && ang2 > Math.PI) || (ang1 > Math.PI && ang3 > Math.PI) || (ang1 > Math.PI && ang4 > Math.PI) || (ang2 > Math.PI && ang3 > Math.PI) || (ang2 > Math.PI && ang4 > Math.PI) || (ang3 > Math.PI && ang4 > Math.PI)) {
            ang1 = Math.PI * 2 - ang1;
            ang2 = Math.PI * 2 - ang2;
            ang3 = Math.PI * 2 - ang3;
            ang4 = Math.PI * 2 - ang4;
        }
        if (ang1 > Math.PI || ang3 > Math.PI) {
            this.isConvex = false;
            this.criticalConvexDiag.update(this.TOP_RIGHT).subFromThis(this.LOW_LEFT);
            this.criticalConvexFirst.update(this.TOP_LEFT).subFromThis(this.LOW_LEFT);
            this.criticalConvexSec.update(this.LOW_RIGHT).subFromThis(this.LOW_LEFT);
            this.criticalConvexOnPoint.update(this.LOW_LEFT);
        } else if (ang2 > Math.PI || ang4 > Math.PI) {
            this.isConvex = false;
            this.criticalConvexDiag.update(this.LOW_RIGHT).subFromThis(this.TOP_LEFT);
            this.criticalConvexFirst.update(this.LOW_LEFT).subFromThis(this.TOP_LEFT);
            this.criticalConvexSec.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
            this.criticalConvexOnPoint.update(this.TOP_LEFT);
        } else {
            this.isConvex = true;
            if ((ang2 > ang1 && ang2 > ang3 && ang2 > ang4) || (ang4 > ang1 && ang4 > ang3 && ang4 > ang2)) {
                this.criticalConvexDiag.update(this.LOW_RIGHT).subFromThis(this.TOP_LEFT);
                this.criticalConvexFirst.update(this.LOW_LEFT).subFromThis(this.TOP_LEFT);
                this.criticalConvexSec.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
                this.criticalConvexOnPoint.update(this.TOP_LEFT);
            } else {
                this.criticalConvexDiag.update(this.TOP_RIGHT).subFromThis(this.LOW_LEFT);
                this.criticalConvexFirst.update(this.TOP_LEFT).subFromThis(this.LOW_LEFT);
                this.criticalConvexSec.update(this.LOW_RIGHT).subFromThis(this.LOW_LEFT);
                this.criticalConvexOnPoint.update(this.LOW_LEFT);
            }
        }
    }

    public void calculateNormals(vec3 left, vec3 diag, vec3 top) {
        this.normTriang1 = ((vec3) left.mulToThis(-1)).cross(diag, false);
        left.mulToThis(-1);
        this.normTriang2 = diag.cross(top, false);
    }

    public void calculateNormals() {
        this.calculateNormals(this.criticalConvexFirst, this.criticalConvexDiag, this.criticalConvexSec);
    }

    public void setPhysical(boolean physical) {
        this.isPhysical = physical;
    }

    public vec3 getTriangleNormal1() {
        return this.normTriang1;
    }

    public vec3 getTriangleNormal2() {
        return this.normTriang2;
    }

    public boolean isPhysical() {
        return this.isPhysical;
    }

    public String toString() {
        return "Quadliteral: [low_left = " + this.LOW_LEFT + ", low_right = " + this.LOW_RIGHT + ", top_rigtht = " + this.TOP_RIGHT + ", top_left = " + this.TOP_LEFT+"]";
    }

    public static class AutoUpdate extends Quadrilateral3D {
        public AutoUpdate(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            super(LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT);
        }

        @Override
        public void makeCriticalVecsForRayTrace() {
            this.updateIsConvex();
        }

        @Override
        public vec3 getTriangleNormal1() {
            this.calculateNormals();
            return super.getTriangleNormal1();
        }

        @Override
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

        @Override
        public boolean isConvex() {
            this.updateIsConvex();
            return super.isConvex();
        }

        public static class Symmetrical extends AutoUpdate {
            public Symmetrical(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
                super(LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT);
            }

            @Override
            public boolean isFlat() {
                return true;
            }

            @Override
            public void makeCriticalVecsForRayTrace() {
                this.criticalConvexDiag.update(this.LOW_RIGHT).subFromThis(this.TOP_LEFT);
                this.criticalConvexFirst.update(this.LOW_LEFT).subFromThis(this.TOP_LEFT);
                this.criticalConvexSec.update(this.TOP_RIGHT).subFromThis(this.TOP_LEFT);
                this.criticalConvexOnPoint.update(this.TOP_LEFT);
            }

            @Override
            public boolean isSymmetric() {
                return true;
            }

            @Override
            public boolean isConvex() {
                return true;
            }
        }
    }

    public static class ManualUpdate extends Quadrilateral3D {
        public ManualUpdate(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            //super(new vec3(LOW_LEFT), new vec3(LOW_RIGHT), new vec3(TOP_RIGHT), new vec3(TOP_LEFT));
            super(vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_LEFT, false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_RIGHT, false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_RIGHT, false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_LEFT, false));
        }

        @Override
        public void makeCriticalVecsForRayTrace() {
        }

        public Quadrilateral3D updateLowLeft(@Nonnull vec3 lowLeft) {
            this.LOW_LEFT.update(lowLeft, false);
            this.updateIsFlat();
            this.updateIsSymmetric();
            this.updateIsConvex();
            this.calculateNormals();
            return this;
        }

        public Quadrilateral3D updateLowRight(@Nonnull vec3 lowRight) {
            this.LOW_RIGHT.update(lowRight, false);
            this.updateIsFlat();
            this.updateIsSymmetric();
            this.updateIsConvex();
            this.calculateNormals();
            return this;
        }

        public Quadrilateral3D updateTopLeft(@Nonnull vec3 topLeft) {
            this.TOP_LEFT.update(topLeft, false);
            this.updateIsFlat();
            this.updateIsSymmetric();
            this.updateIsConvex();
            this.calculateNormals();
            return this;
        }

        public Quadrilateral3D updateTopRight(@Nonnull vec3 topRight) {
            this.TOP_RIGHT.update(topRight, false);
            this.updateIsFlat();
            this.updateIsSymmetric();
            this.updateIsConvex();
            this.calculateNormals();
            return this;
        }

        public Quadrilateral3D update(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            this.updateLowLeft(LOW_LEFT);
            this.updateLowRight(LOW_RIGHT);
            this.updateTopLeft(TOP_LEFT);
            this.updateTopRight(TOP_RIGHT);
            this.updateIsFlat();
            this.updateIsSymmetric();
            this.updateIsConvex();
            this.calculateNormals();
            return this;
        }
    }

    public static class NoUpdate extends Quadrilateral3D {
        public NoUpdate(vec3 LOW_LEFT, vec3 LOW_RIGHT, vec3 TOP_RIGHT, vec3 TOP_LEFT) {
            //super(new vec3(LOW_LEFT), new vec3(LOW_RIGHT), new vec3(TOP_RIGHT), new vec3(TOP_LEFT));
            super(vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_LEFT, false).setChangeable(false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), LOW_RIGHT, false).setChangeable(false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_RIGHT, false).setChangeable(false), vec3.newVecWithPrecision(LOW_LEFT.getPrecision(), TOP_LEFT, false).setChangeable(false));
        }

        @Override
        public void makeCriticalVecsForRayTrace() {
        }
    }

    public static void main(String[] args) {
        Quadrilateral3D face = new AutoUpdate(
                new vec3.DoubleVec(0, 0, 0, false),
                new vec3.DoubleVec(1, 0, 0.99, false),
                new vec3.DoubleVec(0.5, 0.1, 0, false),
                new vec3.DoubleVec(1, 1, 0, false));
        face.setPhysical(true);
        RayTrace3D rayTrace3D = new RayTrace3D(new vec3.DoubleVec(1, 0, 1, false), new vec3.DoubleVec(0, 0, -1, false), 100, true);
        long startTime = System.currentTimeMillis();
        //int testCount = 100;
        //for (int k = 0; k < testCount; k++)
        Log.d(LogEnums.GEOMETRY, face.checkIfCrosses(rayTrace3D));
        Log.d(LogEnums.GEOMETRY, "Time: " + (System.currentTimeMillis() - startTime));
    }
}
