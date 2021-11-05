package quinteger.rampant.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * Static holder for our capability, static method to register it during mod setup and NBT storage implementation.
 */
public class CapabilityDistanceScaler {

    @CapabilityInject(IDistanceScaler.class)
    public static Capability<IDistanceScaler> DISTANCE_SCALER_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IDistanceScaler.class, new Storage(), DistanceScaler::new);
    }

    public static class Storage implements Capability.IStorage<IDistanceScaler> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<IDistanceScaler> capability, IDistanceScaler instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();

            nbt.putBoolean("applied", instance.isApplied());

            CompoundNBT healthNbt = new CompoundNBT();
            nbt.put("health", healthNbt);
            healthNbt.putFloat("value", instance.getHealthMultiplier());
            if (instance.getHealthAttributeModifier() != null) {
                healthNbt.putUUID("modifier", instance.getHealthAttributeModifier());
            }

            CompoundNBT damageNbt = new CompoundNBT();
            nbt.put("damage", damageNbt);
            damageNbt.putFloat("value", instance.getDamageMultiplier());

            return nbt;
        }

        @Override
        public void readNBT(Capability<IDistanceScaler> capability, IDistanceScaler instance, Direction side, INBT nbt) {
            if (!(nbt instanceof CompoundNBT)) return;

            instance.setApplied(((CompoundNBT) nbt).getBoolean("applied"));

            CompoundNBT healthNbt = ((CompoundNBT) nbt).getCompound("health");
            instance.setHealthMultiplier(healthNbt.getFloat("value"));
            if (healthNbt.hasUUID("modifier")) {
                instance.setHealthAttributeModifier(healthNbt.getUUID("modifier"));
            }

            CompoundNBT damageNbt = ((CompoundNBT) nbt).getCompound("damage");
            instance.setDamageMultiplier(damageNbt.getFloat("value"));
        }
    }
}
