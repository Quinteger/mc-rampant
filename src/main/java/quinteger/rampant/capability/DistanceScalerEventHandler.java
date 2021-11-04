package quinteger.rampant.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import quinteger.rampant.Rampant;
import quinteger.rampant.config.RampantConfig;
import quinteger.rampant.util.EntityUtils;
import quinteger.rampant.util.MathUtils;

import java.util.UUID;

import static net.minecraft.entity.ai.attributes.Attributes.MAX_HEALTH;

/**
 * Static methods for handling distance scaling related events
 */
public class DistanceScalerEventHandler {

    /**
     * Attach our capability {@link IDistanceScaler} to new entities.<br>
     * Necessary checks are performed to short-circuit the method for non-eligible candidates.
     *
     * @param event the event handler
     */
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();

        // Check for server-side and entity validity
        if (!EntityUtils.isEligibleForDistanceScaling(entity)) return;

        DistanceScalerProvider provider = new DistanceScalerProvider();
        event.addCapability(new ResourceLocation(Rampant.MODID, "distance_scaling"), provider);
        event.addListener(provider::invalidate);
    }

    /**
     * Apply the scaler to entities once they join the world, based on the position where they appear.<br>
     * Necessary checks are performed to short-circuit the method for non-eligible candidates.<br>
     * The {@link EntityJoinWorldEvent} event is chosen over
     * {@link net.minecraftforge.event.entity.living.LivingSpawnEvent.SpecialSpawn},
     * since it accounts for all instances of monster spawning,
     * including the exotic ones like summoning the Wither.<br>
     * Respects the coordinate scale of dimension that the event takes place in.
     * For example, in the Nether distance scaling will occur 8 times faster.
     * This allows you to expect similar level of monster power when you step in/out of Nether portal.
     *
     * @param event the event handler
     */
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
        // Check for server-side and entity validity
        if (!EntityUtils.isEligibleForDistanceScaling(event.getEntity())) return;

        // Config toggle in action
        if (!RampantConfig.ACTIVE.get()) return;

        LivingEntity livingEntity = (LivingEntity) event.getEntity();

        livingEntity.getCapability(CapabilityDistanceScaler.DISTANCE_SCALER_CAPABILITY).ifPresent(scaler -> {
            // Scaler should not be already present
            if (scaler.isApplied()) return;

            double totalDistance = Math.max(Math.abs(livingEntity.getX()), Math.abs(livingEntity.getZ()));
            double coordinateScale = event.getWorld().dimensionType().coordinateScale();
            double safeDistance = RampantConfig.SAFE_DISTANCE.get() / coordinateScale;

            // Distance must be large enough
            if (totalDistance <= safeDistance) return;

            double scalingStep = RampantConfig.SCALING_STEP.get() / coordinateScale;

            ModifiableAttributeInstance attrInstance = livingEntity.getAttribute(MAX_HEALTH);

            // Null-check due to @Nullable
            if (attrInstance != null) {
                float healthMultiplier = MathUtils.calculateHealthMultiplier(totalDistance, safeDistance, scalingStep, livingEntity);

                UUID uuid = UUID.randomUUID();
                attrInstance.addPermanentModifier(new AttributeModifier(
                        uuid,
                        "Distance scaling modifier for maximum health",
                        healthMultiplier - 1,
                        Operation.MULTIPLY_TOTAL));

                // Respect entity current health percentage
                livingEntity.setHealth(livingEntity.getHealth() * healthMultiplier);

                scaler.setHealthAttributeModifier(uuid);
                scaler.setHealthMultiplier(healthMultiplier);
            }

            float damageMultiplier = MathUtils.calculateDamageMultiplier(totalDistance, safeDistance, scalingStep);
            scaler.setDamageMultiplier(damageMultiplier);

            scaler.setApplied(true);
        });
    }
}
