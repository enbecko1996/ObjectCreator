package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.vec2;
import com.enbecko.modcreator.linalg.vec3;
import net.minecraft.client.model.PositionTextureVertex;

/**
 * Created by enbec on 03.03.2017.
 */
public abstract class vec3PosTexVert extends PositionTextureVertex {
    public final vec3.FloatVec position;
    public final vec2.FloatVec texture;

    protected vec3PosTexVert(vec3.FloatVec position, vec2.FloatVec texture) {
        super(position.getX(), position.getY(), position.getZ(), texture.getU(), texture.getV());
        this.position = position;
        this.texture = texture;
    }

    public static class vec3PosTexVertNoUpdate extends vec3PosTexVert {
        public vec3PosTexVertNoUpdate(vec3.FloatVec position, vec2.FloatVec texture) {
            super(new vec3.FloatVec(position), new vec2.FloatVec(texture));
        }
    }

    public static class vec3PosTexVertAutoUpdate extends vec3PosTexVert {
        public vec3PosTexVertAutoUpdate(vec3.FloatVec position, vec2.FloatVec texture) {
            super(position, texture);
        }
    }
}
