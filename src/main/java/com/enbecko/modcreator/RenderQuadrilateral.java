package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.util.Arrays;

/**
 * Created by enbec on 03.03.2017.
 */
public abstract class RenderQuadrilateral extends RenderPolygon {
    /**
     * TODO
     */
    public static class Textured extends RenderPolygon <vec3Vert.PosTex> {
        public Textured(vec3Vert.PosTex LOW_LEFT, vec3Vert.PosTex LOW_RIGHT, vec3Vert.PosTex TOP_RIGHT, vec3Vert.PosTex TOP_LEFT) {
            super(LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT);
        }

        public Textured(Quadrilateral3D face, vec2.FloatVec uv_LOW_LEFT, vec2.FloatVec uv_LOW_RIGHT, vec2.FloatVec uv_TOP_RIGHT, vec2.FloatVec uv_TOP_LEFT, boolean autoUpdate) {
            super(autoUpdate ? new vec3Vert.PosTex.AllUpdate(face.LOW_LEFT, uv_LOW_LEFT) : new vec3Vert.PosTex.NoUpdate(face.LOW_LEFT, uv_LOW_LEFT),
                    autoUpdate ? new vec3Vert.PosTex.AllUpdate(face.LOW_RIGHT, uv_LOW_RIGHT) : new vec3Vert.PosTex.NoUpdate(face.LOW_RIGHT, uv_LOW_RIGHT),
                    autoUpdate ? new vec3Vert.PosTex.AllUpdate(face.TOP_RIGHT, uv_TOP_RIGHT) : new vec3Vert.PosTex.NoUpdate(face.TOP_RIGHT, uv_TOP_RIGHT),
                    autoUpdate ? new vec3Vert.PosTex.AllUpdate(face.TOP_LEFT, uv_TOP_LEFT) : new vec3Vert.PosTex.NoUpdate(face.TOP_LEFT, uv_TOP_LEFT));
        }

        public Textured(Quadrilateral3D face, boolean autoUpdate) {
            this(face, new vec2.FloatVec(0, 0), new vec2.FloatVec(0, 1), new vec2.FloatVec(1, 1), new vec2.FloatVec(1, 0), autoUpdate);
        }

        @Override
        public void draw(VertexBuffer vertexBuffer, float scale) {
            vec3.FloatVec first = (vec3.FloatVec) this.getVertexAt(3).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec sec = (vec3.FloatVec) this.getVertexAt(1).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec normal = first.cross(sec, false);

            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);

            for(int i = 0; i < nVertices; ++i) {
                vec3Vert.PosTex posTex = this.getVertexAt(i);
                this.putPosition(vertexBuffer, posTex.position, scale);
                this.putTexture(vertexBuffer, posTex.texture);
                this.putNormal(vertexBuffer, normal);
                vertexBuffer.endVertex();
            }
            Tessellator.getInstance().draw();
        }
    }

    public static class Colored extends RenderPolygon<vec3Vert.PosCol> {
        public Colored(vec3Vert.PosCol first, vec3Vert.PosCol sec, vec3Vert.PosCol third) {
            super(first, sec, third);
        }

        @Deprecated
        public Colored(Quadrilateral3D face, vec4.FloatVec rgb_LOW_LEFT, vec4.FloatVec rgb_LOW_RIGHT, vec4.FloatVec rgb_TOP_RIGHT, vec4.FloatVec rgb_TOP_LEFT, boolean autoUpdate) {
            super(autoUpdate ? new vec3Vert.PosCol.AllUpdate(face.LOW_LEFT, rgb_LOW_LEFT) : new vec3Vert.PosCol.NoUpdate(face.LOW_LEFT, rgb_LOW_LEFT),
                    autoUpdate ? new vec3Vert.PosCol.AllUpdate(face.LOW_RIGHT, rgb_LOW_RIGHT) : new vec3Vert.PosCol.NoUpdate(face.LOW_RIGHT, rgb_LOW_RIGHT),
                    autoUpdate ? new vec3Vert.PosCol.AllUpdate(face.TOP_RIGHT, rgb_TOP_RIGHT) : new vec3Vert.PosCol.NoUpdate(face.TOP_RIGHT, rgb_TOP_RIGHT),
                    autoUpdate ? new vec3Vert.PosCol.AllUpdate(face.TOP_LEFT, rgb_TOP_LEFT) : new vec3Vert.PosCol.NoUpdate(face.TOP_LEFT, rgb_TOP_LEFT));
        }

        public Colored(Quadrilateral3D face, vec4.FloatVec color, boolean autoUpdate) {
            this(face, color, color, color, color, autoUpdate);
        }

        public Colored(Quadrilateral3D face, boolean autoUpdate) {
            this(face, new vec4.FloatVec(1, 1, 1, 1), autoUpdate);
        }
        
        @Override
        public void draw(VertexBuffer vertexBuffer, float scale) {
            vec3.FloatVec first = (vec3.FloatVec) this.getVertexAt(3).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec sec = (vec3.FloatVec) this.getVertexAt(1).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec normal = sec.cross(first, false);

            vertexBuffer.begin(7, OpenGLHelperEnbecko.POSITION_COLOR_NORMAL);

            for(int i = 0; i < nVertices; ++i) {
                vec3Vert.PosCol posCol = this.getVertexAt(i);
                this.putPosition(vertexBuffer, posCol.position, scale);
                this.putColor(vertexBuffer, posCol.color);
                this.putNormal(vertexBuffer, normal);
                vertexBuffer.endVertex();
            }
            System.out.println(Arrays.toString(vertexBuffer.getByteBuffer().asFloatBuffer().asReadOnlyBuffer().array()));
            Tessellator.getInstance().draw();
        }
    }
}
