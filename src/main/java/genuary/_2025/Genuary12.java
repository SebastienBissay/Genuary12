package genuary._2025;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static genuary._2025.parameters.Parameters.*;
import static genuary._2025.save.SaveUtil.saveSketch;

public class Genuary12 extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Genuary12.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        fill(FILL_COLOR.red(), FILL_COLOR.green(), FILL_COLOR.blue(), FILL_COLOR.alpha());
        noStroke();
        rect(MARGIN, MARGIN, WIDTH - 2f * MARGIN, HEIGHT - 2f * MARGIN);
        noFill();
        colorMode(COLOR_MODE.mode(), COLOR_MODE.max1(), COLOR_MODE.max2(), COLOR_MODE.max3(), COLOR_MODE.maxA());
        blendMode(BLEND_MODE);
        noLoop();
    }

    @Override
    public void draw() {
        List<Quad> quads = new ArrayList<>(List.of(
                new Quad(new PVector(MARGIN, MARGIN),
                        new PVector(MARGIN, HEIGHT - MARGIN),
                        new PVector(WIDTH - MARGIN, MARGIN),
                        new PVector(WIDTH - MARGIN, HEIGHT - MARGIN),
                        1, INITIAL_HUE)));
        List<Quad> stack = new ArrayList<>(List.of(quads.get(0)));

        while (!stack.isEmpty()) {
            Quad activeQuad = stack.get(0);
            activeQuad.render(this);
            quads.addAll(divideQuad(activeQuad));
            quads.remove(activeQuad);
            stack.remove(0);
            if (quads.get(quads.size() - 1).recursionDepth < MAX_RECURSION_DEPTH) {
                if (random(1) < 1 - quads.get(quads.size() - 1).recursionDepth * ADD_QUAD_FACTOR) {
                    stack.add(quads.get(quads.size() - 2));
                }
                if (random(1) < 1 - quads.get(quads.size() - 1).recursionDepth * ADD_QUAD_FACTOR) {
                    stack.add(quads.get(quads.size() - 1));
                }
            }
        }

        for (Quad q : quads) {
            q.render(this);
        }

        saveSketch(this);
    }

    private List<Quad> divideQuad(Quad quad) {
        PVector p1;
        PVector p2;
        Quad q1;
        Quad q2;
        if (((1 + GAUSSIAN_RANDOM_FACTOR * randomGaussian()) * abs(max(quad.northEast.x, quad.southEast.x) - min(quad.northWest.x,
                quad.southWest.x)))
                > (abs(max(quad.southEast.y, quad.southWest.y) - min(quad.northEast.y, quad.northWest.y)))) {
            float r = PADDING + random(1 - 2 * PADDING);
            p1 = PVector.add(PVector.mult(quad.northEast, r), PVector.mult(quad.northWest, 1 - r));
            r = PADDING + random(1 - 2 * PADDING);
            p2 = PVector.add(PVector.mult(quad.southEast, r), PVector.mult(quad.southWest, 1 - r));
            q1 = new Quad(quad.northWest, quad.southWest, p1, p2, quad.recursionDepth + 1,
                    quad.hue + HUE_FIXED_OFFSET + HUE_GAUSSIAN_OFFSET_FACTOR * randomGaussian());
            q2 = new Quad(quad.northEast, quad.southEast, p1, p2, quad.recursionDepth + 1,
                    quad.hue - HUE_FIXED_OFFSET + HUE_GAUSSIAN_OFFSET_FACTOR * randomGaussian());
        } else {
            float r = PADDING + random(1 - 2 * PADDING);
            p1 = PVector.add(PVector.mult(quad.northEast, r), PVector.mult(quad.southEast, 1 - r));
            r = PADDING + random(1 - 2 * PADDING);
            p2 = PVector.add(PVector.mult(quad.southWest, r), PVector.mult(quad.northWest, 1 - r));
            q1 = new Quad(quad.northWest, quad.northEast, p1, p2, quad.recursionDepth + 1,
                    quad.hue - HUE_FIXED_OFFSET + HUE_GAUSSIAN_OFFSET_FACTOR * randomGaussian());
            q2 = new Quad(quad.southEast, quad.southWest, p1, p2, quad.recursionDepth + 1,
                    quad.hue + HUE_FIXED_OFFSET + HUE_GAUSSIAN_OFFSET_FACTOR * randomGaussian());
        }
        return List.of(q1, q2);
    }
}