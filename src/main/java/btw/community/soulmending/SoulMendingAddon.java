package btw.community.soulmending;

import btw.BTWAddon;
import btw.AddonHandler;
import com.inf1nlty.soulmending.EnchantmentSoulMending;
import com.inf1nlty.soulmending.SoulMendingConfig;
import com.inf1nlty.soulmending.block.SoulMendingBlocks;
import com.inf1nlty.soulmending.client.TileEntityTotemRenderer;
import com.inf1nlty.soulmending.init.SMInit;
import com.inf1nlty.soulmending.item.SoulMendingItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

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
            TileEntityTotemRenderer.registerTotemTESR();
        }
    }
}