package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.Log.LogEnums;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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

    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
        Log.d(LogEnums.MINECRAFT, "Hallo" + Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    @NotNull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }



    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TE_Editor editor = (TE_Editor) worldIn.getTileEntity(pos);
        Log.d(LogEnums.MINECRAFT, editor + " is Now active: " + editor.setActive(!editor.isActive()));
        return true;
    }

    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        TE_Editor editor = (TE_Editor) world.getTileEntity(pos);
        editor.setActive(false);
        Log.d(LogEnums.MINECRAFT, editor+" is Now active: "+editor.isActive()+" && dead ");
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

}
