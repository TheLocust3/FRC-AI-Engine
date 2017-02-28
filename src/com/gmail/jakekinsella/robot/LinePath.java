package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.map.SolidObjects.SolidObject;

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

        if (pathPart == null) {
            return null;
        }

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
            RotatedRectangle pathToIntersection = this.createPaddedPath(startX, startY, intersection.getX(), intersection.getY());
            Angle newAngle = new Angle(pathToIntersection.getAngle().getDegrees() + 10);
            pathToIntersection.rotate(newAngle);
            // TODO: Lower distance so it can drive to a wall

            paths.addAll(this.evaluatePath(pathToIntersection));

            RotatedRectangle lastPath = paths.get(paths.size() - 1).getLine();
            path = this.createPaddedPath(lastPath.getMaxX(), lastPath.getMaxY(), endX, endY);
            intersection = this.map.getIntersection(path.getShape());
        }

        paths.add(new PathPart(path, this.robotControl));

        return paths;
    }

    private ArrayList<PathPart> evaluatePath(RotatedRectangle path) {
        return this.evaluatePath(path.getMinX(), path.getMinY(), path.getMaxX(), path.getMaxY());
    }

    private RotatedRectangle createPaddedPath(double startX, double startY, double endX, double endY) {
        return new RotatedRectangle(startX, startY, endX + this.robotControl.getRobotBounds().getWidth(), endY + this.robotControl.getRobotBounds().getWidth());
    }
}
