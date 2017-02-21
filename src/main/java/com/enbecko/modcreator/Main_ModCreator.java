package com.enbecko.modcreator;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Main_ModCreator.MODID, version = Main_ModCreator.VERSION)
public class Main_ModCreator
{
    public static final String MODID = "modcreator";
    public static final String VERSION = "1.0";

    public static int maxBlockSize = 16;
    public static int contentCubesPerCube;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
}
