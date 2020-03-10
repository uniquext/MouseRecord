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


    private static final int WIDTH_BUTTON = 100;
    private static final int HEIGHT_BUTTON = 50;
    private static final int WIDTH_FRAME = 150;
    private static final int HEIGHT_FRAME = 200;

    private MouseReplay replay = new MouseReplay();
    private MouseSimulation simulation = new MouseSimulation();


    private volatile int status = SIMULATION;
    private long currentTimestamp = 0L;

    private Dimension screenSize;

    public MyFrame() throws AWTException {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width - 150, 0);
        setSize(WIDTH_FRAME, HEIGHT_FRAME);

        this.setLayout(null);
        initOperational();


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


        this.addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (JFrame.MAXIMIZED_BOTH == e.getNewState()) {
                    exitButton.setBounds(screenSize.width - WIDTH_BUTTON, HEIGHT_BUTTON * 0, WIDTH_BUTTON, HEIGHT_BUTTON);
                    recordButton.setBounds(screenSize.width - WIDTH_BUTTON, HEIGHT_BUTTON * 1, WIDTH_BUTTON, HEIGHT_BUTTON);
                    replayButton.setBounds(screenSize.width - WIDTH_BUTTON, HEIGHT_BUTTON * 2, WIDTH_BUTTON, HEIGHT_BUTTON);
                } else if (JFrame.NORMAL == e.getNewState()) {
                    exitButton.setBounds(WIDTH_FRAME - WIDTH_BUTTON, HEIGHT_BUTTON * 0, WIDTH_BUTTON, HEIGHT_BUTTON);
                    recordButton.setBounds(WIDTH_FRAME - WIDTH_BUTTON, HEIGHT_BUTTON * 1, WIDTH_BUTTON, HEIGHT_BUTTON);
                    replayButton.setBounds(WIDTH_FRAME - WIDTH_BUTTON, HEIGHT_BUTTON * 2, WIDTH_BUTTON, HEIGHT_BUTTON);
                }
            }
        });
    }

    JButton exitButton;
    JButton recordButton;
    JButton replayButton;

    private void initOperational() {
        exitButton = new JButton("关闭");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.add(exitButton);

        recordButton = new JButton("录制");
        recordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = RECORD;
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        this.add(recordButton);

        replayButton = new JButton("开始");
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = REPLAY;
                setExtendedState(JFrame.NORMAL);
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


