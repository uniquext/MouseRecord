package com.uniquext.mouserecord.helper;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class MouseSimulation {

    private Robot robot;


    public MouseSimulation() throws AWTException {
        robot = new Robot();
        robot.setAutoWaitForIdle(true);
    }

    public void execute(MouseEvent event) {
        robot.mouseMove(event.getXOnScreen(), event.getYOnScreen());
        robot.mousePress(transformButton(event.getButton()));
        robot.delay(100);
        robot.mouseRelease(transformButton(event.getButton()));
    }

    public static int transformButton(int button) {
        switch (button) {
            case MouseEvent.BUTTON2:
                return InputEvent.BUTTON2_DOWN_MASK;
            case MouseEvent.BUTTON3:
                return InputEvent.BUTTON3_DOWN_MASK;
            case MouseEvent.BUTTON1:
            default:
                return InputEvent.BUTTON1_DOWN_MASK;
        }
    }
}
