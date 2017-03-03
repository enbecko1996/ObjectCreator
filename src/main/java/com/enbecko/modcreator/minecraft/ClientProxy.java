package com.enbecko.modcreator.minecraft;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by enbec on 03.03.2017.
 */
public class ClientProxy extends CommonProxy {
    public static String modid = Main_BlockHeroes.MODID;

    @Override
    public void preInit(FMLPreInitializationEvent e) {

    }

    @Override
    public void init(FMLInitializationEvent e) {
        this.bindRenderers();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        this.bindItemModel(Main_BlockHeroes.editorBlock);
    }

    private void bindRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TE_Editor.class, new Render_EditorBlock());
    }

    private void bindItemModel(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }
}
