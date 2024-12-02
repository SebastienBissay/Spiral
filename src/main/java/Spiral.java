import processing.core.PApplet;
import processing.core.PVector;

import java.util.stream.IntStream;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class Spiral extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Spiral.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        stroke(STROKE_COLOR.red(), STROKE_COLOR.green(), STROKE_COLOR.blue(), STROKE_COLOR.alpha());
        noFill();
        blendMode(BLEND_MODE);
        frameRate(-1);
    }

    @Override
    public void draw() {
        //Start from the point closest to origin among 3 uniformly distributed ones
        PVector point = IntStream.range(0, 3)
                .mapToObj(i -> new PVector(random(MARGIN, width - MARGIN), random(MARGIN, height - MARGIN)))
                .min((v1, v2) -> Float.compare(sq(v1.x - ORIGIN.x) + sq(v1.y - ORIGIN.y),
                        sq(v2.x - ORIGIN.x) + sq(v2.y - ORIGIN.y)))
                .orElseThrow();
        //Apply noisefield
        for (int k = 0; k < LINE_LENGTH; k++) {
            point(point.x, point.y);
            point.add(PVector.fromAngle(TWO_PI * myNoise(point.x, point.y))
                    .mult(SPEED_FIXED_PART + SPEED_GAUSSIAN_FACTOR * randomGaussian()));
        }

        if (frameCount >= NUMBER_OF_ITERATIONS) {
            noLoop();
            saveSketch(this);
        }
    }

    private float myNoise(float x, float y) {
        float scale = NOISE_BASE_SCALE + pow(sq(ORIGIN.x - x) + sq(ORIGIN.y - y), NOISE_EXPONENT);
        return NOISE_EXTERNAL_FACTOR * (int) (NOISE_INTERNAL_FACTOR * noise(x / scale, y / scale))
                / (NOISE_DIVISOR_FIXED_PART + NOISE_DIVISOR_VARIABLE_PART_FACTOR * scale);
    }
}
