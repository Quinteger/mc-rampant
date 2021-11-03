package quinteger.rampant.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import quinteger.rampant.capability.CapabilityDistanceScaler;
import quinteger.rampant.util.EntityUtils;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    private MixinLivingEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    /**
     * A mixin which applies damage scaling provided by {@link quinteger.rampant.capability.IDistanceScaler}.<br>
     * Fired at the beginning of {@link LivingEntity#hurt(DamageSource, float)}.<br>
     * After some consideration, a decision was made to use a mixin instead of
     * {@link net.minecraftforge.event.entity.living.LivingHurtEvent}. Unfortunately, modifying
     * the damage during that event does not affect the damage done to player's shield,
     * which devalues shield enchanting.<br>
     * It would be desirable to instead modify damage when
     * {@link net.minecraftforge.event.entity.living.LivingAttackEvent} is fired,
     * but that's currently not possible. When/if it becomes possible, this mixin will become redundant.
     *
     * @param capture damage value captured at the point of injection
     * @param damageSource source of damage passed into {@link LivingEntity#hurt(DamageSource, float)}
     * @param value damage value passed into {@link LivingEntity#hurt(DamageSource, float)}
     * @return modified damage value
     */
    @ModifyVariable(method = "hurt(Lnet/minecraft/util/DamageSource;F)Z", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 1)
    private float onHurtModifyDamage(float capture, DamageSource damageSource, float value) {
        Entity entity = damageSource.getEntity();

        // Check for server-side and entity validity
        if (!EntityUtils.isEligibleForDistanceScaling(entity)) return capture;

        AtomicReference<Float> resultReference = new AtomicReference<>(capture);

        entity.getCapability(CapabilityDistanceScaler.DISTANCE_SCALER_CAPABILITY).ifPresent(scaler -> {
            if (!scaler.isApplied()) return;
            resultReference.set(value * scaler.getDamageMultiplier());
        });

        return resultReference.get();
    }
}
