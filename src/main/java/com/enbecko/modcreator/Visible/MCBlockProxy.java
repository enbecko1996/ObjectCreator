package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.RenderQuadrilateral;
import com.enbecko.modcreator.contentholder.Bone;
import com.enbecko.modcreator.contentholder.Content;
import com.enbecko.modcreator.events.ManipulatingEvent;
import com.enbecko.modcreator.linalg.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

/**
 * Created by Niclas on 07.03.2017.
 */
public class MCBlockProxy extends Content.CubicContent {
    private final IBlockState blockState;

    protected MCBlockProxy(Bone parentBone, vec3.IntVec positonInBoneCoords, IBlockState state) {
        super(parentBone, positonInBoneCoords, 8, vec_n.vecPrec.INT);
        this.blockState = state;
    }

    @Override
    public void manipulateMe(ManipulatingEvent event, RayTrace3D rayTrace3D) {

    }

    @Override
    public MCBlockProxy createBoundingGeometry() {
        this.makeHexahedralEdgesAndFacesNoUpdate();
        return this;
    }
}
