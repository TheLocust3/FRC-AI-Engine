package com.gmail.jakekinsella.ai;

import com.gmail.jakekinsella.ai.sequences.GetGearFromLoadingStationSequence;
import com.gmail.jakekinsella.ai.sequences.Sequence;
import com.gmail.jakekinsella.map.Map;
import com.gmail.jakekinsella.robot.RobotControl;

/**
 * Created by jakekinsella on 3/27/17.
 */
public class SimpleAI extends GenericAI {

    private Sequence currentSequence;

    public SimpleAI(RobotControl robotControl) {
        super(robotControl);
    }

    @Override
    public void tick(double deltaSeconds, Map map) {
        if (this.currentSequence != null && this.currentSequence.isRunning()) {
            this.currentSequence.run(deltaSeconds, map);
        } else {

        }
    }

    private Sequence decideNextSequence(Map map) {
        // Check if robot has gear
        return new GetGearFromLoadingStationSequence(robotControl);
    }
}
