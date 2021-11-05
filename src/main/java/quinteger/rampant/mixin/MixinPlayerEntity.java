package quinteger.rampant.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import quinteger.rampant.config.RampantConfig;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
    private MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * This mixin limits the amount of damage that a shield can take in a single hit.<br>
     * It is necessary due to an unfortunate bug in {@link PlayerEntity#hurtCurrentlyUsedShield(float)}.
     * If the float value passed in that method is floored to {@link Integer#MAX_VALUE},
     * following increment by 1 makes it overflow and do no damage to the shield.
     *
     * @param capture damage value captured at the point of injection
     * @return adjusted value
     */
    @ModifyVariable(method = "hurtCurrentlyUsedShield(F)V", at = @At("HEAD"), argsOnly = true, ordinal = 0, require = 1)
    private float onHurtCurrentlyUsedShieldModifyDamage(float capture) {
        return Math.min(capture, RampantConfig.MAX_DAMAGE_TO_SHIELD.get().floatValue() - 1);
    }
}
