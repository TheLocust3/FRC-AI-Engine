package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.map.SolidObject;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 1/5/17.
 */
public class LinePath {

    private Map map;
    private RobotControl robotControl;
    private ArrayList<PathPart> pathParts;
    private int atPath;

    public LinePath(Map map, RobotControl robotControl) {
        this.map = map;
        this.robotControl = robotControl;
        this.pathParts = new ArrayList<>();
        this.atPath = 0;
    }

    public PathPart getCurrentPath() {
        PathPart pathPart = pathParts.get(atPath);

        if (pathPart.isFinished()) {
            atPath++;

            if (atPath >= pathParts.size()) {
                return null;
            }

            pathPart = pathParts.get(atPath);
        }

        return pathPart;
    }

    public void generatePath(double startX, double startY, double endX, double endY) {
        pathParts.addAll(evaluatePath(startX, startY, endX, endY));
    }

    public ArrayList<PathPart> evaluatePath(double startX, double startY, double endX, double endY) {
        ArrayList<PathPart> paths = new ArrayList<>();

        RotatedRectangle path = this.createPaddedPath(startX, startY, endX, endY);
        SolidObject intersection = this.map.getIntersection(path.getShape());
        while (intersection != null) {
            RotatedRectangle avoidancePath = this.getShortestSnappedPath(path);

            double avoidanceDistance = avoidancePath.getLineDistance() + intersection.getWidth(); // TODO: Change from just using width
            double avoidX = avoidanceDistance * Math.cos(avoidancePath.getAngle().getRadians());
            double avoidY = avoidanceDistance * Math.sin(avoidancePath.getAngle().getRadians());

            paths.addAll(evaluatePath(avoidancePath.getX1(), avoidancePath.getY1(), avoidX, avoidY)); // TODO: Really need to do some testing with this recursion

            path = this.createPaddedPath(avoidancePath.getX2(), avoidancePath.getY2(), endX, endY);
            intersection = this.map.getIntersection(path.getShape());
        }

        paths.add(new PathPart(path, this.robotControl));

        return paths;
    }

    private RotatedRectangle createPaddedPath(double startX, double startY, double endX, double endY) {
        return new RotatedRectangle(startX, startY, endX + this.robotControl.getRobotBounds().getWidth(), endY + this.robotControl.getRobotBounds().getWidth());
    }

    // Get the shortest path around the object. The returned line is snapped to a 90 degree
    private RotatedRectangle getShortestSnappedPath(RotatedRectangle hypotenuse) {
        Angle possibleAngle1 = new Angle(hypotenuse.getAngle().getDegrees() + 90);
        Angle possibleAngle2 = new Angle(hypotenuse.getAngle().getDegrees() - 90);

        double possibleEndX1 = hypotenuse.getLineDistance() * Math.cos(possibleAngle1.getRadians());
        double possibleEndY1 = hypotenuse.getLineDistance() * Math.cos(possibleAngle1.getRadians());
        RotatedRectangle possibleLine1 = this.createPaddedPath(hypotenuse.getX1(), hypotenuse.getY1(), possibleEndX1, possibleEndY1); // TODO: I feel iffy about this

        double possibleEndX2 = hypotenuse.getLineDistance() * Math.cos(possibleAngle2.getRadians());
        double possibleEndY2 = hypotenuse.getLineDistance() * Math.cos(possibleAngle2.getRadians());
        RotatedRectangle possibleLine2 = this.createPaddedPath(hypotenuse.getX1(), hypotenuse.getY1(), possibleEndX2, possibleEndY2);

        return possibleLine1.getLineDistance() < possibleLine2.getLineDistance() ? possibleLine1 : possibleLine2;
    }
}
