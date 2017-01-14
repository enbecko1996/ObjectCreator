package com.enbecko.modcreator.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enbec on 07.01.2017.
 */
public class SearchTree {
    public CoordinatedList<CoordinatedList<ArrayList<Block3D>>> blocks = new CoordinatedList<CoordinatedList<ArrayList<Block3D>>>();

    public boolean setBlock(int x_P, int y_P, int z_P) {
        for (int x = 0; x < this.blocks.size(); x++) {
            CoordinatedList xList = this.blocks.get(x);
            if (xList.coordinate == x) {
                for (int y = 0; y < xList.size(); y++) {
                    CoordinatedList yList = xList.get(y);
                    if (yList.coordinate == y) {
                        for (int z = 0; z < yList.size(); z++) {
                            if(yList)
                        }
                    }
                }
            }
        }
    }

    private static final class CoordinatedList <E> extends ArrayList{
        final int coordinate;

        CoordinatedList(int coordinate) {
            this.coordinate = coordinate;
        }

        @SuppressWarnings("unchecked")
        public E get(int index) {
            return (E) super.get(index);
        }

    }
}
