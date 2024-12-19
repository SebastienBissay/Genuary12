package genuary._2025.parameters;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static processing.core.PConstants.HSB;
import static processing.core.PConstants.SCREEN;

public final class Parameters {
    public static final long SEED = 20250112;

    public static final int WIDTH = 2025;
    public static final int HEIGHT = 2025;
    public static final float MARGIN = 50;
    public static final int MAX_RECURSION_DEPTH = 18;
    public static final float PADDING = .2f;
    public static final float GAUSSIAN_RANDOM_FACTOR = .5f;
    public static final float HUE_FIXED_OFFSET = 10;
    public static final float HUE_GAUSSIAN_OFFSET_FACTOR = 5;
    public static final float ADD_QUAD_FACTOR = .005f;
    public static final Color BACKGROUND_COLOR = new Color(255);
    public static final Color FILL_COLOR = new Color(20);
    public static final ColorMode COLOR_MODE = new ColorMode(HSB, 360, 100, 100, 100);
    public static final Color STROKE_COLOR = new Color(0, 70, 80, 30);
    public static final int BLEND_MODE = SCREEN;
    public static final float INITIAL_HUE = 100;


    /**
     * Helper method to extract the constants in order to save them to a json file
     *
     * @return a Map of the constants (name -> value)
     */
    public static Map<String, Object> toJsonMap() throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        Field[] declaredFields = Parameters.class.getDeclaredFields();
        for (Field field : declaredFields) {
            map.put(field.getName(), field.get(Parameters.class));
        }

        return Collections.singletonMap(Parameters.class.getSimpleName(), map);
    }

    public record Color(float red, float green, float blue, float alpha) {
        public Color(float red, float green, float blue) {
            this(red, green, blue, 255);
        }

        public Color(float grayscale, float alpha) {
            this(grayscale, grayscale, grayscale, alpha);
        }

        public Color(float grayscale) {
            this(grayscale, 255);
        }

        public Color(String hexCode) {
            this(decode(hexCode));
        }

        public Color(Color color) {
            this(color.red, color.green, color.blue, color.alpha);
        }

        public static Color decode(String hexCode) {
            return switch (hexCode.length()) {
                case 2 -> new Color(Integer.valueOf(hexCode, 16));
                case 4 -> new Color(Integer.valueOf(hexCode.substring(0, 2), 16),
                        Integer.valueOf(hexCode.substring(2, 4), 16));
                case 6 -> new Color(Integer.valueOf(hexCode.substring(0, 2), 16),
                        Integer.valueOf(hexCode.substring(2, 4), 16),
                        Integer.valueOf(hexCode.substring(4, 6), 16));
                case 8 -> new Color(Integer.valueOf(hexCode.substring(0, 2), 16),
                        Integer.valueOf(hexCode.substring(2, 4), 16),
                        Integer.valueOf(hexCode.substring(4, 6), 16),
                        Integer.valueOf(hexCode.substring(6, 8), 16));
                default -> throw new IllegalArgumentException();
            };
        }
    }

    public record ColorMode(int mode, float max1, float max2, float max3, float maxA) {
    }
}
