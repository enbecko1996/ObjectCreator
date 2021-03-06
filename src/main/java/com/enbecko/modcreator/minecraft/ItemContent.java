package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.events.BlockSetModes.BlockSetMode;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.item.Item;

/**
 * Created by Niclas on 08.03.2017.
 */
public abstract class ItemContent extends Item {
    public abstract Content createNewContentAndReturn(Bone parentBone, vec3 positionInBoneCoords, ContentOption option);

    public abstract boolean canBeSetHere(vec3 pos, BlockSetMode mode);
}
