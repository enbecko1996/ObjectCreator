package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.LocalRenderSetting;
import com.enbecko.modcreator.Log;
import com.enbecko.modcreator.Log.LogEnums;
import com.enbecko.modcreator.Visible.Gridded_CUBE;
import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.contentholder.RayTraceResult;
import com.enbecko.modcreator.events.BlockSetModes.BlockSetMode;
import com.enbecko.modcreator.linalg.RayTrace3D;
import com.enbecko.modcreator.linalg.vec3;
import com.sun.istack.internal.Nullable;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 03.03.2017.
 */
public class TE_Editor extends TileEntity implements ITickable {
    private final List<Bone> bones = new ArrayList<Bone>();
    private boolean isActive;
    private vec3.IntVec posInWorld;
    protected final List<Bone> rayTraceResult = new ArrayList<Bone>();
    protected double[] distance;

    public TE_Editor() {
    }

    public boolean setActive(boolean active) {
        if (this.worldObj.isRemote && this.isActive != active) {
            if (active) {
                if (Main_BlockHeroes.active_Editor_Block != null)
                    Main_BlockHeroes.active_Editor_Block.setActive(false);
                Main_BlockHeroes.active_Editor_Block = this;
            } else
                Main_BlockHeroes.active_Editor_Block = null;
            this.isActive = active;
        }
        return this.isActive;
    }

    public RayTraceResult getRayTraceResult(RayTrace3D rayTrace_GLOBAL, BlockSetMode editMode) {
       // return this.getBoneAt(0).getRayTraceResult(rayTrace_GLOBAL, editMode);
        this.rayTraceResult.clear();
        for (int l = 0; l < distance.length; l++) {
            if (distance[l] != 0)
                distance[l] = 0;
            else
                break;
        }
        vec3 pos;
        for (Bone bone : this.bones) {
            if (bone.isInside(rayTrace_GLOBAL.getOnPoint())) {
                this.rayTraceResult.add(0, bone);
                double tmp = distance[0];
                for (int l = 1; l < distance.length; l++) {
                    if (l > 0 && distance[l - 1] != 0) {
                        double tt = distance[l];
                        distance[l] = tmp;
                        tmp = tt;
                    } else
                        break;
                }
            }
            if ((pos = bone.checkIfCrosses(rayTrace_GLOBAL)) != null) {
                double d = pos.subFromThis(rayTrace_GLOBAL.getOnPoint()).length();
                int k = 0;
                for (; k < distance.length; k++) {
                    if (distance[k] == 0 || distance[k] > d) {
                        if (distance[k] != 0) {
                            double tmp = distance[k];
                            for (int l = k + 1; l < distance.length; l++) {
                                if (l > 0 && distance[l - 1] != 0) {
                                    double tt = distance[l];
                                    distance[l] = tmp;
                                    tmp = tt;
                                } else
                                    break;
                            }
                        }
                        distance[k] = d;
                        break;
                    }
                }
                this.rayTraceResult.add(k, bone);
            }
        }

        for (Bone bone : this.rayTraceResult) {
            RayTraceResult result;
            if ((result = bone.getRayTraceResult(rayTrace_GLOBAL, editMode)) != null)
                return result;
        }
        return null;
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
            this.addBone(new vec3.IntVec(this.getPos()));
            this.bones.get(0).addContent(new Gridded_CUBE(this.getBoneAt(0), this.getBoneAt(0).getBoneGrid(), new vec3.IntVec(-2, 0, 0), 1).createBoundingGeometry());
        }

        if (this.isActive()) {
            Main_BlockHeroes.current_BlockSetMode.dispatchTick();
        }
    }

    public void addBone(vec3 position) {
        this.bones.add(new Bone(position));
        this.distance = new double[this.bones.size()];
    }

    public void renderBones(VertexBuffer buffer, LocalRenderSetting... localRenderSettings) {
        for (Bone bone : this.bones) {
            bone.render(buffer, localRenderSettings);
        }
    }

    public void renderBonesWithExceptions(VertexBuffer buffer, @Nullable List<Content> exceptions, LocalRenderSetting... localRenderSettings) {
        for (Bone bone : this.bones) {
            bone.renderContentWithExceptions(buffer, exceptions, localRenderSettings);
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
            if ((tmp = (int) bone.getMaxPos3InWorld().subFromThis(this.posInWorld).length()) > tmpLen)
                tmpLen = tmp;
            if ((tmp = (int) bone.getMinPos3InWorld().subFromThis(this.posInWorld).length()) > tmpLen)
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
