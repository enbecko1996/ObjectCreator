package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.linalg.RayTrace3D;

import javax.annotation.Nonnull;

/**
 * Created by Niclas on 07.03.2017.
 */
public class RayTraceResult {
    private final Bone theBone;
    private final RayTrace3D theRayTrace;
    private final Content result;
    private final FaceCrossPosAngle face;

    public RayTraceResult(@Nonnull Bone bone, @Nonnull RayTrace3D rayTrace3D, @Nonnull Content result, @Nonnull FaceCrossPosAngle face) {
        this.theBone = bone;
        this.theRayTrace = rayTrace3D;
        this.result = result;
        this.face = face;
    }

    public Bone getTheBone() {
        return this.theBone;
    }

    public RayTrace3D getTheRayTrace() {
        return this.theRayTrace;
    }

    public Content getResult() {
        return this.result;
    }

    public FaceCrossPosAngle getFace() {
        return this.face;
    }

    public String toString() {
        return "RayTraceResult: raytrace = " + theRayTrace +",\n content = " + this.result +",\n FaceCrossPosAngle = " + this.face;
    }
}
