package quinteger.rampant.config;

import com.electronwill.nightconfig.core.EnumGetMethod;
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

    public static final ConfigValue<ScalingMethod> SCALING_METHOD;

    public static final ConfigValue<Double> MAX_HEALTH_MULTIPLIER;
    public static final ConfigValue<Double> MAX_DAMAGE_MULTIPLIER;

    public static final ConfigValue<Double> MAX_SCALED_HEALTH;
    public static final ConfigValue<Double> MAX_DAMAGE_TO_SHIELD;

    static {
        BUILDER.push(Rampant.MODID);

        ACTIVE = BUILDER.comment(
                "\nThe main toggle for scaling application.\n" +
                        "If disabled, no new scalers will be applied. Existing ones will remain and function."
        ).define("active", true);

        SAFE_DISTANCE = BUILDER.comment(
                "\nDistance from Overworld coordinate origin at which no monster scaling will be applied and no extra NBT will be attached.\n" +
                        "This value defines a square of safe area with sides equal to this value doubled, centered on coordinate origin.\n" +
                        "In other dimensions this area will be adjusted according to their coordinate scale. For example, in the Nether this value will be divided by 8."
        ).defineInRange("safeDistance", 16384D, 512D, 1e12D);
        SCALING_STEP = BUILDER.comment(
                "\nDistance segments beyond Overworld safe area over which monsters become more powerful by factors specified below.\n" +
                        "In other dimensions this area will be adjusted according to their coordinate scale. For example, in the Nether this value will be divided by 8."
        ).defineInRange("scalingStep", 16384D, 512D, 1e12D);

        HEALTH_SCALING_FACTOR = BUILDER.comment(
                "\nBase value for calculating health multiplier.\n" +
                        "As a rule of thumb, monsters' health will be multiplied by this value at the end of the first scalingStep.\n" +
                        "For preceding and following scaling behavior - refer to scalingMethod."
        ).defineInRange("healthScalingFactor", 2D, 1D, 1000D);
        DAMAGE_SCALING_FACTOR = BUILDER.comment(
                "\nBase value for calculating damage multiplier.\n" +
                        "As a rule of thumb, monsters' damage will be multiplied by this value at the end of the first scalingStep.\n" +
                        "For preceding and following scaling behavior - refer to scalingMethod."
        ).defineInRange("damageScalingFactor", 2D, 1D, 1000D);

        SCALING_METHOD = BUILDER.comment(
                "\nScaling calculation method.\n" +
                        "LINEAR - each scalingStep provides as much absolute power as the first one.\n" +
                        "For example, with a factor of 2 at the end of steps 1, 2 and 3 monsters will become 2, 3 and 4 times more powerful.\n" +
                        "EXPONENT - each scalingStep multiplies monster power by specified factor.\n" +
                        "For example, with a factor of 2 at the end of steps 1, 2 and 3 monsters will become 2, 4 and 8 times more powerful.\n" +
                        "Case-insensitive."
        ).defineEnum("scalingMethod", ScalingMethod.LINEAR, EnumGetMethod.NAME_IGNORECASE);

        MAX_HEALTH_MULTIPLIER = BUILDER.comment(
                "\nFinal calculated health multiplier will not be higher than this value."
        ).defineInRange("maxHealthMultiplier", 1e12D, 1D, Float.MAX_VALUE);
        MAX_DAMAGE_MULTIPLIER = BUILDER.comment(
                "\nFinal calculated damage multiplier will not be higher than this value."
        ).defineInRange("maxDamageMultiplier", 1e12D, 1D, Float.MAX_VALUE);

        MAX_SCALED_HEALTH = BUILDER.comment(
                "\nHealth multiplier will not raise monster's health above this value.\n" +
                        "This check was added instead of changing MAX_HEALTH because MAX_HEALTH can be patched by other mods.\n" +
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

    public enum ScalingMethod {
        LINEAR,
        EXPONENT
    }
}
