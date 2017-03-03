package com.enbecko.modcreator.minecraft;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Main_BlockHeroes.MODID, version = Main_BlockHeroes.VERSION)
public class Main_BlockHeroes
{
    public static final String MODID = "modcreator";
    public static final String VERSION = "1.0";

    public static int maxBlockSize = 16;
    public static int contentCubesPerCube = 4;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
}
