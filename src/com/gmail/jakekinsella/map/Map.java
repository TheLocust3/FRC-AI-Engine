package com.gmail.jakekinsella.map;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.map.SolidObjects.*;
import com.gmail.jakekinsella.map.SolidObjects.Robot;
import com.gmail.jakekinsella.robot.RobotControl;
import com.gmail.jakekinsella.robot.pathing.PaddedLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Map implements Paintable {

    // 54ft 4in by 27ft
    // 1ft = 15px
    public final int FRAME_HEIGHT = 405, FRAME_WIDTH = 815;

    private final int ROBOTS_ON_FIELD = 6;
    private final int CLOSE_ENOUGH = 50;

    private final int WALL_THICKNESS = 3;
    private final int TOP_CORNER_LENGTH = 100; // Approximately the proper length
    private final int BOTTOM_CORNER_LENGTH = 62;
    private final int TOP_CORNER_ANGLE = 90 - 115;
    private final int BOTTOM_CORNER_ANGLE = 180 + 135;
    private final int LOADING_STATION_ANGLE = 66;

    private static final Logger logger = LogManager.getLogger();

    private ArrayList<SolidObject> map = this.createDefaultField(); // Map is on its side, 0,0 is the corner to the right of the blue tower
    private ArrayList<Wall> walls;

    public Map() {
        this.createWalls();
    }

    public void addObstacle(FuzzyObject obstacle) {
        map.add(obstacle);
    }

    public void tick(double deltaSeconds) {
        for (SolidObject solidObject : this.map) {
            solidObject.tick(deltaSeconds);
        }
    }

    public SolidObject getIntersection(PaddedLine paddedLine) {
        for (int i = 0; i < this.map.size(); i++) {
            if (!(this.map.get(i) instanceof Ball)) {
                if (this.map.get(i).doesIntersectWithPaddedLine(paddedLine)) {
                    return this.map.get(i);
                }
            }
        }

        return null;
    }

    public double getDistanceToClosestIntersectionWithWall(PaddedLine paddedLine) {
        double[] point;
        double closestDistance = Double.MAX_VALUE;

        for (Wall wall : this.walls) {
            if (wall.doesIntersect(paddedLine.getShape())) {
                point = wall.getIntersection(paddedLine);
                double distance = calculateDistanceBetweenPoints(paddedLine.getStartX(), paddedLine.getStartY(), point[0], point[1]);
                if (distance < closestDistance) {
                    closestDistance = distance;
                }
            }
        }

        if (closestDistance == Double.MAX_VALUE) {
            return -1;
        }

        return closestDistance;
    }

    // visionObjects = 0 -> type, 1 -> x, 2 -> y
    public void inputVisionData(ArrayList<double[]> visionObjects, RobotControl robotControl) {
        ArrayList<SolidObject> newMap = this.createDefaultField();

        for (SolidObject solidObject : this.map) {
            if (solidObject instanceof FuzzyObject && solidObject.isObjectReal()) {
                newMap.add(solidObject);
            }
        }

        for (double[] visionObject : visionObjects) {
            if (visionObject[0] == 0) {
                Robot robot = new Robot((int) visionObject[1], (int) visionObject[2], visionObject[3]);
                robot.setCenterX((int) visionObject[1]);
                robot.setCenterY((int) visionObject[2]);

                newMap.add(robot);
            } else { // Gears and balls are irrelevant to overall strategy
                logger.error("Caught unknown object from vision: " + visionObject[0]);
            }
        }

        newMap = this.tuneRobotsFromVision(newMap, robotControl);

        this.map = newMap;
    }

    @Override
    public void paint(Graphics2D graphics2D) {
        for (SolidObject solidObject : this.map) {
            solidObject.paint(graphics2D);
        }

        for (Wall wall : this.walls) {
            wall.paint(graphics2D);
        }
    }

    private double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void createWalls() {
        this.walls = new ArrayList<>();

        this.walls.add(new Wall(0, 0, this.WALL_THICKNESS, this.FRAME_HEIGHT, 0)); // left
        this.walls.add(new Wall(this.FRAME_WIDTH - this.WALL_THICKNESS, 0, this.WALL_THICKNESS, this.FRAME_HEIGHT, 0)); // right

        this.walls.add(new Wall(0, this.WALL_THICKNESS, this.WALL_THICKNESS, this.FRAME_WIDTH, -90)); // top
        this.walls.add(new Wall(0, this.FRAME_HEIGHT, this.WALL_THICKNESS, this.FRAME_WIDTH, -90)); // bottom

        this.walls.add(new Wall(0, 42, this.TOP_CORNER_LENGTH, this.WALL_THICKNESS, this.TOP_CORNER_ANGLE)); // top left corner
        this.walls.add(new Wall(this.FRAME_WIDTH - 92, 0, this.TOP_CORNER_LENGTH, this.WALL_THICKNESS, -this.TOP_CORNER_ANGLE)); // top right corner

        this.walls.add(new Wall(0, this.FRAME_HEIGHT - 47, this.BOTTOM_CORNER_LENGTH, this.WALL_THICKNESS, -this.BOTTOM_CORNER_ANGLE)); // bottom left corner
        this.walls.add(new Wall(this.FRAME_WIDTH - 47, this.FRAME_HEIGHT - 2, this.BOTTOM_CORNER_LENGTH, this.WALL_THICKNESS, this.BOTTOM_CORNER_ANGLE)); // bottom right corner
    }

    private ArrayList<SolidObject> createDefaultField() {
        ArrayList<SolidObject> newMap = new ArrayList<>();

        // Create blue loading stations
        newMap.add(new LoadingStation(25, 0, this.LOADING_STATION_ANGLE));
        newMap.add(new LoadingStation(60, -18, this.LOADING_STATION_ANGLE));

        // Create red loading stations
        newMap.add(new LoadingStation(this.FRAME_WIDTH - 25, 0, 90 - this.LOADING_STATION_ANGLE));
        newMap.add(new LoadingStation(this.FRAME_WIDTH - 60, -18, 90 - this.LOADING_STATION_ANGLE));

        newMap.add(new LoadingStation(500, 200, 0));
        newMap.add(new LoadingStation(500, 100, 0));

        /* No objects just to test the pathfinder
        // TODO: Properly place all of this crap

        newMap.add(new LoadingStation(50, 0));

        // Create red loading stations

        newMap.add(new LoadingStation(250, 600));

        // Create hopper triggers
        newMap.add(new HopperTrigger(100, 0));
        newMap.add(new HopperTrigger(400, 0));
        newMap.add(new HopperTrigger(700, 0));

        newMap.add(new HopperTrigger(200, 1000));
        newMap.add(new HopperTrigger(800, 1000));

        newMap.add(new Boiler(0, 1000));
        newMap.add(new Boiler(1000, 1000));*/

        return newMap;
    }

    private ArrayList<SolidObject> tuneRobotsFromVision(ArrayList<SolidObject> map, RobotControl robotControl) {
        ArrayList<Robot> robots = this.robotsInMap(map);

        if (robots.size() > this.ROBOTS_ON_FIELD) {
            ArrayList<Robot> condensedRobots = condenseRobotsCloseTogether(robots);

            if (condensedRobots.size() > this.ROBOTS_ON_FIELD) {
                for (int i = 0; i < condensedRobots.size(); i++) {
                    Robot tmp = condensedRobots.get(i);
                    tmp.setChanceObjectIsReal(0.2);
                    condensedRobots.set(i, tmp);
                }
            }

            for (int i = 0; i < map.size(); i++) {
                if (map.get(i) instanceof Robot) {
                    map.remove(i);
                }
            }

            map.addAll(condensedRobots);
        }

        for (Robot robot : robots) {
            if (this.isObjectNearRobotBounds(robot.getCenterX(), robot.getCenterY(), robotControl.getRobotBounds().getBounds())) {
                // TODO: Calculate confidenceModifier
                robotControl.updateInternalPositionFromVision(robot.getCenterX(), robot.getCenterY(), robot.getAngle(), 1);

                map.remove(robot);
            }
        }

        return map;
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

    private ArrayList<Robot> condenseRobotsCloseTogether(ArrayList<Robot> robots) {
        ArrayList<Robot> sortedRobots = this.sortRobotsByX(robots);
        // For every two x values check if their y values are close
        int index = 0;
        while (index != -1) {
            Robot robot1 = sortedRobots.get(index);
            Robot robot2 = sortedRobots.get(index + 1);
            sortedRobots.remove(index + 1);
            sortedRobots.set(index, new com.gmail.jakekinsella.map.SolidObjects.Robot(robot1, robot2));

            index = this.getIndexOfCloseTogetherRobot(sortedRobots);
        }

        return sortedRobots;
    }

    private ArrayList<Robot> sortRobotsByX(ArrayList<Robot> robots) {
        for (int i = 0; i < robots.size() - 1; i++) {
            int position = i;
            for (int j = i + 1; j < robots.size(); j++) {
                if (robots.get(j).getX() < robots.get(position).getX()) {
                    position = j;
                }
            }

            if (position != i) {
                Robot tmp = robots.get(position);
                robots.set(i, robots.get(position));
                robots.set(position, tmp);
            }
        }

        return robots;
    }

    private int getIndexOfCloseTogetherRobot(ArrayList<Robot> robots) {
        for (int i = 0; i < robots.size() - 1; i++) {
            if (Math.abs(robots.get(i).getX() - robots.get(i + 1).getX()) < this.CLOSE_ENOUGH) {
                if (Math.abs(robots.get(i).getY() - robots.get(i + 1).getY()) < this.CLOSE_ENOUGH) {
                    return i;
                }
            }
        }

        return -1;
    }

    private boolean isObjectNearRobotBounds(int centerX, int centerY, Rectangle robotBounds) {
        if (Math.abs(robotBounds.getCenterX() - centerX) > 30) {
            return false;
        }

        if (Math.abs(robotBounds.getCenterY() - centerY) > 30) {
            return false;
        }

        return true;
    }
}
