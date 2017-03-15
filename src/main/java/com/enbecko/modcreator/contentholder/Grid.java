package com.enbecko.modcreator.contentholder;

import com.enbecko.modcreator.Visible.IGridded;
import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Niclas on 09.03.2017.
 */
public class Grid {
    private final vec3 origin_global;
    private final HashSet<HashSet<HashSet<Content>>> grid1 = new HashSet<HashSet<HashSet<Content>>>();
    private final HashMap<Integer, HashMap<Integer, HashMap<Integer, IGridded>>> grid = new HashMap<Integer, HashMap<Integer, HashMap<Integer, IGridded>>>();

    public Grid(@Nonnull vec3 origin_global) {
        this.origin_global = origin_global;
    }

    @Nullable
    public IGridded getAt(int x, int y, int z) {
        try {
            return this.grid.get(x).get(y).get(z);
        } catch (Exception e) {}
        return null;
    }

    @Nullable
    public boolean setAt(int x, int y, int z, IGridded content) {
        HashMap<Integer, HashMap<Integer, IGridded>> tmpX;
        HashMap<Integer, IGridded> tmpY;
        if ((tmpX = this.grid.get(x)) != null) {
            if ((tmpY = tmpX.get(y)) != null) {
                if (tmpY.get(z) != null) {
                    return false;
                } else {
                    tmpY.put(z, content);
                    return true;
                }
            } else {
                tmpX.put(y, (tmpY = new HashMap<Integer, IGridded>()));
                tmpY.put(z, content);
                return true;
            }
        } else {
            this.grid.put(x, (tmpX = new HashMap<Integer, HashMap<Integer, IGridded>>()));
            tmpX.put(y, (tmpY = new HashMap<Integer, IGridded>()));
            tmpY.put(z, content);
            return true;
        }
    }

    public boolean remove(int x, int y, int z) {
        HashMap<Integer, HashMap<Integer, IGridded>> tmpX;
        HashMap<Integer, IGridded> tmpY;
        if ((tmpX = this.grid.get(x)) != null) {
            if ((tmpY = tmpX.get(y)) != null) {
                if (tmpY.get(z) != null) {
                    tmpY.remove(z);
                    if (tmpY.size() == 0) {
                        tmpX.remove(y);
                        if (tmpX.size() == 0) {
                            this.grid.remove(x);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GRID ->\n");
        for (Integer x : this.grid.keySet()) {
            for (Integer y : this.grid.get(x).keySet()) {
                for (Integer z : this.grid.get(x).get(y).keySet()) {
                    builder.append("x = " + x + ", y = " + y + ", z = " + z + ", content = " + this.grid.get(x).get(y).get(z)+"\n");
                }
            }
        }
        builder.append("<- GRID");
        return builder.toString();
    }
}
