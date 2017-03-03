package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec_n;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by enbec on 03.03.2017.
 */
public class TexturedPolygon extends TexturedQuad{
    private final vec3PosTexVert[] vertexPositions;
    private final int nVertices;
    private boolean invertNormal;

    public TexturedPolygon(vec3PosTexVert[] textureVertices) {
        super(textureVertices);
        this.vertexPositions = textureVertices;
        this.nVertices = textureVertices.length;
    }

    @SideOnly(Side.CLIENT)
    public void draw(VertexBuffer vertexBuffer, float scale) {
        vec3.FloatVec first = (vec3.FloatVec) this.vertexPositions[0].position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.vertexPositions[1].position, false);
        vec3.FloatVec sec = (vec3.FloatVec) this.vertexPositions[2].position.subAndMakeNew(vec_n.vecPrec.FLOAT, this.vertexPositions[1].position, false);
        vec3.FloatVec cross = (vec3.FloatVec) first.cross(sec, false).normalize();
        float f = cross.getX();
        float f1 = cross.getY();
        float f2 = cross.getZ();
        if(this.invertNormal) {
            f = -f;
            f1 = -f1;
            f2 = -f2;
        }

        vertexBuffer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);

        for(int i = 0; i < 4; ++i) {
            vec3PosTexVert posTexVert = this.vertexPositions[i];
            vertexBuffer.pos(posTexVert.position.getX() * (double)scale, posTexVert.position.getY() * (double)scale, posTexVert.position.getZ() * (double)scale).tex(posTexVert.texture.getU(), posTexVert.texture.getV()).normal(f, f1, f2).endVertex();
        }

        Tessellator.getInstance().draw();
    }
}
