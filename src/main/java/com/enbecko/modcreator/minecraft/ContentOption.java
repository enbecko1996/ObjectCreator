package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.linalg.vec4;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

/**
 * Created by Niclas on 08.03.2017.
 */
public abstract class ContentOption {
    @Nullable
    public static ContentOption newContentOptionFromNBT(NBTTagCompound compound) {
        String optionStr;
        if ((optionStr = compound.getString("option")).length() > 0) {
            try {
                ContentOptions option = ContentOptions.valueOf(optionStr);
                switch (option) {
                    case GRIDDEDCUBE:
                        return new GriddedCube(compound.getInteger("size"), compound.getByteArray("color"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public enum ContentOptions {
        GRIDDEDCUBE;
    }

    public static class GriddedCube extends ContentOption{
        private final int size;
        private final vec4.FloatVec color;

        public GriddedCube(int size, vec4.FloatVec color) {
            this.size = size;
            this.color = color;
        }

        public int getSize() {
            return this.size;
        }

        public vec4.FloatVec getColor() {
            return this.color;
        }
    }
}
