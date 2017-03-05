package com.enbecko.modcreator;

import javax.annotation.Nonnull;

/**
 * Created by Niclas on 05.03.2017.
 */
public class GlobalRenderSetting {
    private RenderMode mode = RenderMode.LIVE;

    public GlobalRenderSetting putRenderMode(@Nonnull RenderMode mode) {
        this.mode = mode;
        return this;
    }

    public RenderMode getRenderMode() {
        return this.mode;
    }

    public enum RenderMode {
        DEBUG, LIVE;
    }
}
