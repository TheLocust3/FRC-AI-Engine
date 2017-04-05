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

    private final int ANGLE_INCREMENT = 20;

    private int atPath;
    private int endX, endY;

    private Map map;
    private RobotControl robotControl;
    private ArrayList<PathPart> pathParts;

    public LinePath(Map map, RobotControl robotControl) {
        this.map = map;
        this.robotControl = robotControl;
        this.pathParts = new ArrayList<>();
        this.atPath = 0;
    }

    public PathPart getCurrentPath() {
        if (this.atPath >= this.pathParts.size()) {
            return null;
        }

        PathPart pathPart = this.pathParts.get(atPath);
        if (pathPart.isFinished()) {
            this.atPath++;

            if (this.atPath >= this.pathParts.size()) {
                return null;
            }

            pathPart = this.pathParts.get(this.atPath);
        }

        return pathPart;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        for (PathPart pathPart : this.pathParts) {
            pathPart.paint(graphics2D);
        }

        graphics2D.setColor(Color.GREEN);
        graphics2D.fillOval(this.endX, this.endY, 10, 10);
    }

    public void generatePath(double startX, double startY, double endX, double endY) {
        this.endX = (int) endX;
        this.endY = (int) endY;

        this.pathParts.addAll(evaluatePath(startX, startY, endX, endY));
    }

    public ArrayList<PathPart> evaluatePath(double startX, double startY, double endX, double endY) {
        ArrayList<PathPart> paths = new ArrayList<>();
        if (this.map.getIntersection(new RotatedRectangle(endX - 2, endY - 2, endX + 2, endY + 2, 4)) != null) {
            return paths;
        }

        RotatedRectangle path = this.createPaddedPath(startX, startY, endX, endY);
        SolidObject intersection = this.map.getIntersection(path);
        while (intersection != null) {
            RotatedRectangle pathToIntersection = this.createPaddedPath(path.getStartX(), path.getStartY(), intersection.getCenterX(), intersection.getCenterY());

            paths.addAll(this.handleIntersection(pathToIntersection));

            RotatedRectangle lastPath = paths.get(paths.size() - 1).getLine();
            path = this.createPaddedPath(lastPath.getEndX(), lastPath.getEndY(), endX, endY);
            intersection = this.map.getIntersection(path);
        }

        path = this.handleIntersectionWithWall(path, this.map);
        paths.add(new PathPart(path, this.robotControl));

        return paths;
    }

    // TODO: Could get stuck if there is no easy way to rotate out of an intersection
    private ArrayList<PathPart> handleIntersection(RotatedRectangle path) {
        ArrayList<PathPart> paths = new ArrayList<>();
        SolidObject intersection = this.map.getIntersection(path);
        while (intersection != null) {
            Angle newAngle = new Angle(path.getAngle().getDegrees() + this.ANGLE_INCREMENT);
            path.rotate(newAngle);
            path = this.handleIntersectionWithWall(path, this.map);

            intersection = this.map.getIntersection(path);
        }

        paths.add(new PathPart(path, this.robotControl));

        return paths;
    }

    private RotatedRectangle createPaddedPath(double startX, double startY, double endX, double endY) {
        return new RotatedRectangle(startX, startY, endX, endY, this.robotControl.getRobotBounds().getWidth());
    }

    private RotatedRectangle handleIntersectionWithWall(RotatedRectangle rotatedRectangle, Map map) {
        double distanceToWall = map.getDistanceToClosestIntersectionWithWall(rotatedRectangle);
        if (distanceToWall != -1) {
            rotatedRectangle.setDistance(distanceToWall);
            // TODO: Add a bit of padding so that there is some room between the bot and the wall
        }

        return rotatedRectangle;
    }
}
