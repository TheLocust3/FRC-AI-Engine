package com.gmail.jakekinsella.map;

import com.gmail.jakekinsella.robot.RobotControl;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Map {

    private final int ROBOTS_ON_FIELD = 6;

    private ArrayList<SolidObject> map = this.createDefaultField(); // Map is on its side, 0,0 is the corner to the right of the blue tower

    public void addObstacle(FuzzyObject obstacle) {
        map.add(obstacle);
    }

    public void tick(double deltaSeconds) {
        for (SolidObject solidObject : this.map) {
            solidObject.tick(deltaSeconds);
        }
    }

    // visionObjects = 0 -> x, 1 -> y, 2 -> type
    public void inputVisionData(ArrayList<int[]> visionObjects, RobotControl robotControl) {
        ArrayList<SolidObject> newMap = this.createDefaultField();

        for (SolidObject solidObject : this.map) {
            if (solidObject instanceof FuzzyObject && solidObject.isObjectReal()) {
                newMap.add(solidObject);
            }
        }

        for (int[] visionObject : visionObjects) {
            switch (visionObject[2]) {
                case 0:
                    newMap.add(new Robot(visionObject[0], visionObject[1]));
                case 1:
                    newMap.add(new Ball(visionObject[0], visionObject[1]));
            }
        }

        newMap = this.tuneRobotsFromVision(newMap, robotControl);

        this.map = newMap;
    }

    private ArrayList<SolidObject> createDefaultField() {
        ArrayList<SolidObject> newMap = new ArrayList<>();

        // Create blue defenses
        newMap.add(new Defense(0, 400));
        newMap.add(new Defense(50, 400));
        newMap.add(new Defense(100, 400));
        newMap.add(new Defense(150, 400));
        newMap.add(new Defense(200, 400));

        // Create red defenses
        newMap.add(new Defense(50, 600));
        newMap.add(new Defense(100, 600));
        newMap.add(new Defense(150, 600));
        newMap.add(new Defense(200, 600));
        newMap.add(new Defense(250, 600));

        newMap.add(new Tower(500, 0));
        newMap.add(new Tower(500, 1000));

        return newMap;
    }

    private ArrayList<SolidObject> tuneRobotsFromVision(ArrayList<SolidObject> newMap, RobotControl robotControl) {
        ArrayList<Robot> robots = robotsInMap(newMap);

        if (robots.size() > this.ROBOTS_ON_FIELD) {
            // TODO: Combine extra robots together
        }

        for (Robot robot : robots) {
            if (this.isObjectNearRobotBounds(robot.getX(), robot.getY(), robotControl.getRobotBounds())) {
                robotControl.updateInternalPositionFromVision(robot.getX(), robot.getY());
            }
        }

        return newMap;
    }

    private ArrayList<Robot> robotsInMap(ArrayList<SolidObject> newMap) {
        ArrayList<Robot> robots = new ArrayList<>();

        for (int i = 0; i < newMap.size(); i++) {
            if (newMap.get(i) instanceof Robot) {
                robots.add((Robot) newMap.get(i));
            }
        }

        return robots;
    }

    private boolean isObjectNearRobotBounds(int x, int y, Rectangle robotBounds) {
        if (Math.abs(robotBounds.getX() - x) > 20) {
            return false;
        }

        if (Math.abs(robotBounds.getY() - y) > 20) {
            return false;
        }

        return true;
    }
}
