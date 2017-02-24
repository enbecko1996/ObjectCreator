package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Niclas on 19.11.2016.
 */

@Deprecated
public abstract class Parallelogram3D extends Face3D {

    final vec3 onPoint;
    final vec3 vec1;
    final vec3 vec2;
    private final boolean isEndless;

    private Parallelogram3D(@Nonnull vec3 onPoint, @Nonnull vec3 vec1, @Nonnull vec3 vec2, boolean isEndless) {
        super(new vec3.FloatVec(onPoint, false), (vec3.FloatVec) onPoint.addAndMakeNew(vec_n.vecPrec.FLOAT, vec1, false), (vec3.FloatVec) onPoint.addAndMakeNew(vec_n.vecPrec.FLOAT, vec1, false).addToThis(vec2), (vec3.FloatVec) onPoint.addAndMakeNew(vec_n.vecPrec.FLOAT, vec2, false));
        this.isEndless = isEndless;
        this.onPoint = onPoint;
        this.vec1 = vec1;
        this.vec2 = vec2;
    }

    public boolean isEndless() {
        return isEndless;
    }

    @Nullable
    public vec3.DoubleVec checkIfCrosses(RayTrace3D rayTrace3D) {
        /*
          TODO
         */
        return null;
    }

    @Deprecated
    public static class ParallelogramManualUpdate extends Parallelogram3D {
        public ParallelogramManualUpdate(@Nonnull vec3 onPoint, @Nonnull vec3 vec1, @Nonnull vec3 vec2, boolean isEndless) {
            super(new vec3.DoubleVec(onPoint, false), new vec3.DoubleVec(vec1, false), new vec3.DoubleVec(vec2, false), isEndless);
        }

        public Parallelogram3D updateOnPoint(@Nonnull vec3 onPoint) {
            this.onPoint.update(onPoint);
            super.LOW_LEFT.update(onPoint);
            super.LOW_RIGHT.update(super.LOW_LEFT.addToThis(vec1));
            super.TOP_LEFT.update(super.LOW_LEFT.addToThis(vec1).addToThis(vec2));
            super.TOP_RIGHT.update(super.LOW_LEFT.addToThis(vec2));
            return this;
        }

        public Parallelogram3D updateVecs(@Nullable vec3 vec1, @Nullable vec3 vec2) {
            if(vec1 != null)
                this.vec1.update(vec1);
            if(vec2 != null)
                this.vec2.update(vec2);
            super.LOW_RIGHT.update(super.LOW_LEFT.addToThis(this.vec1));
            super.TOP_LEFT.update(super.LOW_LEFT.addToThis(this.vec1).addToThis(this.vec2));
            super.TOP_RIGHT.update(super.LOW_LEFT.addToThis(this.vec2));
            return this;
        }

        public Parallelogram3D update(@Nonnull vec3 onPoint, @Nullable vec3 vec1, @Nullable vec3 vec2) {
            this.onPoint.update(onPoint);
            if(vec1 != null)
                this.vec1.update(vec1);
            if(vec2 != null)
                this.vec2.update(vec2);
            super.LOW_LEFT.update(this.onPoint);
            super.LOW_RIGHT.update(super.LOW_LEFT.addToThis(this.vec1));
            super.TOP_LEFT.update(super.LOW_LEFT.addToThis(this.vec1).addToThis(this.vec2));
            super.TOP_RIGHT.update(super.LOW_LEFT.addToThis(this.vec2));
            return this;
        }
    }

    @Deprecated
    public static class ParallelogramNoUpdate extends Parallelogram3D {
        public ParallelogramNoUpdate(vec3 onPoint, vec3 vec1, vec3 vec2, boolean isEndless) {
            super(new vec3.DoubleVec(onPoint, false), new vec3.DoubleVec(vec1, false), new vec3.DoubleVec(vec2, false), isEndless);
        }
    }
}
