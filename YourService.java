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

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import gov.nasa.arc.astrobee.Result;
import gov.nasa.arc.astrobee.android.gs.MessageType;
import gov.nasa.arc.astrobee.types.Point;
import gov.nasa.arc.astrobee.types.Quaternion;
import jp.jaxa.iss.kibo.rpc.api.KiboRpcService;

import static android.content.ContentValues.TAG;
import static org.opencv.aruco.Aruco.DICT_5X5_250;

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
        Log.i(TAG, "Start testing");

        final int loop_qrRead = 2;
        int numTryForMove = 0;

        // move Astrobee to P1-3
        numTryForMove = moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);
        Log.i(TAG, "moved to P1-3 after numTry = " + numTryForMove);

        // once Astrobee came to P1-3, get a camera image and read QR
        String valueZ = getQR();
        for (int i = 0; i < loop_qrRead && valueZ.equals(""); i++) {
            numTryForMove = moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);
            Log.i(TAG, "moved to current P1-3 after numTry = " + numTryForMove);

            valueZ = getQR();
        }
        // move to P1-3 again to ensure it's in the same orientation for every simulation
//        moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);
        Log.i(TAG, "valueZ = " + valueZ);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(2, valueZ);
        double valueZdouble = parseInfo(valueZ);


        // move Astrobee from the starting point to P1-1
        numTryForMove = moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
        Log.i(TAG, "moved to P1-1 after numTry = " + numTryForMove); // numTry 3 means fail to move there
        // once Astrobee came to P1-1, get a camera image and read QR
        String valueX = getQR();
        for (int i = 0; i < loop_qrRead && valueX.equals(""); i++) {
            numTryForMove = moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
            Log.i(TAG, "moved to current P1-1 after numTry = " + numTryForMove); // numTry 3 means fail to move there

            valueX = getQR();
        }
        // move to P1-1 again to ensure it's in the same orientation for every simulation
//        moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
        Log.i(TAG, "valueX = " + valueX);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(0, valueX);
        double valueXdouble = parseInfo(valueX);

        // move Astrobee to P1-2
        numTryForMove = moveToWrapper(11, -6, 5.55, 0, -0.7071068, 0, 0.7071068);
        Log.i(TAG, "moved to P1-2 after numTry = " + numTryForMove);
        // once Astrobee came to P1-2, get a camera image and read QR
        String valueY = getQR();
        for (int i = 0; i < loop_qrRead && valueY.equals(""); i++) {
            numTryForMove = moveToWrapper(11, -6, 5.55, 0, -0.7071068, 0, 0.7071068);
            Log.i(TAG, "moved to current P1-2 after numTry = " + numTryForMove);

            valueY = getQR();
        }
        // move to P1-2 again to ensure it's in the same orientation for every simulation
//        moveToWrapper(11, -6, 5.55, 0, -0.7071068, 0, 0.7071068);
        Log.i(TAG, "valueY = " + valueY);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(1, valueY);
        double valueYdouble = parseInfo(valueY);

        // move to Q1, an intermediate point
        numTryForMove = moveToWrapper(10.58,-6.45,5.37,0,0,-0.5,0.866);
        Log.i(TAG, "moved to Q1 after numTry = " + numTryForMove);

        // move Astrobee to P2-2
        numTryForMove = moveToWrapper(11.5,-8,5,0, 0, 0, 1);
        Log.i(TAG, "moved to P2-2 after numTry = " + numTryForMove);
        // once Astrobee came to P2-2, get a camera image and read QR
        String quaY = getQR();
        for (int i = 0; i < loop_qrRead && quaY.equals(""); i++) {
            numTryForMove = moveToWrapper(11.5,-8,5,0, 0, 0, 1);
            Log.i(TAG, "moved to current P2-2 after numTry = " + numTryForMove);

            quaY = getQR();
        }
        // move to P2-2 again to ensure it's in the same orientation for every simulation
//        moveToWrapper(11.5,-8,5,0, 0, 0, 1);
        Log.i(TAG, "quaY = " + quaY);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(4, quaY);
        double quaYdouble = parseInfo(quaY);

        // move Astrobee to P2-3
        numTryForMove = moveToWrapper(11,-7.7,5.55,0,-0.7071068,0,0.7071068);
        Log.i(TAG, "moved to P2-3 after numTry = " + numTryForMove);
        // once Astrobee came to P2-3, get a camera image and read QR
        String quaZ = getQR();
        for (int i = 0; i < loop_qrRead && quaZ.equals(""); i++) {
            numTryForMove = moveToWrapper(11,-7.7,5.55,0,-0.7071068,0,0.7071068);
            Log.i(TAG, "moved to current P2-3 after numTry = " + numTryForMove);

            quaZ = getQR();
        }
        // move to P2-3 again to ensure it's in the same orientation for every simulation
//        moveToWrapper(11,-7.7,5.55,0,-0.7071068,0,0.7071068)
        Log.i(TAG, "quaZ = " + quaZ);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(5, quaZ);
        double quaZdouble = parseInfo(quaZ);

        // move Astrobee to P2-1
        numTryForMove = moveToWrapper(10.30,-7.5,4.7,0,0,1,0);
        Log.i(TAG, "moved to P2-1 after numTry = " + numTryForMove);
        // once Astrobee came to P2-1, get a camera image and read QR
        String quaX = getQR();
        for (int i = 0; i < loop_qrRead && quaX.equals(""); i++) {
            numTryForMove = moveToWrapper(10.30,-7.5,4.7,0,0,1,0);
            Log.i(TAG, "moved to current P2-1 after numTry = " + numTryForMove);

            quaX = getQR();
        }
        // move to P2-1 again to ensure it's in the same orientation for every simulation
//        moveToWrapper(10.30,-7.5,4.7,0,0,1,0);
        Log.i(TAG, "quaX = " + quaX);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(3, quaX);
        double quaXdouble = parseInfo(quaX);

        // Calculate the w orientation of P3, assuming that w is positive (please check this again)
        double quaWdouble = Math.sqrt(1-quaXdouble*quaXdouble-quaYdouble*quaYdouble-quaZdouble*quaZdouble);
        Log.i(TAG, "quaW = " + quaWdouble);

        // Move to Q4, an intermediate point in the middle of the plane entering bay 7
        numTryForMove = moveToWrapper(10.95,-9.1, 4.9,0,0,-0.7071068,0.7071068);
        Log.i(TAG, "moved to Q4 after numTry = " + numTryForMove);

        // Attempt to move to P3 assuming it's in bay 7
        numTryForMove = moveToWrapper(valueXdouble, valueYdouble, valueZdouble, quaXdouble, quaYdouble, quaZdouble, quaWdouble);
        Log.i(TAG, "moved to P3 after numTry = " + numTryForMove);

        String arId = getAR();
        api.judgeSendDiscoveredAR(arId);
        Log.i(TAG,"AR ID = " + arId);

        // attempt to turn on laser control
        api.laserControl(true);
        Log.i(TAG, "laser on");

//        moveToWrapper(11.5, -5.7, 4.5, 0, -0.7071068, 0, 0.7071068);

        api.judgeSendFinishSimulation();
        sendData(MessageType.JSON, "data", "SUCCESS:defaultapk runPlan1");
        Log.i(TAG, "Finished testing");
        api.shutdownFactory();
    }

    @Override
    protected void runPlan2(){
        // write here your plan 2
        // start this run
//

    }

    @Override
    protected void runPlan3(){
        // write here your plan 3
        // start this run

    }
    // You can add your method
    private int moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w){
        // api.moveTO will be used for a maximum of 3 times
        // 0 or 1 or 2 will be returned if the robot succeeds after 1, 2 and 3 tries respectively
        // 3 will be returned if the robot doesn't succeed after 3 tries

        final int LOOP_MAX = 3; // actually this is the minimum
        final int LOOP_LIMIT = 6;
        final Point point = new Point(pos_x, pos_y, pos_z);
        final Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                (float)qua_z, (float)qua_w);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while((!result.hasSucceeded()||(loopCounter < LOOP_MAX)) && loopCounter < LOOP_LIMIT){
//          Log.i("MoveTo", "Try again for current point");
            result = api.moveTo(point, quaternion, true);
            ++loopCounter;
        }
        return loopCounter;
    }


    private String getQR() { // using Zxing
        String value = null;
        int loopCounter = 0;
        final int LOOP_MAX = 3; // 200 is actually too long
        for (loopCounter = 0; loopCounter < LOOP_MAX; loopCounter++) {
            Bitmap snapshot = api.getBitmapNavCam();
            Log.i("getQR", "snapshot acquired");
            value = readQRImage(snapshot);
            if (value != null) {
                Log.i("getQR", "number of QR tries = " + loopCounter);
                return value;
            }
//            if ((loopCounter) % 5 == 0) {
//                rotateRelativeWrapper('x'); // rotate by 90 degrees about x
//                Log.i("getQR", "rotated 90 degrees about x");
//            }
        }
        Log.i("getQR", "number of QR tries = " + loopCounter);
        return "";
    }

    private String readQRImage(Bitmap original) {
        Bitmap bMap = crop(original);
        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

//        MultiFormatReader reader = new MultiFormatReader();// use this otherwise ChecksumException
        Reader reader = new QRCodeReader();
        try {
//            Map<DecodeHintType,Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
//            tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            String contents = reader.decode(bitmap).getText();
//              String contents = reader.decode(bitmap, tmpHintsMap).getText(); // for TRY_HARDER
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
    private Bitmap crop(Bitmap source) {
        // use to crop the big bitmap returned from NavCam
//        Bitmap croppedBitmap = Bitmap.createBitmap(source, 425, 190, 430, 576); // setting 1
        Bitmap croppedBitmap = Bitmap.createBitmap(source, 510, 320, 340, 448); // setting 2
//        Bitmap croppedBitmap = Bitmap.createBitmap(source, 512, 352, 341, 396); // setting 3
//        Log.i("Crop", "done cropping");
        return croppedBitmap;
    }
    private Mat cropMat(Mat source) {
        // used to crop the big mat returned from NavCam

        Rect rectCrop = new Rect(510, 320, 340, 448); // setting 2
        return new Mat(source, rectCrop);
    }
    private double parseInfo(String source) {
        // source is the string read from QR code, we want to extract the coordinates/ orientations from there
        // source for coordinates is like "pos_y, -9.59056230087"
        // source for orientations is like "qua_y, -0.3336344341999481"
        // return type is a double to preserve accuracy

        return Double.parseDouble(source.substring(7));
    }
    private String ARdetect(Mat source) {
        // detect AR id
        Aruco ARaruco = new Aruco();
        Dictionary dictionary = ARaruco.getPredefinedDictionary(DICT_5X5_250);
        Mat markerIds = new Mat();
        ARaruco.detectMarkers(source,dictionary, null,markerIds);
        int id = (int)(markerIds.get(0, 0)[0]);
        return String.valueOf(id);
    }
    private String getAR() {
        // take a snapshot, get ARid
        Mat source = api.getMatNavCam();
        String value = ARdetect(source);
        int loopCounter = 0;
        final int LOOP_MAX = 3;
        while (loopCounter<= LOOP_MAX && value.equals("")) {
            source = api.getMatNavCam();
            value = ARdetect(source);
            loopCounter++;
        }
        Log.i("getAR","get AR after numTry = " + loopCounter);
        return value;
    }
}

