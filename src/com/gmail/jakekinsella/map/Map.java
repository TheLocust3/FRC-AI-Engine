package com.gmail.jakekinsella.map;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 12/19/16.
 */
public class Map {

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
    public void inputVisionData(ArrayList<int[]> visionObjects) {
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
                    // TODO: Check if the robot is close to the real robot and if it is then set position in-between the two values
                case 1:
                    newMap.add(new Ball(visionObject[0], visionObject[1]));
            }
        }
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
}
