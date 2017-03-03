package com.enbecko.modcreator.minecraft;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * Created by enbec on 03.03.2017.
 */
public class TE_Editor extends TileEntity implements ITickable{

    @Override
    public void update() {
        System.out.println("tick");
    }
}
