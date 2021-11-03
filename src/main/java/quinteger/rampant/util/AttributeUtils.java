package quinteger.rampant.util;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AttributeUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    private AttributeUtils() {}

    public static void patchAttributes() {
        if (!(Attributes.MAX_HEALTH instanceof RangedAttribute)) return;
        LOGGER.info("Patching maximum health value");
        ((RangedAttribute) Attributes.MAX_HEALTH).maxValue = Double.MAX_VALUE;
    }
}
