package jp.jaxa.iss.kibo.rpc.defaultapk;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.android.gs.MessageType;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import static android.content.ContentValues.TAG;

/**
 * Class meant to handle commands from the Ground Data System and execute them in Astrobee
 */

public class YourService extends KiboRpcService {
    @Override
    protected void runPlan1(){
        // write here your plan 1
        // start this run
        api.judgeSendStart();

        String TAG = "MyActivity";

        String valueX = "";
        String valueY = "";
        String valueZ = "";
        final int loop_qrRead = 2;

        // move Astrobee from the starting point to P1-1
        moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
        // once Astrobee came to P1-1, get a camera image and read QR

        for (int i = 0; i < loop_qrRead; i++) {
            valueX = getQR();
            if (!valueX.equals("")) {
                break;
            }
//            moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
            Point relativeCloser = new Point(0.05, 0, 0);
            Quaternion relativeOrientation = new Quaternion(0,0,0,1);
            api.relativeMoveTo(relativeCloser, relativeOrientation, true);
            Log.i(TAG, "moved closer to P1-1");
        }
        // move to P1-1 again to ensure it's in the same orientation for every simulation
        moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);
        Log.i(TAG, "valueX = " + valueX);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(0, valueX);


        // move Astrobee from the starting point to P1-2
        // once Astrobee came to P1-2, get a camera image and read QR
        for (int i = 0; i < loop_qrRead; i++) {
            if (!valueY.equals("")) {
                break;
            }
            moveToWrapper(11, -6, 5.55, 0, -0.7071068, 0, 0.7071068);
            Log.i(TAG, "moved to current P1-2");
            valueY = getQR();
        }
        // move to P1-2 again to ensure it's in the same orientation for every simulation
        moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);
        Log.i(TAG, "valueY = " + valueY);

        // send the result to scoring module
        api.judgeSendDiscoveredQR(1, valueY);


        // move Astrobee from the starting point to P1-3
        // once Astrobee came to P1-3, get a camera image and read QR
        for (int i = 0; i < loop_qrRead; i++) {
            if (!valueY.equals("")) {
                break;
            }
            moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);
            Log.i(TAG, "moved to current P1-3");
            valueY = getQR();
        }
        // move to P1-3 again to ensure it's in the same orientation for every simulation
        moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);

        Log.i(TAG, "valueZ = " + valueZ);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(2, valueZ);

//        api.laserControl(true);
//        moveToWrapper(11.5, -5.7, 4.5, 0, -0.7071068, 0, 0.7071068);

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

    private void rotateRelativeWrapper(char axis){
        // rotate by 90 degrees relative to current orientation, around a principal axis

        final int LOOP_MAX = 3;
        final Point point = new Point(0, 0, 0);
        Quaternion quaternion;
        if (axis == 'x') {
            quaternion = new Quaternion((float) 0.7071068, (float) 0,
                    (float) 0, (float) 0.7071068);
        } else if (axis == 'y') {
            quaternion = new Quaternion((float) 0, (float) 0.7071068,
                    (float) 0, (float) 0.7071068);
        } else {
            quaternion = new Quaternion((float) 0, (float) 0,
                    (float) 0.7071068, (float) 0.7071068);
        }
        Result result = api.relativeMoveTo(point, quaternion, true);

        int loopCounter = 0;
        while(!result.hasSucceeded() || loopCounter < LOOP_MAX){
            result = api.relativeMoveTo(point, quaternion, true);
            ++loopCounter;
        }
    }
    private String getQR() {
        String value = null;
        int loopCounter = 0;
        final int LOOP_MAX = 5; // 200 is actually too long
        while (loopCounter < LOOP_MAX) {
            Bitmap snapshot = api.getBitmapNavCam();
            Log.i("Ok", "snapshot acquired");
            value = readQRImage(snapshot);
            if (value != null) {
                return value;
            }
            rotateRelativeWrapper('x'); // rotate by 90 degrees about x
            Log.i("Ok", "rotated 90 degrees about x");
            loopCounter++;
        }

        return "";

    }
    private String readQRImage(Bitmap bMap) {
        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

//        MultiFormatReader reader = new MultiFormatReader();// use this otherwise ChecksumException
        Reader reader = new QRCodeReader();
        try {
//            String contents = reader.decode(bitmap).getText();
              String contents = reader.decode(bitmap).getText();
            return contents;
        } catch (NotFoundException e) {
            Log.e(TAG, "not found exception", e);
            return null;
        } catch (ChecksumException e) {
            Log.e(TAG, "checksum exception", e);
            return null;
        }
        catch (FormatException e) {
            Log.e(TAG, "format exception", e);
            return null;
        }
    }
}

