package piggeongo.nativelib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import piggeongo.piggeon.Stage;

/**
 * Created by Poom on 3/2/2018.
 */

public class PiggeonGLSurfaceView extends GLSurfaceView {

    private SensorManager mSensorManager;
    private Sensor mSensorAccel;
    private Sensor mSensorMagnet;
    private SensorEventListener sensorEventListener;

    public static int canvasHeight;
    public static int canvasWidth;
    public static Context context;

    private final PiggeonGLRenderer glRenderer;

    public PiggeonGLSurfaceView(Context context, int canvasWidth, int canvasHeight, Stage stage) {
        super(context);

        //establish sensors
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnet = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorEventListener = new PiggeonSensorEventListener(mSensorManager);
        //

        InputHandler.init((PiggeonSensorEventListener)sensorEventListener);

        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        PiggeonGLSurfaceView.context = context;

        //create opengl es 2.0 context
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);

        glRenderer = new PiggeonGLRenderer(stage);

        setRenderer(glRenderer);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener
                , mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(sensorEventListener
                , mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int index = e.getActionIndex();
        int id = e.getPointerId(index);
        int action = e.getActionMasked();
        float x = e.getX(index);
        float y = e.getY(index);

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            InputHandler.addTouchEvent(x, y, id);
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            InputHandler.removeTouchEvent(x, y, id);
        } else if (action == MotionEvent.ACTION_MOVE){
            InputHandler.addTouchEvent(x, y, id);
        }

        return true;
    }

    public PiggeonGLRenderer getRenderer(){
        return glRenderer;
    }

}
