package com.enbecko.modcreator.minecraft;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * Created by enbec on 03.03.2017.
 */
public class BlockEditor extends BlockContainer {
    protected BlockEditor() {
        super(Material.ANVIL);
    }

    @NotNull
    @Override
    public TileEntity createNewTileEntity(@NotNull World world, int side) {
        return new TE_Editor();
    }

    @Override
    public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_) {
        return false;
    }

    @NotNull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
