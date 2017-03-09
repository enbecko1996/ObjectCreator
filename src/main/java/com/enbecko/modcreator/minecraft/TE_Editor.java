package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.Log.LogEnums;
import com.enbecko.modcreator.Visible.Gridded_CUBE;
import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.events.EventDispatcher;
import com.enbecko.modcreator.linalg.MathHelper;
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
    private vec3.IntVec posInWorld;

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
        if (this.posInWorld == null) {
            this.posInWorld = new vec3.IntVec(this.pos.getX(), this.pos.getY(), this.pos.getZ());
        }
        if (this.bones.size() == 0) {
            Log.d(LogEnums.MINECRAFT, this.getPos());
            this.bones.add(new Bone(new vec3.IntVec(this.getPos())));
            this.bones.get(0).addContent(new Gridded_CUBE(this.getBoneAt(0), new vec3.IntVec(-2, 0, 0), 1).createBoundingGeometry());
        }

        if (this.isActive()) {
            Main_BlockHeroes.current_BlockSetMode.dispatchTick();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return Math.pow(this.getMaxSize(), 2) + super.getMaxRenderDistanceSquared();
    }

    public int getMaxSize() {
        int tmpLen = Integer.MIN_VALUE;
        int tmp;
        for (Bone bone : this.bones) {
            if ((tmp =  (int) bone.getMaxPos3InWorld().subFromThis(this.posInWorld).length()) > tmpLen)
                tmpLen = tmp;
            if ((tmp =  (int) bone.getMinPos3InWorld().subFromThis(this.posInWorld).length()) > tmpLen)
                tmpLen = tmp;
        }
        return tmpLen;
    }

    public Bone getBoneAt(int pos) {
        return this.bones.get(pos);
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.isActive = compound.getBoolean("active");
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
