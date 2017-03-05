package com.enbecko.modcreator.geometry;

import com.enbecko.modcreator.linalg.vec3;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by enbec on 21.02.2017.
 */
public interface ContentHolder <T extends Content>{
    int getContentCount();

    List<T> getContent();

    /**
     * This method is passing down the Content to Add and in which FirstOrderHolder
     * The ContentHolder with the last Child who's not containing the vec is responsible
     * for adding the new Childs.
     * @param decisiveVec position of the first order Holder where this should be added.
     * @param toAdd Content to add
     * @return true if added, false if not.
     */
    boolean addContent(@Nonnull vec3 decisiveVec, @Nonnull Content toAdd);

    boolean addNewChild(@Nonnull T content);

    boolean removeChild(@Nonnull T content);

    void askForOrderDegrade(@Nonnull T asker, CubicContentHolderGeometry degradeTo);
}
