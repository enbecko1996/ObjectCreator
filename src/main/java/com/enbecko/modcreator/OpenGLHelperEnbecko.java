package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec4;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by enbec on 03.03.2017.
 */
public class OpenGLHelperEnbecko {
    public static final VertexFormat POSITION_COLOR_NORMAL = new VertexFormat();

    public static final vec4.FloatVec RED = (vec4.FloatVec) new vec4.FloatVec(1, 0, 0, 1).setChangeable(false);
    public static final vec4.FloatVec GREEN = (vec4.FloatVec) new vec4.FloatVec(0, 1, 0, 1).setChangeable(false);
    public static final vec4.FloatVec BLUE = (vec4.FloatVec) new vec4.FloatVec(0, 0, 1, 1).setChangeable(false);
    public static final vec4.FloatVec BLACK = (vec4.FloatVec) new vec4.FloatVec(0, 0, 0, 1).setChangeable(false);
    public static final vec4.FloatVec WHITE = (vec4.FloatVec) new vec4.FloatVec(1, 1, 1, 1).setChangeable(false);

    static {
        POSITION_COLOR_NORMAL.addElement(DefaultVertexFormats.POSITION_3F);
        POSITION_COLOR_NORMAL.addElement(DefaultVertexFormats.COLOR_4UB);
        POSITION_COLOR_NORMAL.addElement(DefaultVertexFormats.NORMAL_3B);
        POSITION_COLOR_NORMAL.addElement(DefaultVertexFormats.PADDING_1B);
    }

    //LINE
    @SideOnly(Side.CLIENT)
    public static void drawLine(vec3 start, vec3 end, float red, float green, float blue, float alpha, float width) {
        GlStateManager.glLineWidth(width);
        GlStateManager.color(red, green, blue, alpha);
        GlStateManager.glBegin(GL11.GL_LINES);
        GlStateManager.glVertex3f(start.getXF(), start.getYF(), start.getZF());
        GlStateManager.glVertex3f(end.getXF(), end.getYF(), end.getZF());
        GlStateManager.glEnd();
    }

    @SideOnly(Side.CLIENT)
    public static void drawLine(vec3 start, vec3 end, vec4.FloatVec color, float width) {
        OpenGLHelperEnbecko.drawLine(start, end, color.getR(), color.getG(), color.getB(), color.getA(), width);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLine(vec3 start, vec3 end, vec4.FloatVec color) {
        OpenGLHelperEnbecko.drawLine(start, end, color.getR(), color.getG(), color.getB(), color.getA(), 3);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLine(Line3D line, float red, float green, float blue, float alpha, float width) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), red, green, blue, alpha, width);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLine(Line3D line, float width) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), 1, 1, 1, 1, width);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLine(Line3D line, float red, float green, float blue, float alpha) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), red, green, blue, alpha, 2);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLine(Line3D line, vec4.FloatVec color, float width) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), color.getR(), color.getG(), color.getB(), color.getA(), width);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLine(Line3D line, vec4.FloatVec color) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), color.getR(), color.getG(), color.getB(), color.getA(), 2);
    }

    //LINES
    @SideOnly(Side.CLIENT)
    public static void drawLines(List<Line3D> lines, float red, float green, float blue, float alpha, float width) {
        for (Line3D line : lines)
            drawLine(line, red, green, blue, alpha, width);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLines(List<Line3D> lines, vec4.FloatVec color, float width) {
        drawLines(lines, color.getR(), color.getG(), color.getB(), color.getA(), width);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLines(List<Line3D> lines, float width) {
        drawLines(lines, 1, 1, 1, 1, width);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLines(List<Line3D> lines, vec4.FloatVec color) {
        drawLines(lines, color.getR(), color.getG(), color.getB(), color.getA(), 2);
    }

    @SideOnly(Side.CLIENT)
    public static void drawLines(List<Line3D> lines, float red, float green, float blue, float alpha) {
        drawLines(lines, red, green, blue, alpha, 2);
    }

    /**
     * VERTEXBUFFER!!!!!!
     */
    @SideOnly(Side.CLIENT)
    private int drawToBuffer(float scale, List<TexturedQuad> quads) {
         int displayList = GLAllocation.generateDisplayLists(1);
         GlStateManager.glNewList(displayList, 4864);
         VertexBuffer vertexbuffer = Tessellator.getInstance().getBuffer();

         for (TexturedQuad quad : quads)
             quad.draw(vertexbuffer, scale);

         GlStateManager.glEndList();
         return displayList;
    }
}
