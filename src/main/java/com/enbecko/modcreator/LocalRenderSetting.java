package com.enbecko.modcreator;

import com.enbecko.modcreator.events.EditModeEnums;
import com.enbecko.modcreator.linalg.RayTrace3D;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Niclas on 06.03.2017.
 */
public abstract class LocalRenderSetting {
    public abstract LocalRenderSettings getType();

    public abstract GlobalRenderSetting.RenderOption getOption();

    public static class CrossedByRayTrace extends LocalRenderSetting{
        private RayTrace3D theRayTrace;
        private GlobalRenderSetting.RenderOption option;
        private EditModeEnums editMode;

        public CrossedByRayTrace(@Nonnull RayTrace3D rayTrace3D, @Nonnull EditModeEnums editMode) {
            this.theRayTrace = rayTrace3D;
            this.editMode = editMode;
        }

        public CrossedByRayTrace(@Nonnull RayTrace3D rayTrace3D, @Nonnull EditModeEnums editMode, @Nullable GlobalRenderSetting.RenderOption option) {
            this(rayTrace3D, editMode);
            this.option = option;
        }

        public void setOption(GlobalRenderSetting.RenderOption option) {
            this.option = option;
        }

        public RayTrace3D getTheRayTrace() {
            return this.theRayTrace;
        }

        public LocalRenderSetting setTheRayTrace(@Nonnull RayTrace3D rayTrace3D) {
            this.theRayTrace = rayTrace3D;
            return this;
        }

        @Override
        public LocalRenderSettings getType() {
            return LocalRenderSettings.CROSSED_BY_RAYTRACE;
        }

        @Override
        public GlobalRenderSetting.RenderOption getOption() {
            if (this.option != null)
                return this.option;
            return GlobalRenderSetting.markRayTracedFaceWith(this.editMode);
        }
    }

    public enum LocalRenderSettings {
        CROSSED_BY_RAYTRACE
    }

}
