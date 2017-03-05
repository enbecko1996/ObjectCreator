package com.enbecko.modcreator.Visible;

import com.enbecko.modcreator.Polygon;

import java.util.List;

/**
 * Created by enbec on 07.01.2017.
 */
public interface ITextured <T extends Polygon> {
    List<T> getRenderPolygons();

}
