package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.map.SolidObjects.SolidObject;
import com.gmail.jakekinsella.map.SolidObjects.Wall;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 1/5/17.
 */
public class LinePath implements Paintable {

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

    @Override
    public void paint(Graphics2D graphics2D) {
        for (PathPart pathPart : this.pathParts) {
            pathPart.paint(graphics2D);
        }
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
            pathToIntersection = this.handleIntersectionWithWall(pathToIntersection, this.map);

            paths.addAll(this.evaluatePath(pathToIntersection));

            RotatedRectangle lastPath = paths.get(paths.size() - 1).getLine();
            path = this.createPaddedPath(lastPath.getEndX(), lastPath.getEndY(), endX, endY);
            intersection = this.map.getIntersection(path.getShape());
        }

        path = this.handleIntersectionWithWall(path, this.map);
        paths.add(new PathPart(path, this.robotControl));

        return paths;
    }

    private ArrayList<PathPart> evaluatePath(RotatedRectangle path) {
        return this.evaluatePath(path.getStartX(), path.getStartY(), path.getEndX(), path.getEndY());
    }

    private RotatedRectangle createPaddedPath(double startX, double startY, double endX, double endY) {
        return new RotatedRectangle(startX, startY, endX, endY, this.robotControl.getRobotBounds().getWidth());
    }

    private RotatedRectangle handleIntersectionWithWall(RotatedRectangle rotatedRectangle, Map map) {
        Wall wallIntersection = map.getIntersectionWithWall(rotatedRectangle.getShape());

        rotatedRectangle.setDistance(this.getDistanceToWall(wallIntersection, rotatedRectangle));
        return rotatedRectangle; // TODO: Add a bit of padding so that there is some room between the bot and the wall
    }

    private double getDistanceToWall(Wall wall, RotatedRectangle rotatedRectangle) {
        double endX = wall.getX() + wall.getWidth() * Math.cos(wall.getAngle().getRadians());
        double endY = wall.getY() + wall.getHeight() * Math.sin(wall.getAngle().getRadians());

        // Create line equations
        double wallSlope = (endY - wall.getY()) / (endX - wall.getX());
        double wallBValue = wall.getY() - wallSlope * wall.getX(); // y = ax + b

        double pointSlope = -1 / wallSlope;
        double pointBValue = rotatedRectangle.getStartY() - pointSlope * rotatedRectangle.getStartX();

        // Solve for intersection
        double xIntersection = (pointBValue - wallBValue) / (wallSlope - pointSlope); // Solve the two equations set equal to each other
        double yIntersection = wallSlope * xIntersection + wallBValue;

        return calculateDistanceBetweenPoints(rotatedRectangle.getStartX(), rotatedRectangle.getStartY(), xIntersection, yIntersection);
    }

    private double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
