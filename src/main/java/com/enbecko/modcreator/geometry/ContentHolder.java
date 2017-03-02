package com.enbecko.modcreator.geometry;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public interface ContentHolder <T extends Content>{
    public List<T> getContent();

    public abstract boolean addContent(@Nonnull T content);

    public abstract boolean removeContent(@Nonnull T content);
}
