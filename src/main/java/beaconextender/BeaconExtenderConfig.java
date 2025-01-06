package beaconextender;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = BeaconExtender.MOD_ID)
public class BeaconExtenderConfig implements ConfigData {
    @Comment("Defines the maximum number of beacon layers that will change the effect")
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 4, max = 12)
    int maxLayers = 6;

    @Comment("Defines the method of calculating the effect range")
    @ConfigEntry.Gui.PrefixText
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.Gui.CollapsibleObject
    Function range = Function.linear(10.0, 10.0);

    @Comment("Defines the method of calculating the effect duration in seconds")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    Function effectDuration = Function.linear(2.0, 9.0);

    static class Function {
        @Comment("""
                Defines the type of the function.
                Can be either "EXPONENTIAL" or "LINEAR".
                The exponential function gets evaluated like
                    f(layers) = param1 * param2 ^ layers
                The linear function gets evaluated like
                    f(layers) = param1 * layers + param2
                """)
        @ConfigEntry.Gui.Tooltip(count = 6)
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        Type type;

        @Comment("The first parameter of the function.")
        @ConfigEntry.Gui.Tooltip
        double param1;

        @Comment("The second parameter of the function.")
        @ConfigEntry.Gui.Tooltip
        double param2;

        enum Type {
            EXPONENTIAL,
            LINEAR
        }
        static Function exponential(double initialValue, double base) {
            return new Function(Type.EXPONENTIAL, initialValue, base);
        }
        static Function linear(double slope, double offset) {
            return new Function(Type.LINEAR, slope, offset);
        }
        Function(Type type, double param1, double param2) {
            this.type = type;
            this.param1 = param1;
            this.param2 = param2;
        }

        public double evaluate(int beaconLevel) {
            switch (type) {
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
    }

    public int getMaxLayers() {
        return maxLayers;
    }

    public double getRange(int beaconLevel) {
        return range.evaluate(beaconLevel);
    }

    public double getEffectDuration(int beaconLevel) {
        return effectDuration.evaluate(beaconLevel);
    }
}
