package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.Face3D;
import com.enbecko.modcreator.linalg.Line3D;
import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;

/**
 * Created by enbec on 21.02.2017.
 */
public abstract class Content {
    protected final vec3[] cornersInBoneCoords;
    protected Line3D[] edgesInBoneCoords;
    protected Face3D[] facesInBoneCoords;
    final vec3 positionInBoneCoords;
    @Nonnull
    private final Bone parentBone;

    public Content(Bone parentBone, vec3 positionInBoneCoords, int size) {
        this.positionInBoneCoords = new vec3.IntVec(positionInBoneCoords, false).setChangeable(false);
        this.parentBone = parentBone;
        this.cornersInBoneCoords = new vec3[size];
    }

    @Nonnull
    public Bone getParentBone() {
        return this.parentBone;
    }

    public vec3 getPositionInBoneCoords() {
        return this.positionInBoneCoords;
    }

    public vec3 getCorner(int pos) {
        return this.cornersInBoneCoords[pos];
    }

    public abstract boolean isColliding(Content other);

    public abstract boolean isFullInside(Content other);

    public abstract boolean isInside(vec3 vec);

    public abstract void init();

    public int getCornerCount() {
        return this.cornersInBoneCoords.length;
    }

}
