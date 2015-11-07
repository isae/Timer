
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener {
    private JLabel timeLabel = new JLabel();
    private JButton pauseBtn = new JButton("Stop");
    private JButton resumeBtn = new JButton("Start");
    private CountTimer countTimer;

    public Main() {
        setTimerText("         ");
        GUI();
    }

    private void GUI() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        timeLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.add(timeLabel, BorderLayout.NORTH);
        pauseBtn.addActionListener(this);
        resumeBtn.addActionListener(this);
        JPanel cmdPanel = new JPanel();
        cmdPanel.setLayout(new GridLayout());
        cmdPanel.add(resumeBtn);
        cmdPanel.add(pauseBtn);
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
        JButton btn = (JButton) e.getSource();
        if (btn.equals(pauseBtn)) {
            countTimer.stop();
        } else if (btn.equals(resumeBtn)) {
            countTimer.start();
        }
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(Main::new);
    }

    private class CountTimer implements ActionListener {
        private static final int ONE_SECOND = 1000;
        private int count = 0;
        private boolean isTimerActive = false;
        private Timer tmr = new Timer(ONE_SECOND, this);

        public CountTimer() {
            count = 0;
            setTimerText(TimeFormat(count));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isTimerActive) {
                count++;
                setTimerText(TimeFormat(count));
            }
        }

        public void start() {
            isTimerActive = true;
            tmr.restart();
        }

        public void stop() {
            isTimerActive = false;
        }
    }

    private String TimeFormat(int count) {
        int hours = count / 3600;
        int minutes = (count - hours * 3600) / 60;
        int seconds = count - minutes * 60;
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
}