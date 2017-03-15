package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.Visible.Gridded_CUBE;
import com.enbecko.modcreator.Visible.MCBlockProxy;
import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.events.BlockSetModes.BlockSetMode;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.minecraft.ContentOption.GriddedCube;
import net.minecraft.init.Blocks;

/**
 * Created by Niclas on 08.03.2017.
 */
public class ItemMcProxyCube extends ItemContent implements IGriddable {
    @Override
    public Content createNewContentAndReturn(Bone parentBone, vec3 positionInBoneCoords, ContentOption option) {
         return new MCBlockProxy(parentBone, new vec3.IntVec(positionInBoneCoords), Blocks.BEACON.getDefaultState());
    }

    @Override
    public boolean canBeSetHere(vec3 pos, BlockSetMode mode) {
        switch (mode.getEnum()) {
            case SINGLE_GRIDDED:
                if (pos instanceof vec3.IntVec || (pos.getXD() == (int)pos.getXD() && pos.getYD() == (int)pos.getYD() && pos.getZD() == (int)pos.getZD())) {
                    return true;
                }
        }
        return false;
    }
}
