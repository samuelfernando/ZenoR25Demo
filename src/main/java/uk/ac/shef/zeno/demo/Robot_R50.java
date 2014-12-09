package uk.ac.shef.zeno.demo;

import java.util.HashMap;


import org.robokind.api.animation.Animation;
import org.robokind.api.animation.messaging.RemoteAnimationPlayerClient;
import org.robokind.api.motion.messaging.RemoteRobot;
import org.robokind.api.speech.messaging.RemoteSpeechServiceClient;
import org.robokind.client.basic.Robokind;
import org.robokind.client.basic.UserSettings;
import static org.robokind.api.motion.Robot.*;

import uk.ac.shef.zeno.utils.Utils;

/**
 * Simple example of a synchronous module for the domain specified in
 * domains/examples/example-step-by-step.xml.
 *
 * <p>
 * The example creates a visual grid of size GRID_SIZE and updates the position
 * of the agent in accordance with the movements.
 *
 * @author Pierre Lison (plison@ifi.uio.no)
 * @version $Date:: 2014-04-16 17:34:31 #$
 */
public class Robot_R50 {

    // logger
    // start the agent in the middle of the grid
    RemoteSpeechServiceClient mySpeaker;
    RemoteAnimationPlayerClient animPlayer;
    RemoteRobot myRobot;
    RobotPositionMap storedPositions;
    boolean robotActive;
    HashMap<String, Animation> animations;

    public Robot_R50() {
        HashMap<String, String> configs = Utils.readConfig("C:\\Users\\samf\\Documents\\NetBeansProjects\\zeno-config.txt");
        animations = new HashMap<String, Animation>();
        Animation happyAnim = Robokind.loadAnimation("animations/victory.xml");
        Animation sadAnim = Robokind.loadAnimation("animations/disappointed.xml");
        Animation waveAnim = Robokind.loadAnimation("animations/wave-anim-r50.xml");
      
        animations.put("Happy", happyAnim);
        animations.put("Sad", sadAnim);
        animations.put("Wave", waveAnim);
      
        robotActive = Boolean.parseBoolean(configs.get("robot-active"));
        if (robotActive) {
            String robotID = "myRobot";
            String robotIP = configs.get("ip");
            UserSettings.setSpeechAddress(robotIP);
            UserSettings.setRobotId(robotID);
            UserSettings.setRobotAddress(robotIP);
            UserSettings.setAnimationAddress(robotIP);
            myRobot = Robokind.connectRobot();
            animPlayer = Robokind.connectAnimationPlayer();
            mySpeaker = Robokind.connectSpeechService();
        }
    }

    void speak(String text) {
        if (robotActive) {
            mySpeaker.speak(text);
        } else {
            System.out.println(text);
        }
    }

    void playAnimation(String name) {
        if (robotActive) {

            if (name.equals("Default")) {
                RobotPositionMap map = myRobot.getDefaultPositions();
                myRobot.move(map, 1000);
            } else {
                Animation anim = animations.get(name);
                animPlayer.playAnimation(anim);
            }
        } else {
            System.out.println("anim " + name);

        }

    }

}
