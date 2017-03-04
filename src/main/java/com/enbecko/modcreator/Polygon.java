package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by enbec on 03.03.2017.
 */
public abstract class Polygon <T extends vec3Vert> {
    private final T[] vertexPositions;
    int nVertices;
    protected boolean invertNormals;

    public Polygon(T ... textureVertices) {
        this.vertexPositions = textureVertices;
        this.nVertices = textureVertices.length;
    }

    @SideOnly(Side.CLIENT)
    public abstract void draw(VertexBuffer vertexBuffer, float scale);

    public T getVertexAt(int pos) {
        return this.vertexPositions[pos];
    }

    public void putNormal(VertexBuffer vertexBuffer, vec3.FloatVec normal) {
        if (this.invertNormals)
            normal.invert();
        vertexBuffer.normal(normal.getX(), normal.getY(), normal.getZ());
    }

    public void putPosition(VertexBuffer vertexBuffer, vec3 position, float scale) {
        vertexBuffer.pos(position.getXF() * scale, position.getYF() * scale, position.getZF() * scale);
    }

    public void putColor(VertexBuffer vertexBuffer, vec4.FloatVec color) {
        vertexBuffer.color(color.getR(), color.getG(), color.getB(), color.getA());
    }

    public void putTexture(VertexBuffer vertexBuffer, vec2.FloatVec texture) {
        vertexBuffer.tex(texture.getU(), texture.getV());
    }

    /**
     * Created by enbec on 03.03.2017.
     */
    public static class Textured extends Polygon <vec3Vert.PosTexNorm> {
        private boolean invertNormal;

        public Textured(vec3Vert.PosTexNorm ... vertices) {
            super(vertices);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void draw(VertexBuffer vertexBuffer, float scale) {
            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);

            for(int i = 0; i < nVertices; ++i) {
                vec3Vert.PosTexNorm posTexNorm = this.getVertexAt(i);
                this.putPosition(vertexBuffer, posTexNorm.position, scale);
                this.putTexture(vertexBuffer, posTexNorm.texture);
                this.putNormal(vertexBuffer, posTexNorm.normal);
                vertexBuffer.endVertex();
            }
            Tessellator.getInstance().draw();
        }
    }

    /**
     * Created by enbec on 03.03.2017.
     */
    public static class Colored extends Polygon <vec3Vert.PosColNorm> {
        private boolean invertNormal;

        public Colored(vec3Vert.PosColNorm ... vertices) {
            super(vertices);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void draw(VertexBuffer vertexBuffer, float scale) {
            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

            for(int i = 0; i < nVertices; ++i) {
                vec3Vert.PosColNorm posColNorm = this.getVertexAt(i);
                this.putPosition(vertexBuffer, posColNorm.position, scale);
                this.putColor(vertexBuffer, posColNorm.color);
                this.putNormal(vertexBuffer, posColNorm.normal);
                vertexBuffer.endVertex();
            }
            Tessellator.getInstance().draw();
        }
    }
}
