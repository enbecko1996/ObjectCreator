package com.enbecko.modcreator.linalg;

import javax.annotation.Nullable;

/**
 * Created by Niclas on 06.03.2017.
 */
public abstract class Triangle3D {
    private final vec3 first, sec, third;
    private final vec3 tmp1 = new vec3.DoubleVec(), tmp2 = new vec3.DoubleVec(), tmpRhs = new vec3.DoubleVec();

    public Triangle3D(vec3 first, vec3 sec, vec3 third) {
        this.first = first;
        this.sec = sec;
        this.third = third;
    }

    @Nullable
    public vec3 checkIfCrosses(RayTrace3D rayTrace3D) {
        synchronized (rayTrace3D) {
            tmp1.update(third).subFromThis(first);
            tmp2.update(sec).subFromThis(first);
            Matrix.Matrix_NxN matrix = Matrix.NxN_FACTORY.makeMatrixFromColumns(this.tmp1, this.tmp2, rayTrace3D.getVec().mulToThis(-1));
            rayTrace3D.getVec().mulToThis(-1);
            matrix.doLUDecomposition();
            tmpRhs.update(rayTrace3D.getOnPoint(), false).subFromThis(this.first);
            vec_n_DOUBLE r_s_t_1 = matrix.solveLGS_fromLU(tmpRhs);
            double[] rst = r_s_t_1.getVecD();
            if (rst[2] > 0 && rst[2] < rayTrace3D.getLimit() && rst[0] >= 0 && rst[0] <= 1 && rst[1] >= 0 && rst[1] <= 1 && rst[0] + rst[1] <= 1) {
                return rayTrace3D.advanceOnVecAndReturnPosition(rst[2]);
            }
        }
        return null;
    }

    public void update(vec3 first, vec3 sec, vec3 third) {
        this.first.update(first);
        this.sec.update(sec);
        this.third.update(third);
    }

    public static class AutoUpdate extends Triangle3D{
        public AutoUpdate(vec3 first, vec3 sec, vec3 third) {
            super(first, sec, third);
        }
    }

    public static class NoUpdate extends Triangle3D{
        public NoUpdate(vec3 first, vec3 sec, vec3 third) {
            super(vec3.newVecWithPrecision(first.getPrecision(), first, false), vec3.newVecWithPrecision(sec.getPrecision(), sec, false), vec3.newVecWithPrecision(third.getPrecision(), third, false));
        }
    }
}
