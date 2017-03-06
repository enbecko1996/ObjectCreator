package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec_n;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

/**
 * Created by enbec on 03.03.2017.
 */
public abstract class RenderTriangle <T extends vec3Vert> extends RenderPolygon <T> {
    public RenderTriangle(T v1, T v2, T v3) {
        super(v1, v2, v3);
    }

    public static class Textured extends RenderTriangle <vec3Vert.PosTex> {
        public Textured(vec3Vert.PosTex first, vec3Vert.PosTex sec, vec3Vert.PosTex third) {
            super(first, sec, third);
        }

        @Override
        public void draw(VertexBuffer vertexBuffer, float scale, LocalRenderSetting... localRenderSettings) {
            vec3.FloatVec first = (vec3.FloatVec) this.getVertexAt(2).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
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

    public static class Colored extends RenderTriangle <vec3Vert.PosCol> {
        public Colored(vec3Vert.PosCol first, vec3Vert.PosCol sec, vec3Vert.PosCol third) {
            super(first, sec, third);
        }

        @Override
        public void draw(VertexBuffer vertexBuffer, float scale, LocalRenderSetting... localRenderSettings) {
            vec3.FloatVec first = (vec3.FloatVec) this.getVertexAt(2).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec sec = (vec3.FloatVec) this.getVertexAt(1).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec normal = first.cross(sec, false);

            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

            for(int i = 0; i < nVertices; ++i) {
                vec3Vert.PosCol posCol = this.getVertexAt(i);
                this.putPosition(vertexBuffer, posCol.position, scale);
                this.putColor(vertexBuffer, posCol.color);
                this.putNormal(vertexBuffer, normal);
                vertexBuffer.endVertex();
            }
            Tessellator.getInstance().draw();
        }
    }
}
