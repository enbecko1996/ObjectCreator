package com.enbecko.modcreator.events;

import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * Created by Niclas on 14.11.2016.
 */
public interface IKeyListener {

    public void onKeyEvent(InputEvent.KeyInputEvent event);

}
