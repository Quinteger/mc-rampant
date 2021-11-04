package quinteger.rampant.util;

import net.minecraft.entity.LivingEntity;
import quinteger.rampant.config.RampantConfig;

public class MathUtils {

    private MathUtils() {}

    public static float calculateHealthMultiplier(double totalDistance, double safeDistance, double scalingStep, LivingEntity livingEntity) {
        float result;
        double intermediate;
        switch (RampantConfig.SCALING_METHOD.get()) {
            case LINEAR: default:
                intermediate = 1 + ((totalDistance - safeDistance) / scalingStep)
                        * (RampantConfig.HEALTH_SCALING_FACTOR.get() - 1);
                break;
            case EXPONENT:
                intermediate = Math.pow(RampantConfig.HEALTH_SCALING_FACTOR.get(),
                        (totalDistance - safeDistance) / scalingStep);
                break;
        }
        result = (float) Math.min(
                Math.min(
                        intermediate,
                        RampantConfig.MAX_SCALED_HEALTH.get() / livingEntity.getMaxHealth()),
                RampantConfig.MAX_HEALTH_MULTIPLIER.get());
        return result;
    }

    public static float calculateDamageMultiplier(double totalDistance, double safeDistance, double scalingStep) {
        float result;
        double intermediate;
        switch (RampantConfig.SCALING_METHOD.get()) {
            case LINEAR: default:
                intermediate = 1 + ((totalDistance - safeDistance) / scalingStep)
                        * (RampantConfig.DAMAGE_SCALING_FACTOR.get() - 1);
                break;
            case EXPONENT:
                intermediate = Math.pow(RampantConfig.DAMAGE_SCALING_FACTOR.get(),
                        (totalDistance - safeDistance) / scalingStep);
                break;
        }
        result = (float) Math.min(intermediate, RampantConfig.MAX_DAMAGE_MULTIPLIER.get());
        return result;
    }
}
