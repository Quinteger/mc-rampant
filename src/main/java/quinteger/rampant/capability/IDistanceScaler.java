package quinteger.rampant.capability;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.UUID;

public interface IDistanceScaler {

    /**
     * Used to mark the scaler as active.<br>
     * New entities are created with this flag set to {@code false}.
     * It serves as a signal for {@link DistanceScalerEventHandler#onEntityJoinWorldEvent(EntityJoinWorldEvent)}.
     * The handler calculates multipliers and sets this flag to {@code true},
     * which causes the entity to be skipped during further firings of this event
     * (for example, if the entity gets transported) and enables the damage multiplier.
     *
     * @param value whether this scaler capability is ready and should be applied.
     * @see #isApplied()
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
     * that performs health scaling on behalf of this capability.
     *
     * @param uuid the UUID to store
     * @see #getHealthAttributeModifier()
     */
    void setHealthAttributeModifier(UUID uuid);

    /**
     * Returns the UUID of {@link net.minecraft.entity.ai.attributes.AttributeModifier},
     * that performs health scaling on behalf of this capability.
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
