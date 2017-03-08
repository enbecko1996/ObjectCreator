package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.linalg.Polygon3D;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec4;

import javax.annotation.Nonnull;

/**
 * Created by Niclas on 08.03.2017.
 */
public class FaceCrossposAngle {
    private final Polygon3D face;
    private final vec3 vec;
    private final vec4 angle;

    public FaceCrossposAngle(@Nonnull Polygon3D face, @Nonnull vec3 vec, vec4 angle) {
        this.face = face;
        this.vec = vec;
        this.angle = angle;
    }

    public Polygon3D getFace() {
        return this.face;
    }

    public vec3 getCrossPosOnFace() {
        return this.vec;
    }

    public vec4 getAngle() {
        return this.angle;
    }

    public String toString() {
        return "{ face = " + this.face + ", vec = " + this.vec + ",\n angle = " + this.angle.set(3, Math.toDegrees(this.angle.getAD())) + "}";
    }
}
