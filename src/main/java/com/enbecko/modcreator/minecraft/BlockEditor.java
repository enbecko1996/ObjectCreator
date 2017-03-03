package com.enbecko.modcreator.minecraft;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * Created by enbec on 03.03.2017.
 */
public class BlockEditor extends BlockContainer {
    protected BlockEditor(Material p_i45386_1_) {
        super(p_i45386_1_);
    }

    @NotNull
    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int side) {
        return null;
    }
}
