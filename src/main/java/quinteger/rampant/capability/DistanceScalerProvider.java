package quinteger.rampant.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The capability provider class.<br>
 * Holds a {@link LazyOptional} with an instance of our capability.<br>
 * Handles (de)serialization if the capability hasn't been initialized yet.
 */
public class DistanceScalerProvider implements ICapabilitySerializable<CompoundNBT> {
    private final DistanceScaler distanceScaler;
    private final LazyOptional<IDistanceScaler> distanceScalerLazyOptional;

    public DistanceScalerProvider() {
        distanceScaler = new DistanceScaler();
        distanceScalerLazyOptional = LazyOptional.of(() -> distanceScaler);
    }

    public void invalidate() {
        distanceScalerLazyOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        //TODO: Fix the unchecked cast
        return distanceScalerLazyOptional.cast();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if (CapabilityDistanceScaler.DISTANCE_SCALER_CAPABILITY != null) {
            return (CompoundNBT) CapabilityDistanceScaler.DISTANCE_SCALER_CAPABILITY.writeNBT(distanceScaler, null);
        } else {
            return new CompoundNBT();
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (CapabilityDistanceScaler.DISTANCE_SCALER_CAPABILITY != null) {
            CapabilityDistanceScaler.DISTANCE_SCALER_CAPABILITY.readNBT(distanceScaler, null, nbt);
        }
    }
}
