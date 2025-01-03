package beaconextender;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Path;

public class BeaconExtenderConfig {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(BeaconExtender.MOD_ID + ".toml");
    public static final Logger LOGGER = BeaconExtender.LOGGER;

    private int maxLayers;
    private Function range;

    @SuppressWarnings({"CanBeFinal", "CanBeStatic"})
    private class Function {
        private String type = "linear";
        private Exponential exponential = new Exponential();
        private Linear linear = new Linear();

        private class Exponential {
            private double initialValue = 12.0;
            private double base = 1.5;
        }

        private class Linear {
            private double slope = 10.0;
            private double offset = 10.0;
        }
    }

    private static BeaconExtenderConfig loadDefault() {
        InputStream defaultConfigStream = BeaconExtenderConfig.class.getResourceAsStream("/assets/beaconextender/defaultConfig.toml");
        if (defaultConfigStream == null) {
            throw new IllegalStateException("Default config could not be loaded");
        }
        try {
            BeaconExtenderConfig config = new Toml().read(defaultConfigStream).to(BeaconExtenderConfig.class);
            assert (config.range.type.equals("exponential") || config.range.type.equals("linear"));
            return config;
        } catch (Exception e) {
            throw new IllegalStateException("Default config could not be loaded", e);
        }
    }

    private static void writeDefaultConfig(File file) throws IOException {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Could not create parent directories");
            }
        } else if (!dir.isDirectory()) {
            throw new IOException("Parent file is not a directory");
        }

        try (Writer writer = new FileWriter(file)) {
            BeaconExtenderConfig config = BeaconExtenderConfig.loadDefault();
            new TomlWriter().write(config, writer);
        }
    }

    public static synchronized BeaconExtenderConfig load() {
        File file = CONFIG_PATH.toFile();
        if (file.exists()) {
            try {
                return new Toml().read(file).to(BeaconExtenderConfig.class);
            } catch (Exception e) {
                LOGGER.error("Could not load configuration file, using the default configuration", e);
            }
        } else {
            try {
                writeDefaultConfig(file);
            } catch (IOException e) {
                LOGGER.warn("Could not write default configuration file", e);
            }
        }
        return BeaconExtenderConfig.loadDefault();
    }

    public int getMaxLayers() {
        return maxLayers;
    }

    public double getDistance(int beaconLevel) {
        return getFunctionValue(beaconLevel, range);
    }

    private double getFunctionValue(int beaconLevel, Function f) {
        if (f.type.equals("exponential")) {
            double initialValue = f.exponential.initialValue;
            double base = f.exponential.base;

            return (initialValue * Math.pow(base, beaconLevel));
        } else if (f.type.equals("linear")) {
            double slope = f.linear.slope;
            double offset = f.linear.offset;

            return slope * (double)beaconLevel + offset;
        } else {
            throw new IllegalStateException("type must be either exponential or linear");
        }
    }
}
