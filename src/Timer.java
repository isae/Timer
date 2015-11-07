
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Timer {

    private final static int SECONDS_PER_HOUR = 3600;
    public static final String START_BUTTON_TEXT = "Start";
    public static final String STOP_BUTTON_TEXT = "Stop";
    public static final String TIMER_STORAGE = "storage.txt";
    public static final int ONE_SECOND = 1000;
    private static final String PAUSE_BUTTON_TEXT = "Pause";


    private final JLabel timeLabel = new JLabel();
    private final JButton pauseButton = new JButton(PAUSE_BUTTON_TEXT);
    private final JButton startButton = new JButton(START_BUTTON_TEXT);
    private final JButton stopButton = new JButton(STOP_BUTTON_TEXT);
    private final CountTimer countTimer = new CountTimer();


    public Timer() {
        setTimerText("press \"" + START_BUTTON_TEXT + "\"");
        setUpGUI();
    }

    private void setUpGUI() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        timeLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.add(timeLabel, BorderLayout.NORTH);

        startButton.addActionListener((actionEvent) -> {
            countTimer.start();
        });

        pauseButton.addActionListener((actionEvent) -> {
            countTimer.stop();
        });

        JPanel cmdPanel = new JPanel();
        cmdPanel.setLayout(new GridLayout());
        cmdPanel.add(startButton);
        cmdPanel.add(pauseButton);
        cmdPanel.add(stopButton);
        panel.add(cmdPanel, BorderLayout.SOUTH);
        JPanel clrPanel = new JPanel();
        clrPanel.setLayout(new GridLayout(0, 1));
        panel.add(clrPanel, BorderLayout.EAST);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // JFrame.EXIT_ON_CLOSE
        frame.setVisible(true);
        frame.pack();
    }

    private void setTimerText(String sTime) {
        timeLabel.setText(sTime);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(Timer::new);
    }

    private class CountTimer implements ActionListener {
        private long count = 0;
        private boolean isTimerActive = false;
        private javax.swing.Timer tmr = new javax.swing.Timer(ONE_SECOND, this);

        public CountTimer() {
            count = 0;
            try {
                if (new File(TIMER_STORAGE).exists()) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(TIMER_STORAGE)))) {
                        String s = in.readLine();
                        if (s == null) {
                            count = 0;
                        } else {
                            if (s.startsWith("stopButton")) {
                                count = Integer.parseInt(s.substring(4));
                            } else {
                                count = (int) ((System.currentTimeMillis() - Long.parseLong(s.substring(5))) / 1000);
                            }
                        }
                    }
                } else {
                    backupTime();
                }
            } catch (Exception ignore) {
                backupTime();
            }
            setTimerText(timeFormat(count));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isTimerActive) {
                count++;
                setTimerText(timeFormat(count));
            }
        }

        public void start() {
            if (isTimerActive) {
                return;
            }
            isTimerActive = true;
            tmr.restart();

            try (PrintWriter printWriter = new PrintWriter(new FileWriter(TIMER_STORAGE))) {
                printWriter.println("start" + (System.currentTimeMillis() - (count * 1000)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void backupTime() {
            try (PrintWriter out = new PrintWriter(TIMER_STORAGE)) {
                out.print(count - (System.currentTimeMillis() / ONE_SECOND));
            } catch (IOException e) {
                throw new RuntimeException("Failed to write time");
            }
        }

        public void stop() {
            isTimerActive = false;
            try (PrintWriter printWriter = new PrintWriter(new FileWriter(TIMER_STORAGE))) {
                printWriter.println("stopButton" + count);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String timeFormat(long count) {
        long hours = count / SECONDS_PER_HOUR;
        long minutes = (count - hours * SECONDS_PER_HOUR) / SECONDS_PER_HOUR;
        long seconds = count - minutes * SECONDS_PER_HOUR;
        return String.format("%02d : %02d : %02d", hours, minutes, seconds);
    }
}