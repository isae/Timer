
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Timer implements ActionListener {

    private final static int SECONDS_PER_HOUR = 3600;
    public static final String START_BUTTON_TEXT = "Start";
    public static final String STOP_BUTTON_TEXT = "Stop";
    public static final String TIMER_BEGINNING_FILE = "timerBackup.txt";


    private final JLabel timeLabel = new JLabel();
    private final JButton pauseButton = new JButton(STOP_BUTTON_TEXT);
    private final JButton resumeButton = new JButton(START_BUTTON_TEXT);
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
        pauseButton.addActionListener(this);
        resumeButton.addActionListener(this);
        JPanel cmdPanel = new JPanel();
        cmdPanel.setLayout(new GridLayout());
        cmdPanel.add(resumeButton);
        cmdPanel.add(pauseButton);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton Button = (JButton) e.getSource();
        if (Button.equals(pauseButton)) {
            countTimer.stop();
        } else if (Button.equals(resumeButton)) {
            countTimer.start();
        }
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(Timer::new);
    }

    private class CountTimer implements ActionListener {
        private static final int ONE_SECOND = 1000;
        private int count = 0;
        private boolean isTimerActive = false;
        private javax.swing.Timer tmr = new javax.swing.Timer(ONE_SECOND, this);

        public CountTimer() {
            count = 0;
            try {
                if (new File(TIMER_BEGINNING_FILE).exists()) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(TIMER_BEGINNING_FILE)))) {
                        String line = in.readLine();
                        count = Integer.parseInt(line);
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
        }

        private void backupTime() {
            try {
                try (ConcurrentOutputStream stream = new ConcurrentOutputStream(TIMER_BEGINNING_FILE);
                     PrintWriter out = new PrintWriter(stream)) {
                    out.print(count);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to write time");
            }
        }

        public void stop() {
            isTimerActive = false;
        }
    }

    private String timeFormat(int count) {
        int hours = count / SECONDS_PER_HOUR;
        int minutes = (count - hours * SECONDS_PER_HOUR) / SECONDS_PER_HOUR;
        int seconds = count - minutes * SECONDS_PER_HOUR;
        return String.format("%02d : %02d : %02d", hours, minutes, seconds);
    }
}