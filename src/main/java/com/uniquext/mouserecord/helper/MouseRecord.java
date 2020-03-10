package com.uniquext.mouserecord.helper;

import java.awt.*;

public class MouseRecord {

    private long delay;
    private Point point;
    private int button;

    public MouseRecord(long delay, Point point, int button) {
        this.delay = delay;
        this.point = point;
        this.button = button;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

}
