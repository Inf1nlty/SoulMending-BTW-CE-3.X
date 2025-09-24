package btw.community.soultotem;

import btw.BTWAddon;
import btw.AddonHandler;
import btw.util.sounds.AddonSoundRegistryEntry;
import com.inf1nlty.soultotem.STEnchantments;
import com.inf1nlty.soultotem.STConfig;
import com.inf1nlty.soultotem.block.STBlocks;
import com.inf1nlty.soultotem.client.TileEntityTotemRenderer;
import com.inf1nlty.soultotem.init.STInit;
import com.inf1nlty.soultotem.item.STItems;
import com.inf1nlty.soultotem.network.TotemCDNet;
import com.inf1nlty.soultotem.network.TotemParticleNet;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class SoulTotemAddon extends BTWAddon {

    public static final AddonSoundRegistryEntry SOUL_TOTEM_USE = new AddonSoundRegistryEntry("soultotem:totem_use");

    @Override
    public void initialize() {
        AddonHandler.logMessage(getName() + " v" + getVersionString() + " Initializing...");

        STBlocks.initSoulMendingBlocks();
        STItems.initSMItems();
        STEnchantments.registerEnchantment();

        STInit.init();
        STConfig.load();

        TotemParticleNet.register(this);
        TotemCDNet.register(this);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            TileEntityTotemRenderer.registerTotemTESR();
        }
    }
}