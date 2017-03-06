package com.enbecko.modcreator.linalg;

import com.enbecko.modcreator.RenderPolygon;
import com.sun.istack.internal.Nullable;

/**
 * Created by Niclas on 06.03.2017.
 */
public abstract class Polygon3D <T extends RenderPolygon> {
    private final vec3[] corners;
    private T renderer;

    public Polygon3D(vec3 ... corners) {
        this.corners = corners;
    }

    @Nullable
    public T getRenderer() {
        return this.renderer;
    }

    public T setRenderer(T renderer) {
        this.renderer = renderer;
        return renderer;
    }

    public abstract vec3 checkIfCrosses(RayTrace3D rayTrace3D);

    public abstract boolean isFlat();

    public abstract boolean isSymmetric();

    public abstract boolean isConvex();
}
