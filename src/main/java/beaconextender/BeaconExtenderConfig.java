package beaconextender;

import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.*;
import dev.isxander.yacl3.config.v2.api.autogen.Boolean;
import dev.isxander.yacl3.config.v2.api.autogen.Label;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.awt.*;

import static beaconextender.BeaconExtenderConfig.FunctionType.EXPONENTIAL;
import static beaconextender.BeaconExtenderConfig.FunctionType.LINEAR;

public class BeaconExtenderConfig {
    public static ConfigClassHandler<BeaconExtenderConfig> HANDLER = ConfigClassHandler.createBuilder(BeaconExtenderConfig.class)
            .id(ResourceLocation.fromNamespaceAndPath(BeaconExtender.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("beaconextender.json5"))
                    .setJson5(true)
                    .build()
            )
            .build();

    private static final String CLIENT_CATEGORY = "client";
    private static final String SERVER_CATEGORY = "server";
    private static final String RANGE_GROUP = "range";
    private static final String EFFECT_DURATION_GROUP = "effectDuration";

    static final String RANGE_FUNCITON_DESCRIPTION = """
                Defines the type of the range function.
                Can be either "EXPONENTIAL" or "LINEAR".
                The exponential function gets evaluated like
                    f(layers) = param1 * param2 ^ layers
                The linear function gets evaluated like
                    f(layers) = param1 * layers + param2
                """;

    static final String EFFECT_DURATION_FUNCITON_DESCRIPTION = """
                Defines the type of the effect duration function.
                Can be either "EXPONENTIAL" or "LINEAR".
                The exponential function gets evaluated like
                    f(layers) = param1 * param2 ^ layers
                The linear function gets evaluated like
                    f(layers) = param1 * layers + param2
                """;


    @AutoGen(category = CLIENT_CATEGORY)
    @Boolean(formatter = Boolean.Formatter.YES_NO, colored = true)
    @SerialEntry(comment = "Show the number of active layers in the beacon ui")
    boolean showBeaconLayers = true;

    @AutoGen(category = SERVER_CATEGORY)
    @IntSlider(min = 4, max = 12, step = 1)
    @SerialEntry(comment = "Defines the maximum number of beacon layers that will change the effect")
    int maxLayers = 6;

    @AutoGen(category = SERVER_CATEGORY) @Label
    private final Component warningLabel = Component.translatable("yacl3.config.beaconextender:config.warning");


    @AutoGen(category = SERVER_CATEGORY, group = RANGE_GROUP)
    @EnumCycler
    @CustomDescription(RANGE_FUNCITON_DESCRIPTION)
    @SerialEntry(comment = RANGE_FUNCITON_DESCRIPTION)
    FunctionType rangeFunctionType = LINEAR;

    @AutoGen(category = SERVER_CATEGORY, group = RANGE_GROUP)
    @SerialEntry(comment = "The first parameter of the range function.")
    @DoubleField
    double rangeFunctionParam1 = 10.0;

    @AutoGen(category = SERVER_CATEGORY, group = RANGE_GROUP)
    @SerialEntry(comment = "The second parameter of the range function.")
    @DoubleField
    double rangeFunctionParam2 = 10.0;

    @AutoGen(category = SERVER_CATEGORY, group = EFFECT_DURATION_GROUP)
    @EnumCycler
    @CustomDescription(EFFECT_DURATION_FUNCITON_DESCRIPTION)
    @SerialEntry(comment = EFFECT_DURATION_FUNCITON_DESCRIPTION)
    FunctionType effectDurationFunctionType = LINEAR;

    @AutoGen(category = SERVER_CATEGORY, group = EFFECT_DURATION_GROUP)
    @SerialEntry(comment = "The first parameter of the effect duration function.")
    @DoubleField
    double effectDurationFunctionParam1 = 10.0;

    @AutoGen(category = SERVER_CATEGORY, group = EFFECT_DURATION_GROUP)
    @SerialEntry(comment = "The second parameter of the effect duration function.")
    @DoubleField
    double effectDurationFunctionParam2 = 10.0;

    enum FunctionType implements NameableEnum {
        EXPONENTIAL,
        LINEAR;

        @Override
        public Component getDisplayName() {
            return Component.literal(name());
        }
    }

    private double evaluate(FunctionType t, double param1, double param2, int beaconLevel) {
        switch (t) {
            case EXPONENTIAL -> {
                return param1 * Math.pow(param2, beaconLevel);
            }
            case LINEAR -> {
                return param1 * beaconLevel + param2;
            }
            default -> {
                throw new IllegalStateException("Type must be either EXPONENTIAL or LINEAR");
            }
        }
    }

    public int getMaxLayers() {
        return maxLayers;
    }

    public double getRange(int beaconLevel) {
        return evaluate(rangeFunctionType, rangeFunctionParam1, rangeFunctionParam2, beaconLevel);
    }

    public double getEffectDuration(int beaconLevel) {
        return evaluate(effectDurationFunctionType, effectDurationFunctionParam1, effectDurationFunctionParam2, beaconLevel);
    }

    public boolean showBeaconLayers() {
        return showBeaconLayers;
    }
}
