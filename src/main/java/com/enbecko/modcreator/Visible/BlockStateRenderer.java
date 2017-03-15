package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Niclas on 15.03.2017.
 */
public class BlockStateRenderer {
    private static final HashMap<IBlockState, List<TexturedQuad>> quadsForState = new HashMap<IBlockState, List<TexturedQuad>>();

    public static boolean registerBlockState(IBlockState state) {
        if (!quadsForState.containsKey(state)) {
            BlockModelShapes blockModelShapes = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
            EnumBlockRenderType enumblockrendertype = state.getRenderType();
            switch (enumblockrendertype)
            {
                case MODEL:
                    IBakedModel model = blockModelShapes.getModelForState(state);
                    List<TexturedQuad> quadList = new ArrayList<TexturedQuad>();
                    for (EnumFacing enumfacing : EnumFacing.values()) {
                        List<BakedQuad> list = model.getQuads(state, enumfacing, 0); // MathHelper.getCoordinateRandom(global.getX(), global.getY(), global.getZ()));
                        if (!list.isEmpty()) { // && (!checkSides || blockState.shouldSideBeRendered(worldIn, posIn, enumfacing))){
                            for (BakedQuad quad : list) {
                                fillQuadBounds(quad.getVertexData(), quad.getFormat(), quadList);
                            }
                        }
                    }
                    List<BakedQuad> list = model.getQuads(state, null, 0); // MathHelper.getCoordinateRandom(global.getX(), global.getY(), global.getZ()));
                    if (!list.isEmpty()) { // && (!checkSides || blockState.shouldSideBeRendered(worldIn, posIn, enumfacing))){
                        for (BakedQuad quad : list) {
                            fillQuadBounds(quad.getVertexData(), quad.getFormat(), quadList);
                        }
                    }
                    quadsForState.put(state, quadList);
                    break;
            }
            return true;
        }
        return false;
    }

    @Nullable
    public static List<TexturedQuad> getQuadsForState(IBlockState state) {
        if (quadsForState.containsKey(state))
            return quadsForState.get(state);
        return null;
    }

    public static boolean isStateRegistered(IBlockState state) {
        return quadsForState.containsKey(state);
    }

    private static void fillQuadBounds(int[] vertexData, VertexFormat format, List<TexturedQuad> toFill) {
        PositionTextureVertex[] positionTextureVertices = new PositionTextureVertex[4];
        float[][][] tst = new float[4][format.getElementCount()][];
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < format.getElementCount(); e++) {
                tst[v][e] = new float[format.getElement(e).getElementCount()];
                LightUtil.unpack(vertexData, tst[v][e], format, v, e);
            }
            positionTextureVertices[v] = new PositionTextureVertex(tst[v][0][0], tst[v][0][1], tst[v][0][2], tst[v][2][0], tst[v][2][1]);
        }
        toFill.add(new TexturedQuad(positionTextureVertices));
    }
}
