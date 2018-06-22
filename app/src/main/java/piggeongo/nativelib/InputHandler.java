package piggeongo.nativelib;

import com.app.tanapoom.piggeongo.MainActivity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Poom on 5/30/2018.
 */

public class InputHandler {

    static HashMap<Integer, float[]> touchEvent = new HashMap<>();;
    static PiggeonSensorEventListener piggeonSensorEventListener;


    public static void init(PiggeonSensorEventListener piggeonSensorEventListener){
        touchEvent = new HashMap<>();
        InputHandler.piggeonSensorEventListener = piggeonSensorEventListener;
    }

    public static void addTouchEvent(float x, float y, int pointerid){
        touchEvent.put(pointerid, new float[]{x,y});
    }

    public static HashMap<Integer, float[]> getTouchEvents(){
        return touchEvent;
    }

    public static ArrayList<float[]> getTouchCoordinatesCompensated(){
        ArrayList<float[]> compensated = new ArrayList<>();
        try {
            for (float[] raw : touchEvent.values()) {
                float posX = ((float) MainActivity.gameWidth) * raw[0] / ((float) MainActivity.screenWidth);
                float posY = ((float) MainActivity.gameHeight) * raw[1] / ((float) MainActivity.screenHeight);

                compensated.add(new float[]{posX, posY});
            }
        }catch(ConcurrentModificationException ex){}

        return compensated;
    }

    public static void removeTouchEvent(float x, float y, int pointerid){
        touchEvent.remove(pointerid);
    }

    public static void updateOrientationAngles(){
        piggeonSensorEventListener.updateOrientationAngles();
    }

    public static float getPitch(){
        return piggeonSensorEventListener.getPitch();
    }

    public static float getYaw(){
        return piggeonSensorEventListener.getYaw();
    }

    public static float getRoll(){
        return piggeonSensorEventListener.getRoll();
    }
}
