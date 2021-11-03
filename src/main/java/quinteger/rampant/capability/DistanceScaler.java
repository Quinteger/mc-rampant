package quinteger.rampant.capability;

import lombok.ToString;

import javax.annotation.Nullable;
import java.util.UUID;

@ToString
public class DistanceScaler implements IDistanceScaler {
    private boolean applied = false;
    private UUID healthAttributeModifier;
    private float healthMultiplier = 1F;
    private float damageMultiplier = 1F;

    @Override
    public void setApplied(boolean value) {
        applied = value;
    }

    @Override
    public boolean isApplied() {
        return applied;
    }

    @Override
    public void setHealthAttributeModifier(UUID uuid) {
        this.healthAttributeModifier = uuid;
    }

    @Nullable
    @Override
    public UUID getHealthAttributeModifier() {
        return healthAttributeModifier;
    }

    @Override
    public void setHealthMultiplier(float value) {
        this.healthMultiplier = value;
    }

    @Override
    public float getHealthMultiplier() {
        return healthMultiplier;
    }

    @Override
    public void setDamageMultiplier(float value) {
        this.damageMultiplier = value;
    }

    @Override
    public float getDamageMultiplier() {
        return damageMultiplier;
    }
}
