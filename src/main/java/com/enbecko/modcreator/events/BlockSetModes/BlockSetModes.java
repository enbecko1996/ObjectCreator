package com.enbecko.modcreator.events.BlockSetModes;

/**
 * Created by Niclas on 06.03.2017.
 */
public class BlockSetModes {
    public static final int SINGLE_GRIDDED = 0, STRAIGHT_LINE = 1;
    public static final BlockSetMode SINGLE_GRIDDED_MODE = new SINGLE_GRIDDED_MODE();
    public static final BlockSetMode LINE_GRIDDED_MODE = new SINGLE_GRIDDED_MODE();
    public static final BlockSetMode FACE_GRIDDED_MODE = new SINGLE_GRIDDED_MODE();
    //public static final BlockSetMode SINGLE_GRIDDED_MODE = new SINGLE_GRIDDED_MODE();
    //public static final BlockSetMode STRAIGHT_LINE_MODE = new BlockSetMode(STRAIGHT_LINE, null, null);

    public BlockSetModes() {

    }

}

