package com.enbecko.modcreator;

import com.enbecko.modcreator.events.BlockSetModes.BlockSetMode;
import com.enbecko.modcreator.linalg.vec4;

import javax.annotation.Nonnull;

/**
 * Created by Niclas on 05.03.2017.
 */
public class GlobalRenderSetting {
    private static RenderMode renderMode = RenderMode.LIVE;
    private static RenderOption markRayTracedFace = RenderOption.OUTLINE;
    private static final double overlayStrength = 0.4;

    public static void putRenderMode(@Nonnull RenderMode mode) {
        renderMode = mode;
    }

    public static RenderMode getRenderMode() {
        return renderMode;
    }

    public static RenderOption markRayTracedFaceWith(BlockSetMode editMode) {
        return RenderOption.OVERLAY_GREEN;
    }

    public enum RenderMode {
        DEBUG, LIVE;
    }

    public enum RenderOption {
        OUTLINE {
            private final vec4.FloatVec color = (vec4.FloatVec) new vec4.FloatVec(1, 1, 1, 1).normalize().mulToThis(overlayStrength).addToThis(1).normalize();
            public vec4.FloatVec getColor() {
                return color;
            }

            public void setColor(float red, float green, float blue) {
                color.update(red, green, blue, 1).normalize().mulToThis(overlayStrength).addToThis(1).normalize();
            }
        }, OVERLAY_RED {
            private final vec4.FloatVec red = (vec4.FloatVec) new vec4.FloatVec(1, 0, 0, 0.44F);
            public vec4.FloatVec getColor() {
                return red;
            }
        }, OVERLAY_GREEN {
            private final vec4.FloatVec green = (vec4.FloatVec) new vec4.FloatVec(0.3F, 1, 0.3F, 0.44F);
            public vec4.FloatVec getColor() {
                return green;
            }
        }, OVERLAY_CUSTOM {
            private final vec4.FloatVec color = (vec4.FloatVec) new vec4.FloatVec(1, 1, 1, 0.44F);
            public vec4.FloatVec getColor() {
                return color;
            }

            public void setColor(float red, float green, float blue) {
                color.update(red, green, blue, 1).normalize().mulToThis(overlayStrength).addToThis(1).normalize();
            }
        };

        public abstract vec4.FloatVec getColor();
    }
}
