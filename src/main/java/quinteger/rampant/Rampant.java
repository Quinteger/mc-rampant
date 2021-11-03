package quinteger.rampant;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quinteger.rampant.capability.DistanceScalerEventHandler;
import quinteger.rampant.config.RampantConfig;
import quinteger.rampant.setup.ModSetup;
import quinteger.rampant.util.AttributeUtils;

@Mod("rampant")
public class Rampant
{
    public static final String MODID = "rampant";

    private static final Logger LOGGER = LogManager.getLogger();

    public Rampant() {
        AttributeUtils.patchAttributes();

        LOGGER.info("Registering config file for Rampant");
        RampantConfig.register();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::onCommonSetup);

        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, DistanceScalerEventHandler::onAttachCapabilitiesEvent);
        MinecraftForge.EVENT_BUS.addListener(DistanceScalerEventHandler::onEntityJoinWorldEvent);
    }
}
