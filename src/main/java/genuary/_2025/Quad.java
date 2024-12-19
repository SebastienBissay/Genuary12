package genuary._2025;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static genuary._2025.parameters.Parameters.COLOR_MODE;
import static genuary._2025.parameters.Parameters.STROKE_COLOR;

public class Quad {
    public final PVector northEast;
    public final PVector northWest;
    public final PVector southWest;
    public final PVector southEast;
    public final float hue;
    public final int recursionDepth;

    public Quad(PVector p1, PVector p2, PVector p3, PVector p4, int recursionDepth, float hue) {
        PVector barycenter = PVector.add(PVector.add(p1, p2), PVector.add(p3, p4)).div(4);
        List<PVector> vectors = new ArrayList<>(List.of(p1, p2, p3, p4));
        vectors.sort((p, q) ->
                Float.compare(PVector.sub(p, barycenter).heading(), PVector.sub(q, barycenter).heading()));
        southWest = vectors.get(0);
        southEast = vectors.get(1);
        northEast = vectors.get(2);
        northWest = vectors.get(3);
        this.recursionDepth = recursionDepth;
        this.hue = hue;
    }

    void render(PApplet pApplet) {
        pApplet.stroke(hue % COLOR_MODE.max1(), STROKE_COLOR.green(), STROKE_COLOR.blue(), STROKE_COLOR.alpha());
        pApplet.quad(southWest.x, southWest.y, southEast.x, southEast.y, northEast.x, northEast.y, northWest.x, northWest.y);
    }
}
