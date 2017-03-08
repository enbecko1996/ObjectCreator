package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.events.EventDispatcher;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 03.03.2017.
 */
public class TE_Editor extends TileEntity implements ITickable {
    private final List<Bone> bones = new ArrayList<Bone>();
    private boolean isActive;

    public TE_Editor() {
    }

    public boolean setActive(boolean active) {
        if(this.worldObj.isRemote && this.isActive != active) {
            if(active) {
                if (Main_BlockHeroes.active_Editor_Block != null)
                    Main_BlockHeroes.active_Editor_Block.setActive(false);
                Main_BlockHeroes.active_Editor_Block = this;
            } else
                Main_BlockHeroes.active_Editor_Block = null;
            this.isActive = active;
        }
        return this.isActive;
    }

    public boolean isActive() {
        return this.isActive;
    }


    @Override
    public void update() {
        if (this.bones.size() == 0) {
            System.out.println(this.getPos());
            this.bones.add(new Bone(new vec3.IntVec(this.getPos())));
            this.bones.get(0).addContent(null);
        }

        if (this.isActive()) {
            Main_BlockHeroes.current_BlockSetMode.dispatchTick();
        }
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    public Bone getBoneAt(int pos) {
        return this.bones.get(pos);
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.setActive(compound.getBoolean("active"));
        if (this.isActive()) {
            if (Main_BlockHeroes.active_Editor_Block != null)
                Main_BlockHeroes.active_Editor_Block.setActive(false);
            Main_BlockHeroes.active_Editor_Block = this;
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("active", this.isActive);
        return super.writeToNBT(compound);
    }
}
