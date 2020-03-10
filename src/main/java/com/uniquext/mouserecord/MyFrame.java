package com.uniquext.mouserecord;

import com.uniquext.mouserecord.helper.MouseRecord;
import com.uniquext.mouserecord.helper.MouseReplay;
import com.uniquext.mouserecord.helper.MouseSimulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MyFrame extends JFrame {

    private static final int SIMULATION = 0x00;
    private static final int RECORD = 0x01;
    private static final int REPLAY = 0x10;


    private MouseReplay replay = new MouseReplay();
    private MouseSimulation simulation = new MouseSimulation();


    private volatile int status = SIMULATION;
    private long currentTimestamp = 0L;

    public MyFrame() throws AWTException {
        this.setLayout(null);
        this.setAlwaysOnTop(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                initOperational();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (status == SIMULATION) {
                    simulation(e);
                } else if (status == RECORD) {
                    record(e);
                    simulation(e);
                }
            }
        });


        this.setUndecorated(true);
        this.setBackground(new Color(0,0,0,0));
        this.setVisible(true);
    }

    private void initOperational() {

        int width = 100;
        int height = 50;

        JButton exitButton = new JButton("关闭");
        exitButton.setBounds(getWidth() - width, height * 0, width, height);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.add(exitButton);

        JButton recordButton = new JButton("录制");
        recordButton.setBounds(getWidth() - width, height * 1, width, height);
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = RECORD;
            }
        });
        this.add(recordButton);

        JButton replayButton = new JButton("开始");
        replayButton.setBounds(getWidth() - width, height * 2, width, height);
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = REPLAY;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        replay.execute();
                    }
                }).start();
            }
        });
        this.add(replayButton);
    }

    private void record(MouseEvent e) {
        final long timestamp = System.currentTimeMillis();
        final long delay = currentTimestamp == 0L ? 3000L : timestamp - currentTimestamp;
        currentTimestamp = timestamp;
        replay.addRecord(new MouseRecord(delay, e.getLocationOnScreen(), MouseSimulation.transformButton(e.getButton())));
    }

    private void simulation(final MouseEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
                simulation.execute(e);
                simulation.execute(e);
                setVisible(true);
            }
        }).start();
    }

}


