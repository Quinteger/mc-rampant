package quinteger.rampant.util;

import net.minecraft.entity.LivingEntity;
import quinteger.rampant.config.RampantConfig;

public class MathUtils {

    private MathUtils() {}

    public static float calculateHealthMultiplier(double totalDistance, double safeDistance, double scalingStep, LivingEntity livingEntity) {
        return (float) Math.min(
                Math.min(
                        calculateRawHealthMultiplier(totalDistance, safeDistance, scalingStep),
                        RampantConfig.MAX_SCALED_HEALTH.get() / livingEntity.getMaxHealth()),
                RampantConfig.MAX_HEALTH_MULTIPLIER.get());
    }

    private static double calculateRawHealthMultiplier(double totalDistance, double safeDistance, double scalingStep) {
        switch (RampantConfig.SCALING_METHOD.get()) {
            case LINEAR: default:
                return 1 + ((totalDistance - safeDistance) / scalingStep)
                        * (RampantConfig.HEALTH_SCALING_FACTOR.get() - 1);
            case EXPONENT:
                return Math.pow(RampantConfig.HEALTH_SCALING_FACTOR.get(),
                        (totalDistance - safeDistance) / scalingStep);
        }
    }

    public static float calculateDamageMultiplier(double totalDistance, double safeDistance, double scalingStep) {
        return (float) Math.min(
                calculateRawDamageMultiplier(totalDistance, safeDistance, scalingStep),
                RampantConfig.MAX_DAMAGE_MULTIPLIER.get());
    }

    private static double calculateRawDamageMultiplier(double totalDistance, double safeDistance, double scalingStep) {
        switch (RampantConfig.SCALING_METHOD.get()) {
            case LINEAR: default:
                return 1 + ((totalDistance - safeDistance) / scalingStep)
                        * (RampantConfig.DAMAGE_SCALING_FACTOR.get() - 1);
            case EXPONENT:
                return Math.pow(RampantConfig.DAMAGE_SCALING_FACTOR.get(),
                        (totalDistance - safeDistance) / scalingStep);
        }
    }
}
