package com.uniquext.mouserecord.helper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MouseReplay {

    private Robot robot;
    private List<MouseRecord> mouseRecordList = new ArrayList<>();

    private int total = 100;

    public MouseReplay() throws AWTException {
        robot = new Robot();
        robot.setAutoWaitForIdle(true);
    }

    public void addRecord(MouseRecord record) {
        mouseRecordList.add(record);
    }

    public void execute() {
        while (total-- > 0) {
            for (MouseRecord record : mouseRecordList) {
                robot.delay((int) record.getDelay());
                robot.mouseMove(record.getPoint().x, record.getPoint().y);
                robot.mousePress(record.getButton());
                robot.delay(100);
                robot.mouseRelease(record.getButton());
            }
        }
    }



}
