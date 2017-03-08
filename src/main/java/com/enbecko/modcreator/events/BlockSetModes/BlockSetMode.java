package com.enbecko.modcreator.events.BlockSetModes;

import com.enbecko.modcreator.contentholder.RayTraceResult;
import com.enbecko.modcreator.linalg.vec3;
import com.enbecko.modcreator.minecraft.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.enbecko.modcreator.events.BlockSetModes.BlockSetMode.BlockSetModeEvent.*;

/**
 * Created by Niclas on 08.03.2017.
 */
public abstract class BlockSetMode {
    protected final vec3.IntVec[] positions;
    private final vec3.IntVec FAIL = new vec3.IntVec();
    private final List<BlockSetModeEvent> downs = new ArrayList<BlockSetModeEvent>();
    private final int mode;

    BlockSetMode(int mode, vec3.IntVec... positions) {
        this.positions = positions;
        this.mode = mode;
    }

    public boolean setResult(int pos, vec3.IntVec result) {
        if (pos < this.positions.length) {
            this.positions[pos] = result;
            return true;
        }
        return false;
    }

    public abstract BlockSetEnum getEnum();

    public abstract void clicked(BlockSetModeEvent event, RayTraceResult result, ItemStack toSet);

    public abstract void dispatchMouseMoved(MouseEvent event, RayTraceResult result, ItemStack toSet);

    public abstract void released(BlockSetModeEvent event, RayTraceResult result, ItemStack toSet);

    public abstract void tickDowns(List<BlockSetModeEvent> downs);

    @Nullable
    public BlockSetModeEvent dispatchMouseClickedReleased(MouseEvent event, RayTraceResult result, ItemStack toSet) {
        switch (event.getButton()) {
            case 0:
                if (event.isButtonstate()) {
                    this.dispatchClicked(MOUSE_LEFT, result, toSet);
                } else {
                    this.dispatchReleased(MOUSE_LEFT, result, toSet);
                }
                return MOUSE_LEFT;
            case 1:
                if (event.isButtonstate()) {
                    this.dispatchClicked(MOUSE_RIGHT, result, toSet);
                } else {
                    this.dispatchReleased(MOUSE_RIGHT, result, toSet);
                }
                return MOUSE_RIGHT;
            case 2:
                if (event.isButtonstate()) {
                    this.dispatchClicked(MOUSE_MIDDLE, result, toSet);
                } else {
                    this.dispatchReleased(MOUSE_MIDDLE, result, toSet);
                }
                return MOUSE_MIDDLE;
        }
        return null;
    }

    @Nullable
    public BlockSetModeEvent dispatchKeyEvent(InputEvent.KeyInputEvent event, RayTraceResult result, ItemStack toSet) {
        if (ClientProxy.KEY_X.isKeyDown()) {
            if (!KEY_X.isDown) {
                return this.dispatchClicked(KEY_X, result, toSet);
            }
        } else {
            if (KEY_X.isDown) {
                return this.dispatchReleased(KEY_X, result, toSet);
            }
        }

        if (ClientProxy.KEY_Y.isKeyDown()) {
            if (!KEY_Y.isDown) {
                return this.dispatchClicked(KEY_Y, result, toSet);
            }
        } else {
            if (KEY_Y.isDown) {
                return this.dispatchReleased(KEY_Y, result, toSet);
            }
        }

        if (ClientProxy.KEY_Z.isKeyDown()) {
            if (!KEY_Z.isDown) {
                return this.dispatchClicked(KEY_Z, result, toSet);
            }
        } else {
            if (KEY_Z.isDown) {
                return this.dispatchReleased(KEY_Z, result, toSet);
            }
        }
        return null;
    }

    public BlockSetModeEvent dispatchClicked(BlockSetModeEvent event, RayTraceResult result, ItemStack toSet) {
        event.lastClick = System.currentTimeMillis();
        event.deltaClicked = 0;
        event.isDown = true;
        if (!this.downs.contains(event) && event.tickDown())
            this.downs.add(event);
        this.clicked(event, result, toSet);
        return event;
    }

    public BlockSetModeEvent dispatchReleased(BlockSetModeEvent event, RayTraceResult result, ItemStack toSet) {
        event.isDown = false;
        if (this.downs.contains(event))
            this.downs.remove(event);
        event.deltaClicked = System.currentTimeMillis() - event.lastClick;
        this.released(event, result, toSet);
        return event;
    }

    @Nullable
    public vec3.IntVec getResult(int pos) {
        if (pos < positions.length)
            return positions[pos];
        return FAIL;
    }

    public boolean isSet(int pos) {
        vec3.IntVec position;
        position = this.getResult(pos);
        return (position != null && position != FAIL);
    }

    public void setupNew() {
        for (int k = 0; k < this.positions.length; k++)
            this.positions[k] = null;
    }

    public void dispatchTick() {
        this.tickDowns(this.downs);
    }

    public enum BlockSetModeEvent {
        MOUSE_LEFT, MOUSE_RIGHT, MOUSE_MIDDLE, KEY_X, KEY_Y, KEY_Z;

        long lastClick, deltaClicked;
        boolean isDown, tickDown = true;

        public long getLastClick() {
            return this.lastClick;
        }

        public long getDeltaClicked() {
            return this.deltaClicked;
        }

        public boolean isDown() {
            return this.isDown;
        }

        public boolean tickDown() {
            return this.tickDown;
        }

        public String toString() {
            return "{setModeEvent: " + this.name() + ", down:" + this.isDown + ", last:" + this.lastClick + ", delta:" + this.deltaClicked + "}";
        }
    }

}
