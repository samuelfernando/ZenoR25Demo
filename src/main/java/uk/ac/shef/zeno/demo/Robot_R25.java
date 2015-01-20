package uk.ac.shef.zeno.demo;

import org.mechio.api.speech.messaging.RemoteSpeechServiceClient;
import java.util.HashMap;
import org.mechio.api.animation.Animation;

import org.mechio.api.animation.messaging.RemoteAnimationPlayerClient;
import org.mechio.api.motion.Robot.RobotPositionMap;
import org.mechio.api.motion.messaging.RemoteRobot;
import org.mechio.client.basic.MechIO;
import org.mechio.client.basic.UserSettings;
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
public class Robot_R25 {

    // logger
    // start the agent in the middle of the grid
    RemoteSpeechServiceClient mySpeaker;
    RemoteAnimationPlayerClient animPlayer;
    RemoteRobot myRobot;
    RobotPositionMap storedPositions;
    boolean robotActive;
    HashMap<String, Animation> animations;

    public Robot_R25() {
        HashMap<String, String> configs = Utils.readConfig("C:\\Users\\samf\\Documents\\NetBeansProjects\\zeno-r25-config.txt");
        animations = new HashMap<String, Animation>();
        Animation happyAnim = MechIO.loadAnimation("animations/victory.xml");
        Animation sadAnim = MechIO.loadAnimation("animations/disappointed.xml");
        Animation waveAnim = MechIO.loadAnimation("animations/robokind-wave.xml");
        Animation onehandAnim = MechIO.loadAnimation("animations/robokind-onehandwave.xml");

        animations.put("Happy", happyAnim);
        animations.put("Sad", sadAnim);
        animations.put("Wave", waveAnim);
        animations.put("Onehand", onehandAnim);

        boolean rightBroken = Boolean.parseBoolean(configs.get("right-broken"));

        Animation startAnim, openMaxAnim, openMedAnim, closeAnim, presentAnim;

        if (rightBroken) {
            startAnim = MechIO.loadAnimation("animations/left-diamond-start.xml");
            openMaxAnim = MechIO.loadAnimation("animations/left-open-grasp-maximum.xml");
            openMedAnim = MechIO.loadAnimation("animations/left-open-grasp-medium.xml");
            closeAnim = MechIO.loadAnimation("animations/left-close-grasp.xml");
            presentAnim = MechIO.loadAnimation("animations/left-present-diamond.xml");
        } else {
            startAnim = MechIO.loadAnimation("animations/diamond-start.xml");
            openMaxAnim = MechIO.loadAnimation("animations/open-grasp-maximum.xml");
            openMedAnim = MechIO.loadAnimation("animations/open-grasp-medium.xml");
            closeAnim = MechIO.loadAnimation("animations/close-grasp.xml");
            presentAnim = MechIO.loadAnimation("animations/present-diamond.xml");

        }
        animations.put("start", startAnim);
        animations.put("openMax", openMaxAnim);
        animations.put("openMed", openMedAnim);
        animations.put("close", closeAnim);
        animations.put("present", presentAnim);

        robotActive = Boolean.parseBoolean(configs.get("robot-active"));
        if (robotActive) {
            String robotID = "myRobot";
            String robotIP = configs.get("ip");
            UserSettings.setSpeechAddress(robotIP);
            UserSettings.setRobotId(robotID);
            UserSettings.setRobotAddress(robotIP);
            UserSettings.setAnimationAddress(robotIP);
            myRobot = MechIO.connectRobot();
            animPlayer = MechIO.connectAnimationPlayer();
            mySpeaker = MechIO.connectSpeechService();
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
