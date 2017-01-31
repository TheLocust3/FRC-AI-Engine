package com.gmail.jakekinsella.map;

import com.gmail.jakekinsella.map.SolidObjects.*;
import com.gmail.jakekinsella.robot.RobotControl;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Map {

    private final int ROBOTS_ON_FIELD = 6;
    private final int CLOSE_ENOUGH = 50;

    private ArrayList<SolidObject> map = this.createDefaultField(); // Map is on its side, 0,0 is the corner to the right of the blue tower

    public void addObstacle(FuzzyObject obstacle) {
        map.add(obstacle);
    }

    public void tick(double deltaSeconds) {
        for (SolidObject solidObject : this.map) {
            solidObject.tick(deltaSeconds);
        }
    }

    public SolidObject getIntersection(Shape shape) {
        for (int i = 0; i < this.map.size(); i++) {
            return this.map.get(i);
        }

        return null;
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
                    newMap.add(new com.gmail.jakekinsella.map.SolidObjects.Robot(visionObject[0], visionObject[1]));
                case 1:
                    newMap.add(new Ball(visionObject[0], visionObject[1]));
            }
        }

        newMap = this.tuneRobotsFromVision(newMap, robotControl);

        this.map = newMap;
    }

    private ArrayList<SolidObject> createDefaultField() {
        ArrayList<SolidObject> newMap = new ArrayList<>();

        // Create blue loading stations
        newMap.add(new LoadingStation(0, 0));
        newMap.add(new LoadingStation(50, 0));

        newMap.add(new LoadingStation(1000, 500));

        // Create red loading stations
        newMap.add(new LoadingStation(950, 0));
        newMap.add(new LoadingStation(1000, 0));

        newMap.add(new LoadingStation(250, 600));

        // Create hopper triggers
        newMap.add(new HopperTrigger(100, 0));
        newMap.add(new HopperTrigger(400, 0));
        newMap.add(new HopperTrigger(700, 0));

        newMap.add(new HopperTrigger(200, 1000));
        newMap.add(new HopperTrigger(800, 1000));

        newMap.add(new Boiler(0, 1000));
        newMap.add(new Boiler(1000, 1000));

        return newMap;
    }

    private ArrayList<SolidObject> tuneRobotsFromVision(ArrayList<SolidObject> map, RobotControl robotControl) {
        ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> robots = robotsInMap(map);

        if (robots.size() > this.ROBOTS_ON_FIELD) {
            ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> condensedRobots = condenseRobotsCloseTogether(robots);

            if (condensedRobots.size() > this.ROBOTS_ON_FIELD) {
                for (int i = 0; i < condensedRobots.size(); i++) {
                    com.gmail.jakekinsella.map.SolidObjects.Robot tmp = condensedRobots.get(i);
                    tmp.setChanceObjectIsReal(0.2);
                    condensedRobots.set(i, tmp);
                }
            }

            for (int i = 0; i < map.size(); i++) {
                if (map.get(i) instanceof com.gmail.jakekinsella.map.SolidObjects.Robot) {
                    map.remove(i);
                }
            }

            map.addAll(condensedRobots);
        }

        for (com.gmail.jakekinsella.map.SolidObjects.Robot robot : robots) {
            if (this.isObjectNearRobotBounds(robot.getX(), robot.getY(), robotControl.getRobotBounds().getBounds())) {
                robotControl.updateInternalPositionFromVision(robot.getX(), robot.getY());
            }
        }

        return map;
    }

    private ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> robotsInMap(ArrayList<SolidObject> newMap) {
        ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> robots = new ArrayList<>();

        for (int i = 0; i < newMap.size(); i++) {
            if (newMap.get(i) instanceof com.gmail.jakekinsella.map.SolidObjects.Robot) {
                robots.add((com.gmail.jakekinsella.map.SolidObjects.Robot) newMap.get(i));
            }
        }

        return robots;
    }

    private ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> condenseRobotsCloseTogether(ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> robots) {
        ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> sortedRobots = this.sortRobotsByX(robots);
        // For every two x values check if their y values are close
        int index = 0;
        while (index != -1) {
            com.gmail.jakekinsella.map.SolidObjects.Robot robot1 = sortedRobots.get(index);
            com.gmail.jakekinsella.map.SolidObjects.Robot robot2 = sortedRobots.get(index + 1);
            sortedRobots.remove(index + 1);
            sortedRobots.set(index, new com.gmail.jakekinsella.map.SolidObjects.Robot(robot1, robot2));

            index = this.getIndexOfCloseTogetherRobot(sortedRobots);
        }

        return sortedRobots;
    }

    private ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> sortRobotsByX(ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> robots) {
        for (int i = 0; i < robots.size() - 1; i++) {
            int position = i;
            for (int j = i + 1; j < robots.size(); j++) {
                if (robots.get(j).getX() < robots.get(position).getX()) {
                    position = j;
                }
            }

            if (position != i) {
                com.gmail.jakekinsella.map.SolidObjects.Robot tmp = robots.get(position);
                robots.set(i, robots.get(position));
                robots.set(position, tmp);
            }
        }

        return robots;
    }

    private int getIndexOfCloseTogetherRobot(ArrayList<com.gmail.jakekinsella.map.SolidObjects.Robot> robots) {
        for (int i = 0; i < robots.size() - 1; i++) {
            if (Math.abs(robots.get(i).getX() - robots.get(i + 1).getX()) < this.CLOSE_ENOUGH) {
                if (Math.abs(robots.get(i).getY() - robots.get(i + 1).getY()) < this.CLOSE_ENOUGH) {
                    return i;
                }
            }
        }

        return -1;
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
