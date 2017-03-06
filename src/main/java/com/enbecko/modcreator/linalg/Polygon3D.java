package com.enbecko.modcreator.linalg;

/**
 * Created by Niclas on 06.03.2017.
 */
public class Polygon3D {
    private final PolygonDrawStyle drawStyle;
    private final vec3[] corners;
    public Polygon3D(PolygonDrawStyle drawStyle, vec3 ... corners) {
        this.drawStyle = drawStyle;
        this.corners = corners;
        switch (drawStyle) {
            case TRIANGLES:
                if (this.corners.length == 3) {

                }
                throw new RuntimeException("Please create multiple Triangles instead");
            case QUADS:
                throw new RuntimeException("Please create multiple Triangles instead");
        }
    }

    public enum  PolygonDrawStyle {
        TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN, QUAD_STRIP, QUADS, POLYGON;
    }
}
