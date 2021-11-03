package quinteger.rampant.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import quinteger.rampant.Rampant;

public class RampantConfig {
    public static final Builder BUILDER = new Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ConfigValue<Boolean> ACTIVE;

    public static final ConfigValue<Double> SAFE_DISTANCE;
    public static final ConfigValue<Double> SCALING_STEP;

    public static final ConfigValue<Double> HEALTH_SCALING_FACTOR;
    public static final ConfigValue<Double> DAMAGE_SCALING_FACTOR;

    public static final ConfigValue<Double> MAX_HEALTH_MULTIPLIER;
    public static final ConfigValue<Double> MAX_DAMAGE_MULTIPLIER;

    public static final ConfigValue<Double> MAX_SCALED_HEALTH;
    public static final ConfigValue<Double> MAX_DAMAGE_TO_SHIELD;

    static {
        BUILDER.push(Rampant.MODID);

        ACTIVE = BUILDER.comment(
                "\nThe main toggle for scaling application.\n" +
                        "If disabled, no new scalers will be applied. Existing ones will remain."
        ).define("active", true);

        SAFE_DISTANCE = BUILDER.comment(
                "\nDistance from Overworld coordinate origin at which no monster scaling will be applied and no extra NBT will be attached.\n" +
                        "This value defines a square of safe area with sides equal to this value doubled, centered on coordinate origin.\n" +
                        "In other dimensions this area will be adjusted according to their coordinate scale. For example, in the Nether this value will be divided by 8."
        ).defineInRange("safeDistance", 10240D, 512D, 1e12D);
        SCALING_STEP = BUILDER.comment(
                "\nDistance segments beyond safe area over which monsters become more powerful by factors specified below."
        ).defineInRange("scalingStep", 10240D, 512D, 1e12D);

        HEALTH_SCALING_FACTOR = BUILDER.comment(
                "\nBase value for calculating scaling multipliers.\n" +
                        "At the end of the first scalingStep monsters' health will be multiplied by this value."
        ).defineInRange("healthScalingFactor", 2D, 1D, 1000D);
        DAMAGE_SCALING_FACTOR = BUILDER.comment(
                "\nBase value for calculating scaling multipliers.\n" +
                        "At the end of the first scalingStep monsters' damage will be multiplied by this value."
        ).defineInRange("damageScalingFactor", 2D, 1D, 1000D);

        MAX_HEALTH_MULTIPLIER = BUILDER.comment(
                "\nFinal calculated health multiplier will not be higher than this value."
        ).defineInRange("maxHealthMultiplier", 1e12D, 1D, Float.MAX_VALUE);
        MAX_DAMAGE_MULTIPLIER = BUILDER.comment(
                "\nFinal calculated damage multiplier will not be higher than this value."
        ).defineInRange("maxDamageMultiplier", 1e12D, 1D, Float.MAX_VALUE);

        MAX_SCALED_HEALTH = BUILDER.comment(
                "\nHealth multiplier will not raise monster's health above this value.\n" +
                        "This check was added because MAX_HEALTH attribute can be patched by other mods.\n" +
                        "You probably won't need to change this."
        ).defineInRange("maxScaledHealth", 1e30D, 1000D, Float.MAX_VALUE);
        MAX_DAMAGE_TO_SHIELD = BUILDER.comment(
                "\nShields cannot take more than this amount of damage from a single hit.\n" +
                        "This limit is needed due to a bug in vanilla code and is patched via a mixin.\n" +
                        "You probably won't need to change this."
        ).defineInRange("maxDamageToShield", 1e9D, 1000D, 2e9D);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "rampant-common.toml");
    }
}
