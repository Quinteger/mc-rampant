package quinteger.rampant.setup;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import quinteger.rampant.capability.CapabilityDistanceScaler;

public class ModSetup {
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CapabilityDistanceScaler.register();
    }
}
