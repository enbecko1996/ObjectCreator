package com.enbecko.modcreator.minecraft;

import com.enbecko.modcreator.events.EventDispatcher;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created by enbec on 03.03.2017.
 */
public class ClientProxy extends CommonProxy {
    public static String modid = Main_BlockHeroes.MODID;
    public static KeyBinding KEY_X, KEY_Y, KEY_Z;

    @Override
    public void preInit(FMLPreInitializationEvent e) {

    }

    @Override
    public void init(FMLInitializationEvent e) {
        this.bindRenderers();
        this.registerKeyBindings();
        Minecraft mc = Minecraft.getMinecraft();
        mc.entityRenderer = new CustomEntityRenderer(mc, mc.getResourceManager());
        MinecraftForge.EVENT_BUS.register(EventDispatcher.getTheEventDispatcher());
    }

    public void registerKeyBindings() {
        KEY_X = new KeyBinding("key.x", Keyboard.KEY_X, "key.categories.MercenaryMod");
        KEY_Y = new KeyBinding("key.y", Keyboard.KEY_Y, "key.categories.MercenaryMod");
        KEY_Z = new KeyBinding("key.Z", Keyboard.KEY_Z, "key.categories.MercenaryMod");
        ClientRegistry.registerKeyBinding(KEY_X);
        ClientRegistry.registerKeyBinding(KEY_Y);
        ClientRegistry.registerKeyBinding(KEY_Z);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        this.bindItemModel(Main_BlockHeroes.editorBlock);
    }

    private void bindRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TE_Editor.class, new Render_EditorBlock());
    }

    private void bindItemModel(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(modid + ":" + block.getUnlocalizedName().substring(5), "inventory"));
    }
}
