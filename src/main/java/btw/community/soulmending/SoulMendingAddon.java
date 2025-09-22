package btw.community.soulmending;

import btw.BTWAddon;
import btw.AddonHandler;
import com.inf1nlty.soulmending.EnchantmentSoulMending;
import com.inf1nlty.soulmending.SoulMendingConfig;
import com.inf1nlty.soulmending.block.SoulMendingBlocks;
import com.inf1nlty.soulmending.block.tileentity.TileEntitySoulTotem;
import com.inf1nlty.soulmending.client.TileEntitySoulTotemRenderer;
import com.inf1nlty.soulmending.init.SMInit;
import com.inf1nlty.soulmending.item.SoulMendingItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.src.TileEntityRenderer;

public class SoulMendingAddon extends BTWAddon {
    @Override
    public void initialize() {
        AddonHandler.logMessage(getName() + " v" + getVersionString() + " Initializing...");

        SoulMendingBlocks.initSoulMendingBlocks();
        SoulMendingItems.initSMItems();
        EnchantmentSoulMending.registerEnchantment();

        SMInit.init();
        SoulMendingConfig.load();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            registerClientTESR();
        }
    }

    @SuppressWarnings("unchecked")
    public void registerClientTESR() {
        TileEntityRenderer.instance.specialRendererMap.put(TileEntitySoulTotem.class, new TileEntitySoulTotemRenderer());
    }
}