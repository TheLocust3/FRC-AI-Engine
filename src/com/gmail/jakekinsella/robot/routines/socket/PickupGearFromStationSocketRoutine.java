package com.gmail.jakekinsella.robot.routines.socket;

import com.gmail.jakekinsella.communicator.Communicator;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.Angle;
import com.gmail.jakekinsella.robot.RobotControl;
import com.gmail.jakekinsella.robot.routines.Routine;

/**
 * Created by jakekinsella on 4/11/17.
 */
public class PickupGearFromStationSocketRoutine extends Routine {

    private Angle angle;

    private boolean firstTime, doneTurning;
    private int shouldTurn;
    private double stationX, stationY;

    // 1 = blue leftmost
    // 2 = blue rightmost
    // 3 = red leftmost
    // 4 = red rightmost
    // TODO: allow pickup from middle stations
    public PickupGearFromStationSocketRoutine(int stationNumber, RobotControl robotControl, Communicator communicator) {
        super(robotControl, communicator);

        this.setupStationEndPoint(stationNumber);
        this.firstTime = true;
        this.shouldTurn = 0;
        this.doneTurning = false;
    }

    @Override
    public void execute(Map map) {
        if (this.firstTime) {
            this.robotControl.gotoLocation(this.stationX, this.stationY, map);

            this.firstTime = false;
        }

        if (!this.robotControl.isFollowingPath() && !this.doneTurning && this.shouldTurn < 2) {
            this.shouldTurn++;
            this.robotControl.turn(this.angle);
        }

        if (this.shouldTurn >= 2 && !this.doneTurning) {
            this.doneTurning = this.robotControl.getAngle().checkIfAnglesClose(this.angle, 1);
        }
    }

    @Override
    public boolean isFinished() {
        return !this.firstTime && !this.robotControl.isFollowingPath() && this.doneTurning;
    }

    // TODO: Make this DRY
    private void setupStationEndPoint(int stationNumber) {
        switch (stationNumber) {
            case 1:
                this.stationX = 60;
                this.stationY = 60;
                this.angle = new Angle(90 + Map.LOADING_STATION_ANGLE / 2);
                break;
            case 2:
                this.stationX = 70;
                this.stationY = 60;
                this.angle = new Angle(90 + Map.LOADING_STATION_ANGLE / 2);
                break;
            case 3:
                break;
            case 4:
                break;
        }
    }
}
