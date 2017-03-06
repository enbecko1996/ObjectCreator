package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 03.03.2017.
 */
public class TE_Editor extends TileEntity implements ITickable{
    private final List<Bone> bones = new ArrayList<Bone>();

    public TE_Editor() {
    }

    @Override
    public void update() {
        if (this.bones.size() == 0) {
            System.out.println(this.getPos());
            this.bones.add(new Bone(new vec3.IntVec(this.getPos())));
            this.bones.get(0).addContent(null);
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
}
