package com.inf1nlty.soultotem.block;

import com.inf1nlty.soultotem.block.tileentity.TileEntityEmptySoulTotem;
import com.inf1nlty.soultotem.block.tileentity.TileEntitySoulTotem;
import com.inf1nlty.soultotem.item.EmptySoulTotemItem;
import com.inf1nlty.soultotem.item.SoulTotemItem;
import net.minecraft.src.Item;
import net.minecraft.src.TileEntity;

public class STBlocks {
    public static BlockSoulTotem soulTotem;
    public static Item soulTotemItem;

    public static BlockEmptySoulTotem emptySoulTotem;
    public static Item emptySoulTotemItem;

    public static void initSoulMendingBlocks() {

        soulTotem = new BlockSoulTotem(3902);
        soulTotemItem = new SoulTotemItem(3903);
        TileEntity.addMapping(TileEntitySoulTotem.class, "soultotem:soul_totem");

        emptySoulTotem = new BlockEmptySoulTotem(3904);
        emptySoulTotemItem = new EmptySoulTotemItem(3905);
        TileEntity.addMapping(TileEntityEmptySoulTotem.class, "soultotem:empty_soul_totem");

    }
}