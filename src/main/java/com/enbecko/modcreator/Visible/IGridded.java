package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.contentholder.Grid;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Niclas on 07.03.2017.
 */
public interface IGridded {
    vec3.IntVec getPositionInGridInt();

    int getXDim();

    int getYDim();

    int getZDim();

    Grid getParentGrid();
}
