package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.GlobalRenderSetting;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.jetbrains.annotations.NotNull;

@Mod(modid = Main_BlockHeroes.MODID, version = Main_BlockHeroes.VERSION)
public class Main_BlockHeroes
{
    public static final String MODID = "modcreator";
    public static final String VERSION = "1.0";
    @SidedProxy(clientSide="com.enbecko.modcreator.minecraft.ClientProxy", serverSide="com.enbecko.modcreator.minecraft.ServerProxy")
    public static CommonProxy proxy;

    public static int contentCubesPerCube = 2;

    public static BlockEditor editorBlock;

    private static final CreativeTabs modMakerTab = new CreativeTabs("Enbecko's Mod Maker") {
        @NotNull
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(editorBlock);
        }
    };
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        editorBlock = (BlockEditor) new BlockEditor().setUnlocalizedName("editorblock").setCreativeTab(modMakerTab);
        GlobalRenderSetting.putRenderMode(GlobalRenderSetting.RenderMode.DEBUG);

        GameRegistry.registerTileEntity(TE_Editor.class, "te_editorblock");

        GameRegistry.registerBlock(editorBlock, "editorblock");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
