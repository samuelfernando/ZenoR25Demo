package uk.ac.shef.zeno.demo;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Simple example of an asynchronous module for the domain specified in
 * domains/examples/example-step-by-step.xml.
 *
 * <p>
 * The example creates a small control window where the user can click to
 * provide directions to the agent.
 *
 * @author Pierre Lison (plison@ifi.uio.no)
 * @version $Date:: 2014-04-16 17:34:31 #$
 */
public class ButtonWizard_R25 {

    // logger
    JFrame frame;
    MyActionListener listener;
    Robot_R25 robot;
    
    boolean shouldRun;
    HashSet<String> gestures;

    /**
     * Creates the example module. The module must have access to the dialogue
     * system since it will periodically write new content to it.
     *
     * @param system the dialogue system
     */

    public void start() {
        listener = new MyActionListener();
        frame = new JFrame();
        JPanel panel = new JPanel();

        GridLayout gridLayout = new GridLayout(5, 4);
        panel.setLayout(gridLayout);
        frame.add(panel);

        LinkedHashMap<String, String> utterances = readMap("resources/utter.txt");

        addButtons(panel, utterances);

        gestures = readSet("resources/gestures.txt");
        robot = new Robot_R25();
        frame.setSize(400, 300);
        frame.setTitle("R25-Control");
        // frame.setLocation(600, 600);
        frame.setVisible(true);
        shouldRun = true;
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                shouldRun = false;
            }
        });

        while (shouldRun) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        frame.dispose();
        System.exit(0);
    }

    private void addButtons(JPanel panel, LinkedHashMap<String, String> map) {
        for (String buttonName : map.keySet()) {
            MyButton button = new MyButton(buttonName, map.get(buttonName));
            button.addActionListener(listener);
            panel.add(button);
        }
    }

    private LinkedHashMap<String, String> readMap(String filename) {

        LinkedHashMap<String, String> res = new LinkedHashMap<String, String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
                res.put(fields[0], fields[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private HashSet<String> readSet(String filename) {

        HashSet<String> res = new HashSet<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                res.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String args[]) {
        ButtonWizard_R25 wizard = new ButtonWizard_R25();
        wizard.start();
    }

    /**
     * Action listener for the arrow buttons.
     */
    class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            try {
                MyButton source = (MyButton) arg0.getSource();
                String speakText = source.getSpeakText();
                if (gestures.contains(speakText)) {
                    robot.playAnimation(speakText);
                } else {
                    robot.speak(speakText);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MyButton extends JButton {

        String speakText;

        MyButton(String displayText, String speakText) {
            super(displayText);
            this.speakText = speakText;
        }

        String getSpeakText() {
            return speakText;
        }
    }
}
