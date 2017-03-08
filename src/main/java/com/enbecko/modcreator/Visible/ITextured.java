package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.RenderPolygon;

import java.util.List;

/**
 * Created by enbec on 07.01.2017.
 */
public interface ITextured <T extends RenderPolygon> {
    List<T> getRenderPolygons();

}
