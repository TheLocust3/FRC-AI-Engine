package com.gmail.jakekinsella.robot;

/**
 * Created by jakekinsella on 1/31/17.
 */
public class PathPart {

    private boolean finished;

    private RotatedRectangle line;

    public PathPart(RotatedRectangle line) {
        this.line = line;
        this.finished = false;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void execute() {
        // TODO: follow the line

        this.finished = true;
    }
}
