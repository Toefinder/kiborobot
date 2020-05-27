package jp.jaxa.iss.kibo.rpc.sampleapk;

import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import com.google.zxing.*; // for QR code reader
/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        // start this run
        api.judgeSendStart();

        // move Astrobee from the starting point to P1-1
        moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
        // once Astrobee came to P1-1, get a camera image
        Bitmap snapshot = api.getBitmapNavCam();
        // read the QR code in the image and get the x-axis coordinate value of P3
        String valueX = decodeQR(snapshot);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(0, valueX);

        api.laserControl(true);
        moveToWrapper(11.5, -5.7, 4.5, 0, -0.7071068, 0, 0.7071068);

        api.judgeSendFinishSimulation();
        sendData(MessageType.JSON, "data", "SUCCESS:defaultapk runPlan1");
    }

    @Override
    protected void runPlan2(){
        // write here your plan 2
    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
    }

    // You can add your method
    private void moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w){

        final int LOOP_MAX = 3;
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                                                     (float)qua_z, (float)qua_w);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while(!result.hasSucceeded() || loopCounter < LOOP_MAX){
            result = api.moveTo(point, quaternion, true);
            ++loopCounter;
        }
    }

    private String decodeQR(Bitmap image) { // for QR code reading
        return new MultiFormatReader().decode(bitmap).getText();
    }

}

