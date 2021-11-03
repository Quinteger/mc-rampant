package quinteger.rampant.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;

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
                && entity instanceof LivingEntity
                && isHostileMob(entity));
    }
}
