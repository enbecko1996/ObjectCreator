package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.vec2;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.linalg.vec4;
import net.minecraft.client.model.PositionTextureVertex;

import javax.annotation.Nullable;

/**
 * Created by enbec on 03.03.2017.
 */
public abstract class vec3Vert {
    public final vec3 position;

    protected vec3Vert(@Nullable vec3 position) {
        this.position = position;
    }

    public static abstract class PosTex extends vec3Vert{
        public final vec2.FloatVec texture;

        public PosTex(vec3 position, vec2.FloatVec texture) {
            super(position);
            this.texture = texture;
        }

        public static class NoUpdate extends PosTex {

            public NoUpdate(vec3 position, vec2.FloatVec texture) {
                super(new vec3.FloatVec(position), new vec2.FloatVec(texture));
            }
        }

        public static class PosUpdate extends PosTex {
            public PosUpdate(vec3 position, vec2.FloatVec texture) {
                super(position, new vec2.FloatVec(texture));
            }
        }

        public static class TexUpdate extends PosTex {
            public TexUpdate(vec3 position, vec2.FloatVec texture) {
                super(new vec3.FloatVec(position), texture);
            }
        }

        public static class AllUpdate extends PosTex {
            public AllUpdate(vec3 position, vec2.FloatVec texture) {
                super(position, texture);
            }
        }
    }

    public static abstract class PosTexNorm extends PosTex{
        public final vec3.FloatVec normal;

        public PosTexNorm(vec3 position, vec2.FloatVec texture, vec3.FloatVec normal) {
            super(position, texture);
            this.normal = normal;
        }

        public static class NoUpdate extends PosTexNorm {
            public NoUpdate(vec3 position, vec2.FloatVec texture, vec3.FloatVec normal) {
                super(new vec3.FloatVec(position), new vec2.FloatVec(texture), new vec3.FloatVec(normal));
            }
        }

        public static class PosUpdate extends PosTexNorm {
            public PosUpdate(vec3 position, vec2.FloatVec texture, vec3.FloatVec normal) {
                super(position, new vec2.FloatVec(texture), new vec3.FloatVec(normal));
            }
        }

        public static class TexUpdate extends PosTexNorm {
            public TexUpdate(vec3 position, vec2.FloatVec texture, vec3.FloatVec normal) {
                super(new vec3.FloatVec(position), texture, new vec3.FloatVec(normal));
            }
        }

        public static class NormUpdate extends PosTexNorm {
            public NormUpdate(vec3 position, vec2.FloatVec texture, vec3.FloatVec normal) {
                super(new vec3.FloatVec(position), new vec2.FloatVec(texture), normal);
            }
        }

        public static class AllUpdate extends PosTexNorm {
            public AllUpdate(vec3 position, vec2.FloatVec texture, vec3.FloatVec normal) {
                super(position, texture, normal);
            }
        }
    }

    public static abstract class PosCol extends vec3Vert{
        public final vec4.FloatVec color;

        public PosCol(vec3 position, vec4.FloatVec color) {
            super(position);
            this.color = color;
        }

        public static class NoUpdate extends PosCol {

            public NoUpdate(vec3 position, vec4.FloatVec color) {
                super(new vec3.FloatVec(position), new vec4.FloatVec(color));
            }
        }

        public static class PosUpdate extends PosCol {
            public PosUpdate(vec3 position, vec4.FloatVec color) {
                super(position, new vec4.FloatVec(color));
            }
        }

        public static class ColUpdate extends PosCol {
            public ColUpdate(vec3 position, vec4.FloatVec color) {
                super(new vec3.FloatVec(position), color);
            }
        }

        public static class AllUpdate extends PosCol {
            public AllUpdate(vec3 position, vec4.FloatVec color) {
                super(position, color);
            }
        }
    }

    public static abstract class PosColNorm extends PosCol{
        public final vec3.FloatVec normal;

        public PosColNorm(vec3 position, vec4.FloatVec color, vec3.FloatVec normal) {
            super(position, color);
            this.normal = normal;
        }

        public static class NoUpdate extends PosColNorm {
            public NoUpdate(vec3 position, vec4.FloatVec color, vec3.FloatVec normal) {
                super(new vec3.FloatVec(position), new vec4.FloatVec(color), new vec3.FloatVec(normal));
            }
        }

        public static class PosUpdate extends PosColNorm {
            public PosUpdate(vec3 position, vec4.FloatVec color, vec3.FloatVec normal) {
                super(position, new vec4.FloatVec(color), new vec3.FloatVec(normal));
            }
        }

        public static class TexUpdate extends PosColNorm {
            public TexUpdate(vec3 position, vec4.FloatVec color, vec3.FloatVec normal) {
                super(new vec3.FloatVec(position), color, new vec3.FloatVec(normal));
            }
        }

        public static class NormUpdate extends PosColNorm {
            public NormUpdate(vec3 position, vec4.FloatVec color, vec3.FloatVec normal) {
                super(new vec3.FloatVec(position), new vec4.FloatVec(color), normal);
            }
        }

        public static class AllUpdate extends PosColNorm {
            public AllUpdate(vec3 position, vec4.FloatVec color, vec3.FloatVec normal) {
                super(position, color, normal);
            }
        }
    }
}
