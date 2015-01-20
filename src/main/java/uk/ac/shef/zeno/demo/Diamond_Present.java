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
import java.util.Set;

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
public class Diamond_Present {

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
    void send(String text) {
        if (text.equals("Defaults")) {
            System.out.println("Defaults");
            robot.playAnimation("Default");
        } else if (text.equals("Start Position")) {
            robot.playAnimation("start");
            System.out.println("Start Position");

        } else if (text.equals("Open grasp max")) {
           robot.playAnimation("openMax");
       
            System.out.println("Open grasp max");

        } else if (text.equals("Open grasp medium")) {
            robot.playAnimation("openMed");
       
            System.out.println("Open grasp medium");

        } else if (text.equals("Close grasp")) {
            robot.playAnimation("close");
       
            System.out.println("Close grasp");

        } else if (text.equals("Present diamond")) {
            robot.playAnimation("present");
       robot.speak("Please accept this Diamond coaster as a token to remember this day by.");
            System.out.println("Present diamond");

        }
    }

    public void start() {
        listener = new MyActionListener(this);
        frame = new JFrame();
        JPanel panel = new JPanel();

        GridLayout gridLayout = new GridLayout(6, 1);
        panel.setLayout(gridLayout);
        frame.add(panel);

        Set<String> utterances = readSet("resources/diamond.txt");

        addButtons(panel, utterances);

        //gestures = readSet("resources/gestures.txt");
       robot = new Robot_R25();
        frame.setSize(400, 800);
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

    private void addButtons(JPanel panel, Set<String> set) {
        for (String buttonName : set) {
            JButton button = new JButton(buttonName);
            button.addActionListener(listener);
            panel.add(button);
        }
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
        Diamond_Present wizard = new Diamond_Present();
        wizard.start();
    }

    /**
     * Action listener for the arrow buttons.
     */
    class MyActionListener implements ActionListener {

        Diamond_Present master;

        public MyActionListener(Diamond_Present master) {
            this.master = master;
        }

        public void actionPerformed(ActionEvent arg0) {
            try {
                JButton source = (JButton) arg0.getSource();
                String text = source.getText();
                //System.out.println(text);
                master.send(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
