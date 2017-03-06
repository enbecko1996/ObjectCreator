package com.enbecko.modcreator;

import com.enbecko.modcreator.linalg.RayTrace3D;

import javax.annotation.Nonnull;

/**
 * Created by Niclas on 06.03.2017.
 */
public abstract class LocalRenderSetting {
    public abstract LocalRenderSettings getType();

    public static class CrossedByRayTrace extends LocalRenderSetting{
        private final RayTrace3D theRayTrace;

        public CrossedByRayTrace(@Nonnull RayTrace3D rayTrace3D) {
            this.theRayTrace = rayTrace3D;
        }

        public RayTrace3D getTheRayTrace() {
            return this.theRayTrace;
        }

        @Override
        public LocalRenderSettings getType() {
            return LocalRenderSettings.CROSSED_BY_RAYTRACE;
        }
    }

    public enum LocalRenderSettings {
        CROSSED_BY_RAYTRACE
    }
}
