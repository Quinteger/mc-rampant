package quinteger.rampant.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.ResourceLocation;
import quinteger.rampant.config.RampantConfig;

import java.util.List;

public class EntityUtils {

    private EntityUtils() {}

    public static boolean isHostileMob(Entity entity) {
        return entity instanceof IMob;
    }

    /**
     * The all-in-one method to check for applicability of distance-scaling events to a given {@code Entity}.
     *
     * @param entity entity to be evaluated
     * @return whether it fits the necessary requirements
     */
    public static boolean isEligibleForDistanceScaling(Entity entity) {
        return (entity != null
                // Server-side only
                && !entity.level.isClientSide
                // Cull invalid entities
                && entity instanceof LivingEntity);
    }

    /**
     * A more advanced version of {@link #isEligibleForDistanceScaling(Entity)} that reads configs.<br>
     * Intended to only be fired at capability application.
     *
     * @param entity entity to be evaluated
     * @return whether it fits the necessary requirements
     */
    public static boolean isEligibleForDistanceScalerApplication(Entity entity) {
        return (isEligibleForDistanceScaling(entity))
                && (isHostileMob(entity) || isForcelisted(entity))
                && isWhitelisted(entity)
                && !isBlacklisted(entity);
    }

    /**
     * Performs a blacklist config check for a given entity.
     *
     * @param entity the entity to check
     * @return true if blacklist config is enabled and the entity is blacklisted, false otherwise
     */
    private static boolean isBlacklisted(Entity entity) {
        return RampantConfig.BLACKLIST_ENABLED.get() && isInList(entity, RampantConfig.BLACKLIST.get());
    }

    /**
     * Performs a whitelist config check for a given entity.
     *
     * @param entity the entity to check
     * @return true if either whitelist config is disabled or the entity is whitelisted, false otherwise
     */
    private static boolean isWhitelisted(Entity entity) {
        return !RampantConfig.WHITELIST_ENABLED.get() || isInList(entity, RampantConfig.WHITELIST.get());
    }

    /**
     * Performs a forcelist config check for a given entity.
     *
     * @param entity the entity to check
     * @return true if forcelist config is enabled and the entity is in forcelisted, false otherwise
     */
    private static boolean isForcelisted(Entity entity) {
        return RampantConfig.FORCELIST_ENABLED.get() && isInList(entity, RampantConfig.FORCELIST.get());
    }

    /**
     * Determines whether a string representation of an entity's {@link ResourceLocation} is present in a given list.<br>
     * List elements are converted to {@code String} before checking.
     *
     * @param entity the entity to check
     * @param list the list to check against
     * @return true if entity's {@link ResourceLocation} string is in the list, false otherwise or if entity's {@link ResourceLocation} is null.
     */
    private static boolean isInList(Entity entity, List<?> list) {
        ResourceLocation resourceLocation = entity.getType().getRegistryName();
        if (resourceLocation == null) return false;
        String[] resourceString;
        for (Object entry : list) {
            resourceString = StringUtils.decomposeResourceLocationString(entry.toString());
            if (resourceString == null || resourceString.length < 2) continue;
            if (resourceString[0].equals(resourceLocation.getNamespace())
                    && (resourceString[1].equals(resourceLocation.getPath()) || resourceString[1].equals("*"))) return true;
        }
        return false;
    }
}
