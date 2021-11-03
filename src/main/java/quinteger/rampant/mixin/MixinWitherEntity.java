package quinteger.rampant.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import quinteger.rampant.capability.CapabilityDistanceScaler;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(WitherEntity.class)
public abstract class MixinWitherEntity extends MonsterEntity {
    private MixinWitherEntity(EntityType<? extends MonsterEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * This mixin aims to scale Withers' health regeneration rate together with their health.<br>
     * Withers spawn with 1/3 of their health and slowly regenerate to full during
     * their initial invulnerability phase. They also regenerate health passively.
     * However, both heals are called with absolute values and therefore should be scaled
     * accordingly.<br>
     * This mixin is used over {@link net.minecraftforge.event.entity.living.LivingHealEvent}
     * in order to be able to separate these instances of healing from others.
     *
     * @param capture healing value passed into the method call
     * @return modified value to be passed into the method call
     */
    @ModifyArg(method = "customServerAiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/boss/WitherEntity;heal(F)V"), require = 2)
    private float onHealScaleAmount(float capture) {
        AtomicReference<Float> resultReference = new AtomicReference<>(capture);

        this.getCapability(CapabilityDistanceScaler.DISTANCE_SCALER_CAPABILITY).ifPresent(scaler -> {
            if (!scaler.isApplied()) return;
            resultReference.set(capture * scaler.getHealthMultiplier());
        });

        return resultReference.get();
    }
}
