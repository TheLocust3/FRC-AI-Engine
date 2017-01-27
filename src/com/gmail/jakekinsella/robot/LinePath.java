package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.map.SolidObject;

import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 1/5/17.
 */
public class LinePath {

    private Map map;
    private ArrayList<Line2D.Double> lines;

    public LinePath(Map map) {
        this.map = map;
        this.lines = new ArrayList<>();
    }

    public void generatePath(double startX, double startY, double endX, double endY) {
        lines.addAll(evaluatePath(startX, startY, endX, endY));
    }

    public ArrayList<Line2D.Double> evaluatePath(double startX, double startY, double endX, double endY) {
        ArrayList<Line2D.Double> paths = new ArrayList<>();

        Line2D.Double path = new Line2D.Double(startX, startY, endX, endY);
        SolidObject intersection = this.map.getIntersection(path);
        while (intersection != null) {
            Line2D.Double avoidancePath = this.getShortestSnappedPath(path);
            Angle avoidanceAngle = new Angle(avoidancePath);

            double avoidanceDistance = getLineDistance(avoidancePath) + intersection.getWidth(); // TODO: Change from just using width
            double avoidX = avoidanceDistance * Math.cos(avoidanceAngle.getDegrees());
            double avoidY = avoidanceDistance * Math.sin(avoidanceAngle.getDegrees());

            paths.addAll(evaluatePath(avoidancePath.getX1(), avoidancePath.getY1(), avoidX, avoidY)); // TODO: Really need to do some testing with this recursion

            path = new Line2D.Double(avoidancePath.getX2(), avoidancePath.getY2(), endX, endY);
            intersection = this.map.getIntersection(path);
        }

        return paths;
    }

    // Get the shortest path around the object. The returned line is snapped to a 90 degree
    private Line2D.Double getShortestSnappedPath(Line2D.Double hypotenuse) {
        Angle angle = new Angle(hypotenuse);
        Angle possibleAngle1 = new Angle(angle.getDegrees() + 90);
        Angle possibleAngle2 = new Angle(angle.getDegrees() - 90);

        double possibleEndX1 = this.getLineDistance(hypotenuse) * Math.cos(possibleAngle1.getDegrees());
        double possibleEndY1 = this.getLineDistance(hypotenuse) * Math.cos(possibleAngle1.getDegrees());
        Line2D.Double possibleLine1 = new Line2D.Double(hypotenuse.getX1(), hypotenuse.getX1(), possibleEndX1, possibleEndY1); // TODO: I feel iffy about this

        double possibleEndX2 = this.getLineDistance(hypotenuse) * Math.cos(possibleAngle2.getDegrees());
        double possibleEndY2 = this.getLineDistance(hypotenuse) * Math.cos(possibleAngle2.getDegrees());
        Line2D.Double possibleLine2 = new Line2D.Double(hypotenuse.getX1(), hypotenuse.getX1(), possibleEndX2, possibleEndY2);

        return getLineDistance(possibleLine1) < getLineDistance(possibleLine2) ? possibleLine1 : possibleLine2;
    }

    private double getLineDistance(Line2D.Double line) {
        return Math.sqrt(Math.pow(line.getY2() - line.getY1(), 2) + Math.pow(line.getX2() - line.getX1(), 2));
    }
}
