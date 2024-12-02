package parameters;

import processing.core.PVector;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static processing.core.PConstants.MULTIPLY;

public final class Parameters {
    public static final long SEED = 42;
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static final int MARGIN = -50;
    public static final PVector ORIGIN = new PVector(WIDTH * .7f, HEIGHT / 3f);
    public static final int NUMBER_OF_ITERATIONS = 100000;
    public static final float LINE_LENGTH = 200;
    public static final float SPEED_FIXED_PART = 1f;
    public static final float SPEED_GAUSSIAN_FACTOR = .5f;
    public static final float NOISE_BASE_SCALE = 5f;
    public static final float NOISE_EXPONENT = .25f;
    public static final float NOISE_EXTERNAL_FACTOR = 3f;
    public static final float NOISE_INTERNAL_FACTOR = 5f;
    public static final float NOISE_DIVISOR_FIXED_PART = 2f;
    public static final float NOISE_DIVISOR_VARIABLE_PART_FACTOR = .5f;
    public static final Color BACKGROUND_COLOR = new Color(245);
    public static final Color STROKE_COLOR = new Color(0, 10, 75, 25);
    public static final int BLEND_MODE = MULTIPLY;

    /**
     * Helper method to extract the constants in order to save them to a json file
     *
     * @return a Map of the constants (name -> value)
     */
    public static Map<String, ?> toJsonMap() throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        Field[] declaredFields = Parameters.class.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
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
}
