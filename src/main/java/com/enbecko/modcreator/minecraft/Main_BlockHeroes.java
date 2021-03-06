package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.GlobalRenderSetting;
import com.enbecko.modcreator.GlobalSettings;
import com.enbecko.modcreator.events.BlockSetModes.BlockSetModes;
import com.enbecko.modcreator.events.BlockSetModes.BlockSetMode;
import com.enbecko.modcreator.events.EventDispatcher;
import com.enbecko.modcreator.events.RayTraceDispatcher;
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
    public static TE_Editor active_Editor_Block = null;
    public static BlockSetMode current_BlockSetMode = BlockSetModes.SINGLE_GRIDDED_MODE;

    public static BlockEditor editorBlock;
    public static ItemContent griddedCube_Item;
    public static ItemContent mcProxyCube;

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
        EventDispatcher.getTheEventDispatcher().addKeyListener(RayTraceDispatcher.getTheRayTraceDispatcher());
        EventDispatcher.getTheEventDispatcher().addMouseListener(RayTraceDispatcher.getTheRayTraceDispatcher());

        editorBlock = (BlockEditor) new BlockEditor().setUnlocalizedName("editorblock").setCreativeTab(modMakerTab);
        griddedCube_Item = (ItemContent) new ItemGriddedCube().setUnlocalizedName("gridded_cube").setCreativeTab(modMakerTab);
        mcProxyCube = (ItemContent) new ItemMcProxyCube().setUnlocalizedName("mcproxycube").setCreativeTab(modMakerTab);

        GlobalSettings.setDebugMode();

        GameRegistry.registerTileEntity(TE_Editor.class, "te_editorblock");

        GameRegistry.registerBlock(editorBlock, "editorblock");
        GameRegistry.registerItem(griddedCube_Item, "gridded_cube");
        GameRegistry.registerItem(mcProxyCube, "mcproxycube");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
