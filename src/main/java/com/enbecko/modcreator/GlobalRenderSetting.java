package com.enbecko.modcreator;

import com.enbecko.modcreator.events.EditModeEnums;
import com.enbecko.modcreator.linalg.vec4;
import com.sun.org.apache.regexp.internal.RE;

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

    public static RenderOption markRayTracedFaceWith(EditModeEnums editMode) {
        switch (editMode) {
            case BUILD:
                return RenderOption.OVERLAY_GREEN;
            case REMOVE:
                return RenderOption.OVERLAY_RED;
            default:
                return markRayTracedFace;
        }
    }

    public enum RenderMode {
        DEBUG, LIVE;
    }

    public enum RenderOption {
        OUTLINE, OVERLAY_RED {
            private final vec4.FloatVec red = (vec4.FloatVec) new vec4.FloatVec(1, 0, 0, 1).normalize().mulToThis(overlayStrength).addToThis(1).normalize();
            public vec4.FloatVec getColor() {
                return red;
            }
        }, OVERLAY_GREEN {
            private final vec4.FloatVec green = (vec4.FloatVec) new vec4.FloatVec(0, 1, 0, 1).normalize().mulToThis(overlayStrength).addToThis(1).normalize();
            public vec4.FloatVec getColor() {
                return green;
            }
        }, OVERLAY_CUSTOM {
            private final vec4.FloatVec color = (vec4.FloatVec) new vec4.FloatVec(1, 1, 1, 1).normalize().mulToThis(overlayStrength).addToThis(1).normalize();
            public vec4.FloatVec getColor() {
                return color;
            }

            public void setColor(float red, float green, float blue) {
                color.update(red, green, blue, 1).normalize().mulToThis(overlayStrength).addToThis(1).normalize();
            }
        }
    }
}
