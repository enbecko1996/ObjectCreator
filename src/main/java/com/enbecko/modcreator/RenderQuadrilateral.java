package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

import static com.enbecko.modcreator.GlobalRenderSetting.RenderOption.OUTLINE;
import static com.enbecko.modcreator.GlobalRenderSetting.RenderOption.OVERLAY_GREEN;

/**
 * Created by enbec on 03.03.2017.
 */
public abstract class RenderQuadrilateral <T extends vec3Vert> extends RenderPolygon <T> {
    public RenderQuadrilateral(T v1, T v2, T v3, T v4) {
        super(v1, v2, v3, v4);
    }
    /**
     * TODO
     */
    public static class Textured extends RenderQuadrilateral <vec3Vert.PosTex> {
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
        public void draw(VertexBuffer vertexBuffer, float scale, LocalRenderSetting... localRenderSettings) {
            vec3.FloatVec first = (vec3.FloatVec) this.getVertexAt(3).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec sec = (vec3.FloatVec) this.getVertexAt(1).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec normal = first.cross(sec, false);

            if (localRenderSettings.length > 0) {
                for (LocalRenderSetting setting : localRenderSettings) {
                    switch (setting.getOption()) {
                        case OUTLINE:
                            OpenGLHelperEnbecko.drawLine(this.getVertexAt(0).position, this.getVertexAt(1).position, GlobalRenderSetting.RenderOption.OUTLINE.getColor(), 5);
                            OpenGLHelperEnbecko.drawLine(this.getVertexAt(1).position, this.getVertexAt(2).position, GlobalRenderSetting.RenderOption.OUTLINE.getColor(), 5);
                            OpenGLHelperEnbecko.drawLine(this.getVertexAt(2).position, this.getVertexAt(3).position, GlobalRenderSetting.RenderOption.OUTLINE.getColor(), 5);
                            OpenGLHelperEnbecko.drawLine(this.getVertexAt(3).position, this.getVertexAt(0).position, GlobalRenderSetting.RenderOption.OUTLINE.getColor(), 5);
                            break;
                        case OVERLAY_GREEN:
                            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
                            vec4.FloatVec col = OVERLAY_GREEN.getColor();
                            GlStateManager.color(col.getR(), col.getG(), col.getB(), 0.44F);
                            GL11.glEnable(GL11.GL_BLEND);
                            GL11.glDisable(GL11.GL_LIGHTING);
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            for (int i = 0; i < nVertices; ++i) {
                                vec3Vert.PosTex posTex = this.getVertexAt(i);
                                this.putPosition(vertexBuffer, posTex.position, scale);
                                this.putNormal(vertexBuffer, normal);
                                vertexBuffer.endVertex();
                            }
                            Tessellator.getInstance().draw();
                            GL11.glDisable(GL11.GL_BLEND);
                            GL11.glEnable(GL11.GL_LIGHTING);
                            break;
                    }
                }
            } else {
                vertexBuffer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
                this.drawVerts(vertexBuffer, normal, scale);
            }
        }

        public void drawVerts(VertexBuffer vertexBuffer, vec3.FloatVec normal, float scale) {
            for (int i = 0; i < nVertices; ++i) {
                vec3Vert.PosTex posTex = this.getVertexAt(i);
                this.putPosition(vertexBuffer, posTex.position, scale);
                this.putTexture(vertexBuffer, posTex.texture);
                this.putNormal(vertexBuffer, normal);
                vertexBuffer.endVertex();
            }
            Tessellator.getInstance().draw();
        }
    }

    public static class Colored extends RenderQuadrilateral <vec3Vert.PosCol> {
        public Colored(vec3Vert.PosCol LOW_LEFT, vec3Vert.PosCol LOW_RIGHT, vec3Vert.PosCol TOP_RIGHT, vec3Vert.PosCol TOP_LEFT) {
            super(LOW_LEFT, LOW_RIGHT, TOP_RIGHT, TOP_LEFT);
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
        public void draw(VertexBuffer vertexBuffer, float scale, LocalRenderSetting... localRenderSettings) {
            vec3.FloatVec first = (vec3.FloatVec) this.getVertexAt(3).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec sec = (vec3.FloatVec) this.getVertexAt(1).position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.getVertexAt(0).position,false);
            vec3.FloatVec normal = sec.cross(first, false);

            if (localRenderSettings.length > 0) {
                for (LocalRenderSetting setting : localRenderSettings) {
                    switch (setting.getOption()) {
                        case OUTLINE:
                            OpenGLHelperEnbecko.drawLine(this.getVertexAt(0).position, this.getVertexAt(1).position, GlobalRenderSetting.RenderOption.OUTLINE.getColor(), 5);
                            OpenGLHelperEnbecko.drawLine(this.getVertexAt(1).position, this.getVertexAt(2).position, GlobalRenderSetting.RenderOption.OUTLINE.getColor(), 5);
                            OpenGLHelperEnbecko.drawLine(this.getVertexAt(2).position, this.getVertexAt(3).position, GlobalRenderSetting.RenderOption.OUTLINE.getColor(), 5);
                            OpenGLHelperEnbecko.drawLine(this.getVertexAt(3).position, this.getVertexAt(0).position, GlobalRenderSetting.RenderOption.OUTLINE.getColor(), 5);
                            break;
                        case OVERLAY_GREEN:
                            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_NORMAL);
                            vec4.FloatVec col = OVERLAY_GREEN.getColor();
                            GlStateManager.color(col.getR(), col.getG(), col.getB(), 0.44F);
                            GL11.glEnable(GL11.GL_BLEND);
                            GL11.glDisable(GL11.GL_LIGHTING);
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            for (int i = 0; i < nVertices; ++i) {
                                vec3Vert.PosCol posCol = this.getVertexAt(i);
                                this.putPosition(vertexBuffer, posCol.position, scale);
                                this.putNormal(vertexBuffer, normal);
                                vertexBuffer.endVertex();
                            }
                            Tessellator.getInstance().draw();
                            GL11.glDisable(GL11.GL_BLEND);
                            GL11.glEnable(GL11.GL_LIGHTING);
                            break;
                    }
                }
            } else {
                vertexBuffer.begin(7, OpenGLHelperEnbecko.POSITION_COLOR_NORMAL);
                this.drawVerts(vertexBuffer, normal, scale);
            }
        }

        public void drawVerts(VertexBuffer vertexBuffer, vec3.FloatVec normal, float scale) {
            for (int i = 0; i < nVertices; ++i) {
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
