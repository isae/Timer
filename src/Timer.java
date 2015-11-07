
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Timer implements ActionListener {

    private final static int SECONDS_PER_HOUR = 3600;

    private JLabel timeLabel = new JLabel();
    private JButton pauseButton = new JButton("Stop");
    private JButton resumeButton = new JButton("Start");
    private CountTimer countTimer;

    public Timer() {
        setTimerText("         ");
        GUI();
    }

    private void GUI() {
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
        countTimer = new CountTimer();
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