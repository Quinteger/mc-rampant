package quinteger.rampant.capability;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.UUID;

public interface IDistanceScaler {

    /**
     * Used to mark the scaler as active. If set to true, the entity will not be scaled further by
     * {@link DistanceScalerEventHandler#onEntityJoinWorldEvent(EntityJoinWorldEvent)}
     * and damage multiplier will start to fuction.
     *
     * @param value whether this scaler capability is ready and should be applied.
     * @see #setApplied(boolean) 
     */
    void setApplied(boolean value);

    /**
     * Returns the state of this scaler.
     * 
     * @return whether the scaler is applied.
     * @see #setApplied(boolean) 
     */
    boolean isApplied();

    /**
     * Stores the UUID of {@link net.minecraft.entity.ai.attributes.AttributeModifier},
     * which scales the health of the entity owning this capability.
     *
     * @param uuid the UUID to store
     * @see #getHealthAttributeModifier()
     */
    void setHealthAttributeModifier(UUID uuid);

    /**
     * Returns the UUID of {@link net.minecraft.entity.ai.attributes.AttributeModifier},
     * which scales the health of the entity owning this capability.
     *
     * @return the modifier's UUID
     * @see #setHealthAttributeModifier(UUID) 
     */
    UUID getHealthAttributeModifier();

    /**
     * Sets the health multiplier for the entity owning this capability.
     * 
     * @param value the multiplier
     * @see #getHealthMultiplier() 
     */
    void setHealthMultiplier(float value);

    /**
     * Returns the health multiplier for the entity owning this capability.
     * 
     * @return the multiplier
     * @see #setHealthMultiplier(float) 
     */
    float getHealthMultiplier();

    /**
     * Sets the damage multiplier for the entity owning this capability.
     *
     * @param value the multiplier
     * @see #getDamageMultiplier()
     */
    void setDamageMultiplier(float value);

    /**
     * Returns the damage multiplier for the entity owning this capability.
     *
     * @return the multiplier
     * @see #setDamageMultiplier(float)
     */
    float getDamageMultiplier();
}
