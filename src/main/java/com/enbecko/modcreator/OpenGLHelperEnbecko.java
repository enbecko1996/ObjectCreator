package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by enbec on 03.03.2017.
 */
public class OpenGLHelperEnbecko {
    //LINE
    public static void drawLine(vec3 start, vec3 end, float red, float green, float blue, float width) {
        GlStateManager.glLineWidth(width);
        GL11.glColor3f(red, green, blue);
        GlStateManager.glBegin(GL11.GL_LINES);
        GlStateManager.glVertex3f(start.getXF(), start.getYF(), start.getZF());
        GlStateManager.glVertex3f(end.getXF(), end.getYF(), end.getZF());
        GlStateManager.glEnd();
    }

    public static void drawLine(vec3 start, vec3 end, vec3 color, float width) {
        OpenGLHelperEnbecko.drawLine(start, end, color.getXF(), color.getYF(), color.getZF(), width);
    }

    public static void drawLine(vec3 start, vec3 end, vec3 color) {
        OpenGLHelperEnbecko.drawLine(start, end, color.getXF(), color.getYF(), color.getZF(), 2);
    }

    public static void drawLine(Line3D line, float red, float green, float blue, float width) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), red, green, blue, width);
    }

    public static void drawLine(Line3D line, float width) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), 1, 1, 1, width);
    }

    public static void drawLine(Line3D line, float red, float green, float blue) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), red, green, blue, 2);
    }

    public static void drawLine(Line3D line, vec3 color, float width) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), color.getXF(), color.getYF(), color.getZF(), width);
    }

    public static void drawLine(Line3D line, vec3 color) {
        OpenGLHelperEnbecko.drawLine(line.getOnPoint(), line.getEndPoint(), color.getXF(), color.getYF(), color.getZF(), 2);
    }

    //LINES
    public static void drawLines(List<Line3D> lines, float red, float green, float blue, float width) {
        for (Line3D line : lines)
            drawLine(line, red, green, blue, width);
    }

    public static void drawLines(List<Line3D> lines, vec3 color, float width) {
        drawLines(lines, color.getXF(), color.getYF(), color.getZF(), width);
    }

    public static void drawLines(List<Line3D> lines, float width) {
        drawLines(lines, 1, 1, 1, width);
    }

    public static void drawLines(List<Line3D> lines, vec3 color) {
        drawLines(lines, color.getXF(), color.getYF(), color.getZF(), 2);
    }

    public static void drawLines(List<Line3D> lines, float red, float green, float blue) {
        drawLines(lines, red, green, blue, 2);
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
