package com.inf1nlty.soulmending.block;

import com.inf1nlty.soulmending.block.tileentity.TileEntityEmptySoulTotem;
import com.inf1nlty.soulmending.block.tileentity.TileEntitySoulTotem;
import com.inf1nlty.soulmending.item.EmptySoulTotemItem;
import com.inf1nlty.soulmending.item.SoulTotemItem;
import net.minecraft.src.Item;
import net.minecraft.src.TileEntity;

public class SoulMendingBlocks {
    public static BlockSoulTotem soulTotem;
    public static Item soulTotemItem;

    public static BlockEmptySoulTotem emptySoulTotem;
    public static Item emptySoulTotemItem;

    public static void initSoulMendingBlocks() {

        soulTotem = new BlockSoulTotem(3702);
        soulTotemItem = new SoulTotemItem(3703);
        TileEntity.addMapping(TileEntitySoulTotem.class, "soulmending:soul_totem");

        emptySoulTotem = new BlockEmptySoulTotem(3704);
        emptySoulTotemItem = new EmptySoulTotemItem(3705);
        TileEntity.addMapping(TileEntityEmptySoulTotem.class, "soulmending:empty_soul_totem");

    }
}