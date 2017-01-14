package com.enbecko.modcreator.linalg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Niclas on 19.11.2016.
 */
public class Face3D {

    private final vec3.Double onPoint = new vec3.Double();
    private final vec3.Double vec1 = new vec3.Double();
    private final vec3.Double vec2 = new vec3.Double();
    vec3.Double copyVec1 = new vec3.Double();
    vec3.Double copyVec2 = new vec3.Double();
    vec3.Double copyVec3 = new vec3.Double();
    vec3.Double copyOnThis = new vec3.Double();
    vec3.Double copyOnLine = new vec3.Double();
    private final boolean isEndless;

    public Face3D(vec3.Double onPoint, vec3.Double vec1, vec3.Double vec2, boolean isEndless) {
        this.isEndless = isEndless;
        if(onPoint != null)
            this.onPoint.update(onPoint);
        if(vec1 != null)
            this.vec1.update(vec1);
        if(vec2 != null)
            this.vec2.update(vec2);
    }

    public Face3D() {
        this.isEndless = true;
    }

    public Face3D updateOnPoint(@Nonnull vec3.Double onPoint) {
        this.onPoint.update(onPoint);
        return this;
    }

    public Face3D updateVecs(@Nullable vec3.Double vec1, @Nullable vec3.Double vec2) {
        if(vec1 != null)
            this.vec1.update(vec1);
        if(vec2 != null)
            this.vec2.update(vec2);
        return this;
    }

    public Face3D update(@Nonnull vec3.Double onPoint, @Nullable vec3.Double vec1, @Nullable vec3.Double vec2) {
        this.onPoint.update(onPoint);
        if(vec1 != null)
            this.vec1.update(vec1);
        if(vec2 != null)
            this.vec2.update(vec2);
        return this;
    }

    @Nullable
    public vec3.Double checkIfCrosses(RayTrace3D rayTrace3D) {
        /*
          TODO
         */
        return null;
    }
}
