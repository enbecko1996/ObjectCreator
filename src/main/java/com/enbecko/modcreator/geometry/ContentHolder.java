package com.enbecko.modcreator.geometry;

import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public interface ContentHolder <T extends Content>{
    public List<T> getContent();
}
