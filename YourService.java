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

import java.util.ArrayList;

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

        final int loop_qrRead = 3;
        int numTryForMove = 0;

        // move Astrobee to P1-3
        numTryForMove = moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);
        Log.i(TAG, "moved to P1-3 after numTry = " + numTryForMove);

        if (numTryForMove >=6) { // in case it hasn't been able to
            numTryForMove = moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);
            Log.i(TAG, "moved to P1-3 again after numTry = " + numTryForMove);
        }

        // once Astrobee came to P1-3, get a camera image and read QR
        String valueZ = getQR();
        for (int i = 0; i < loop_qrRead && valueZ.equals(""); i++) {
            numTryForMove = moveToWrapper(11, -5.5, 4.33, 0,0.7071068, 0, 0.7071068);
            Log.i(TAG, "moved to current P1-3 after numTry = " + numTryForMove);


            valueZ = getQR();
        }
        // move to P1-3 again to ensure it's in the same orientation for every simulation
//        moveToWrapper(11, -5.5, 4.33, 0, 0.7071068, 0, 0.7071068);
        Log.i(TAG, "valueZ = " + valueZ);
        // send the result to scoring module
        api.judgeSendDiscoveredQR(2, valueZ);
        double valueZdouble = parseInfo(valueZ);


//        //// Testing moveCloserWrapper
//        numTryForMove = moveToWrapper(11, -5.5, 4.33, 0, 1, 0, 0);
//        Log.i(TAG, "rotated at P1-3 after numTry = " + numTryForMove);
//
//        numTryForMove = moveCloserWrapper(11, -5.5, 4.33, 0, 1, 0, 0);
//        Log.i(TAG, "move closer in the same orientation after numTry = " + numTryForMove);
//        //// End of testing


        // move Astrobee from the starting point to P1-1
        numTryForMove = moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
        Log.i(TAG, "moved to P1-1 after numTry = " + numTryForMove); // numTry 3 means fail to move there

        if (numTryForMove >=6) { // in case it hasn't been able to
            numTryForMove = moveToWrapper(11.5, -5.7, 4.5, 0, 0, 0, 1);
            Log.i(TAG, "moved to P1-1 again after numTry = " + numTryForMove); // numTry 3 means fail to move there
        }

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

        if (numTryForMove >=6) { // in case it hasn't been able to
            numTryForMove = moveToWrapper(11, -6, 5.55, 0, -0.7071068, 0, 0.7071068);
            Log.i(TAG, "moved to P1-2 again after numTry = " + numTryForMove);
        }
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

        if (numTryForMove >=6) { // in case it hasn't been able to
            numTryForMove = moveToWrapper(10.58,-6.45,5.37,0,0,-0.5,0.866);
            Log.i(TAG, "moved to Q1 again after numTry = " + numTryForMove);
        }
        // move Astrobee to P2-2
        numTryForMove = moveToWrapper(11.5,-8,5,0, 0, 0, 1);
        Log.i(TAG, "moved to P2-2 after numTry = " + numTryForMove);

        if (numTryForMove >=6) { // in case it hasn't been able to
            numTryForMove = moveToWrapper(11.5,-8,5,0, 0, 0, 1);
            Log.i(TAG, "moved to P2-2 again after numTry = " + numTryForMove);
        }
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

        if (numTryForMove >=6) { // in case it hasn't been able to
            numTryForMove = moveToWrapper(11,-7.7,5.55,0,-0.7071068,0,0.7071068);
            Log.i(TAG, "moved to P2-3 again after numTry = " + numTryForMove);
        }
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

        if (numTryForMove >=6) { // in case it hasn't been able to
            numTryForMove = moveToWrapper(10.30,-7.5,4.7,0,0,1,0);
            Log.i(TAG, "moved to P2-1 again after numTry = " + numTryForMove);
        }
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

        if (numTryForMove >=6) { // in case it hasn't been able to
            numTryForMove = moveToWrapper(10.95,-9.1, 4.9,0,0,-0.7071068,0.7071068);
            Log.i(TAG, "moved to Q4 again after numTry = " + numTryForMove);
        }
        // Attempt to move to P3 assuming it's in bay 7
        numTryForMove = moveToWrapper(valueXdouble, valueYdouble, valueZdouble, quaXdouble, quaYdouble, quaZdouble, quaWdouble);
        Log.i(TAG, "moved to P3 after numTry = " + numTryForMove);

        if (numTryForMove >=6) { // in case it hasn't been able to
            numTryForMove = moveToWrapper(valueXdouble, valueYdouble, valueZdouble, quaXdouble, quaYdouble, quaZdouble, quaWdouble);
            Log.i(TAG, "moved to P3 again after numTry = " + numTryForMove);
        }
        // Attempt to get AR at P3
        Log.i(TAG,"Getting AR ID at P3");
        String arId = getAR();
        if (!arId.equals("")) { // if we can get from this point, just do it first
            api.judgeSendDiscoveredAR(arId);
            Log.i(TAG,"AR ID = " + arId);
        }

//        numTryForMove = moveCloserWrapper(valueXdouble, valueYdouble, valueZdouble, quaXdouble, quaYdouble, quaZdouble, quaWdouble);
//        Log.i(TAG, "moved closer to AR tag after numTry = " + numTryForMove);

        if (arId.equals("")) { // if we couldn't get AR ID from P3
            // move to face the AR tag

            numTryForMove = moveFaceIDWrapper(valueXdouble, valueYdouble, valueZdouble, quaXdouble, quaYdouble, quaZdouble, quaWdouble);
            Log.i(TAG, "moved to face AR tag after numTry = " + numTryForMove);

            if (numTryForMove >= 6) { // in case it hasn't been able to
                numTryForMove = moveFaceIDWrapper(valueXdouble, valueYdouble, valueZdouble, quaXdouble, quaYdouble, quaZdouble, quaWdouble);
                Log.i(TAG, "moved to face AR tag again after numTry = " + numTryForMove);
            }
            Log.i(TAG, "Getting AR ID at point facing AR tag");
            arId = getAR();

            api.judgeSendDiscoveredAR(arId);
            Log.i(TAG, "AR ID = " + arId);
        }
//        double[] targetCoordinates = getTargetCoordinates(valueXdouble, valueYdouble, valueZdouble, quaXdouble, quaYdouble, quaZdouble, quaWdouble);
//        Log.i(TAG, "Target coordinates = (" + targetCoordinates[0] + "," + targetCoordinates[1]+ "," + targetCoordinates[2]+ ")");
//        if ((10.25 <= targetCoordinates[0]) && (targetCoordinates[0]<= 11.65) && (4.2 <= targetCoordinates[2]) && (targetCoordinates[2] <= 5.6)) {
//            // move from facing AR tag to face target point
//            numTryForMove = moveFaceTargetWrapper(targetCoordinates[0], -9.60, targetCoordinates[2]);
//            Log.i(TAG, "moved to face target point perpendicularly after numTry = " + numTryForMove);
//
//            if (numTryForMove >=6) { // in case it hasn't been able to
//                numTryForMove = moveFaceTargetWrapper(targetCoordinates[0], -9.60, targetCoordinates[2]);
//                Log.i(TAG, "moved to face target point again perpendicularly after numTry = " + numTryForMove);
//            }
//        } else {
//            Log.i(TAG, "Getting current Kinematics");
//            Kinematics kine = api.getTrustedRobotKinematics(5); // could potentially takes time
//            double currentX = kine.getPosition().getX();
//            double currentY = kine.getPosition().getY();
//            double currentZ = kine.getPosition().getZ();
//            Log.i(TAG, "Current coordinates = (" + currentX + "," + currentY + "," + currentZ + ")");
//            double[] orientationToFaceTarget = getRotationFaceTarget(currentX, currentY, currentZ, targetCoordinates[0], targetCoordinates[1], targetCoordinates[2]);
//            numTryForMove = moveToWrapper(currentX, currentY, currentZ, orientationToFaceTarget[0], orientationToFaceTarget[1], orientationToFaceTarget[2], orientationToFaceTarget[3]);
//            Log.i(TAG, "rotated at current point to face target after numTry = " + numTryForMove);
//
//            if (numTryForMove >= 6) {
//                numTryForMove = moveToWrapper(currentX, currentY, currentZ, orientationToFaceTarget[0], orientationToFaceTarget[1], orientationToFaceTarget[2], orientationToFaceTarget[3]);
//                Log.i(TAG, "rotated at current point to face target again after numTry = " + numTryForMove);
//
//                if (numTryForMove >=6) { // in case it hasn't been able to move here, might as well agaration
//                    numTryForMove = moveFaceTargetWrapper(targetCoordinates[0], -9.60, targetCoordinates[2]);
//                    Log.i(TAG, "moved to face target point again again perpendicularly after numTry = " + numTryForMove);
//                }
//            }
//        }

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
        // api.moveTO will be used for a minimum of 4 times and maximum 7 times
        // 3 will be returned if the robot succeeds within 4 tries
        // 6 will be returned if the robot doesn't succeed or if it succeeds on 7th try

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
            Bitmap bMap = crop(snapshot);
            Log.i("getQR", "snapshot acquired");
            value = readQRImage(bMap);
            if (value != null) {
                Log.i("getQR", "number of QR tries = " + loopCounter);
                return value;
            }
//            if ((loopCounter) % 5 == 0) {
//                rotateRelativeWrapper('x'); // rotate by 90 degrees about x
//                Log.i("getQR", "rotated 90 degrees about x");
//            }
        }
        if (value == null) {
            for (loopCounter = 0; loopCounter < LOOP_MAX; loopCounter++) {
                Bitmap snapshot = api.getBitmapNavCam();
                Bitmap bMap = crop1(snapshot); // use crop setting 1
                Log.i("getQR", "snapshot acquired");
                value = readQRImage(bMap);
                if (value != null) {
                    Log.i("getQR", "number of QR tries = " + loopCounter);
                    return value;
                }
//            if ((loopCounter) % 5 == 0) {
//                rotateRelativeWrapper('x'); // rotate by 90 degrees about x
//                Log.i("getQR", "rotated 90 degrees about x");
//            }
            }
        }
        Log.i("getQR", "number of QR tries = " + loopCounter);
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
    private Bitmap crop1(Bitmap source) {
        // use to crop the big bitmap returned from NavCam
        Bitmap croppedBitmap = Bitmap.createBitmap(source, 425, 190, 430, 576); // setting 1
//        Bitmap croppedBitmap = Bitmap.createBitmap(source, 510, 320, 340, 448); // setting 2
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
        ArrayList<Mat> corners = new ArrayList<Mat>();
        Mat markerIds = new Mat();

        ARaruco.detectMarkers(source,dictionary, corners,markerIds);

        if (corners.size() > 0) {
            int id = (int) (markerIds.get(0, 0)[0]);
            return String.valueOf(id);
        }
        else {
            return "";
        }

    }
    private String getAR() {
        // take a snapshot, get ARid

        //// with crop
//        Mat snapshotMat = api.getMatNavCam();
//        Mat source = cropMat(snapshotMat);

        //// without crop
//        Mat source = api.getMatNavCam(); // without crop
//        Log.i("getAR", "snapshot acquired");
//        String value = ARdetect(source);
//        int loopCounter = 0;
//        final int LOOP_MAX = 3;
//        while (loopCounter<= LOOP_MAX && value.equals("")) {
//            source = api.getMatNavCam();
//            Log.i("getAR", "snapshot acquired");
//            value = ARdetect(source);
//            Log.i("getAR","value when loopCounter = " + loopCounter + " is " + value);
//            loopCounter++;
//        }
//        Log.i("getAR","get AR after numTry = " + loopCounter);
//        return value;

        String value = "";
        int loopCounter = 0;
        final int LOOP_MAX = 3; // 200 is actually too long
        for (loopCounter = 0; loopCounter < LOOP_MAX; loopCounter++) {
            Mat source = api.getMatNavCam(); // without crop
            Log.i("getAR", "snapshot acquired");
            value = ARdetect(source);
            Log.i("getAR","value when loopCounter = " + loopCounter + " is " + value);
            if (!value.equals("")) {
                Log.i("getAR","get AR after numTry = " + loopCounter);
                return value;
            }
//            if ((loopCounter) % 5 == 0) {
//                rotateRelativeWrapper('x'); // rotate by 90 degrees about x
//                Log.i("getQR", "rotated 90 degrees about x");
//            }
        }
        Log.i("getAR","value when loopCounter = " + loopCounter + " is " + value);
        Log.i("getAR", "number of AR tries = " + loopCounter);
        return value;
    }

    private int moveCloserWrapper(double pos_x, double pos_y, double pos_z,
                              double qua_x, double qua_y, double qua_z,
                              double qua_w){
        // move the robot closer in the given orientation that it's facing
        // api.moveTO will be used for a minimum of 4 times and maximum 7 times
        // 3 will be returned if the robot succeeds within 4 tries
        // 6 will be returned if the robot doesn't succeed after 7 tries or if it succeeds on try 7

        double direction_x = qua_x*qua_x + qua_w*qua_w - qua_z*qua_z - qua_y*qua_y;
        double direction_y = 2*qua_x*qua_y + 2*qua_w*qua_z;
        double direction_z = 2*qua_x*qua_z - 2*qua_w*qua_y;

        double x_times;
        double y_times;
        double z_times;
        double multiple; // the multiple of the direction vector, calculated as the min of x_times, y_times and z_times
        if (direction_x > 0.001) {
            x_times = (11.60 - pos_x) / direction_x; // 11.65 is the x_max of KIZ
        } else if (direction_x < -0.001)
        {
            x_times = (10.30 - pos_x) / direction_x; // 10.25 is the x_min of KIZ
        } else {
            x_times = 1000; // choose a very big value
        }

        if (direction_y > 0.001) {
            y_times = (-2.95 - pos_y) / direction_y; // -3 is the y_max of KIZ
        } else if (direction_y < -0.001){
            y_times = ((-9.70) - pos_y) / direction_y; // -9.75 is the y_min of KIZ
        } else {
            y_times = 1000; // choose a very big value
        }

        if (direction_z > 0.001) {
            z_times = (5.55 - pos_z) / direction_z; // 5.6 is the z_max of KIZ
        } else if (direction_z < -0.001){
            z_times = (4.25 - pos_z) / direction_z; // 4.2 is the z_min of KIZ
        } else {
            z_times = 1000; // choose a very big value
        }

        // multiple is the minimum of the three to  ensure the robot will be still in KIZ
        multiple = Math.min((Math.min(x_times, y_times)),z_times);

        double new_pos_x = pos_x + multiple * direction_x;
        double new_pos_y = pos_y + multiple * direction_y;
        double new_pos_z = pos_z + multiple * direction_z;

        final int LOOP_MAX = 3; // actually this is the minimum
        final int LOOP_LIMIT = 6;
        Point point = new Point(new_pos_x, new_pos_y, new_pos_z);
        Quaternion quaternion = new Quaternion((float)qua_x, (float)qua_y,
                (float)qua_z, (float)qua_w);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while((!result.hasSucceeded()||(loopCounter < LOOP_MAX)) && loopCounter < LOOP_LIMIT){
//          Log.i("MoveTo", "Try again for current point");
            result = api.moveTo(point, quaternion, true);
            ++loopCounter;
        }
        Log.i("MyActivity", "Move closer in direction (" + direction_x + "," + direction_y + "," +
                direction_z+ ") with multiple = " + multiple);
        return loopCounter;
    }
    private double[] getARcoordinates(double pos_x, double pos_y, double pos_z,
                                      double qua_x, double qua_y, double qua_z,
                                      double qua_w) {
        // get the approximate coordinates of AR from the coordinates and orientation of P3
        // assume that the y coordinates of AR is -10.10
        double direction_x = qua_x*qua_x + qua_w*qua_w - qua_z*qua_z - qua_y*qua_y;
        double direction_y = 2*qua_x*qua_y + 2*qua_w*qua_z; // direction y is always negative
        double direction_z = 2*qua_x*qua_z - 2*qua_w*qua_y;


        // the multiple of the direction vector to get from current position to AR tag location
        double multiple = ((-10.10) - pos_y) / direction_y; // -10.10 is assumed to the y value of AR plane

        double AR_x = pos_x + multiple * direction_x;
        // y-position of AR would be pos_y + multiple * direction_y
        double AR_y = pos_y + multiple * direction_y;
        double AR_z = pos_z + multiple * direction_z;

        double[] AR_coordinates = {AR_x, AR_y, AR_z};
        return AR_coordinates;
    }

    private int moveFaceIDWrapper(double pos_x, double pos_y, double pos_z,
                                  double qua_x, double qua_y, double qua_z,
                                  double qua_w){
        // move the robot to face the AR ID. if AR lies out of bound, just move near there
        // api.moveTO will be used for a minimum of 4 times and maximum 7 times
        // 3 will be returned if the robot succeeds within 4 tries
        // 6 will be returned if the robot doesn't succeed after 7 tries or if it succeeds on try 7

        double[] AR_coordinates = getARcoordinates(pos_x, pos_y, pos_z,
                                                    qua_x, qua_y, qua_z,
                                                    qua_w);
        double new_pos_x = AR_coordinates[0];
        if (new_pos_x < 10.25) { // if AR tag lies out of bound to the left
            new_pos_x = 10.15;
        }
        // y-position of AR would be pos_y + multiple * direction_y
        double new_pos_y = -9.65; // -9.75 is the y_min of KIZ

        double new_pos_z = AR_coordinates[2];
        if (new_pos_z < 4.2) { // if AR tag lies out of bound to above
            new_pos_z = 4.1;
        }


        final int LOOP_MAX = 3; // actually this is the minimum
        final int LOOP_LIMIT = 6;
        Point point = new Point(new_pos_x, new_pos_y, new_pos_z);
        Quaternion quaternion = new Quaternion((float)(0), (float)0,
                (float)(-0.7071068), (float)0.7071068);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while((!result.hasSucceeded()||(loopCounter < LOOP_MAX)) && loopCounter < LOOP_LIMIT){
//          Log.i("MoveTo", "Try again for current point");
            result = api.moveTo(point, quaternion, true);
            ++loopCounter;
        }
//        Log.i("MyActivity", "Move to face AR ID in direction (" + direction_x + "," + direction_y + "," +
//                direction_z+ ") with multiple = " + multiple);
        Log.i("MyActivity", "New location facing AR = (" + new_pos_x + "," + new_pos_y + "," + new_pos_z + ")");

//        double[] values = new double[4];
//        values[0] = loopCounter;
//        values[1] = new_pos_x;
//        values[2] = new_pos_y;
//        values[3] = new_pos_z;
        return loopCounter;
    }
    private double[] getTargetCoordinates(double pos_x, double pos_y, double pos_z,
                                        double qua_x, double qua_y, double qua_z,
                                        double qua_w) {
        double[] AR_coordinates = getARcoordinates(pos_x, pos_y, pos_z,
                                    qua_x, qua_y, qua_z, qua_w);
        double new_pos_x = AR_coordinates[0] + 0.2/(Math.sqrt(2)) - 0.0572; // 0.0572 is laser offset, 0.2 is hypotenuse distance from AR tag to target point
        double new_pos_y = AR_coordinates[1];
        double new_pos_z = AR_coordinates[2] + 0.2/(Math.sqrt(2)) + 0.1111; // 0.1111 is laser offset, 0.2 is hypotenuse distance from AR tag to target point
        double[] targetCoordinates = {new_pos_x, new_pos_y, new_pos_z};
        return targetCoordinates;
    }
    private int moveFaceTargetWrapper(double new_pos_x, double new_pos_y, double new_pos_z){
        // move the robot from facing the AR tag to a location such that the laser will face the target point
        // api.moveTO will be used for a minimum of 4 times and maximum 7 times
        // 3 will be returned if the robot succeeds within 4 tries
        // 6 will be returned if the robot doesn't succeed after 7 tries or if it succeeds on try 7

//        double new_pos_y = -9.60; // -9.75 is the y_min of KIZ
//        double new_pos_z = pos_z + 0.2/(Math.sqrt(2)) + 0.1111; // 0.1111 is laser offset, 0.2 is hypotenuse distance from AR tag to target point
//
        if (new_pos_z >= 5.59) {
            new_pos_z = 5.55;
        } else if (new_pos_z <= 4.21 ) {
            new_pos_z = 4.25;
        }

        if (new_pos_x >= 11.64) {
            new_pos_x = 11.60;
        } else if (new_pos_x <= 10.26){
            new_pos_x = 10.30;
        }

        final int LOOP_MAX = 3; // actually this is the minimum
        final int LOOP_LIMIT = 6;
        Point point = new Point(new_pos_x, new_pos_y, new_pos_z);
        Quaternion quaternion = new Quaternion((float)(0), (float)0,
                (float)(-0.7071068), (float)0.7071068);

        Result result = api.moveTo(point, quaternion, true);

        int loopCounter = 0;
        while((!result.hasSucceeded()||(loopCounter < LOOP_MAX)) && loopCounter < LOOP_LIMIT){
//          Log.i("MoveTo", "Try again for current point");
            result = api.moveTo(point, quaternion, true);
            ++loopCounter;
        }
        Log.i("MyActivity", "New location facing target = (" + new_pos_x + "," + new_pos_y + "," + new_pos_z + ")");
        return loopCounter;
    }
    private double[] multiplyQuaternions (double[] quaternion1, double[] quaternion2) {
        // (x,y,z,w) are the elements of the arrays, in that order
        double[] result = new double[4];
        result[0] = quaternion1[0] * quaternion2[3] + quaternion1[3] * quaternion2[0] - quaternion1[2] * quaternion2[1]
                + quaternion1[1] * quaternion2[2];
        result[1] = quaternion1[1] * quaternion2[3] + quaternion1[2] * quaternion2[0] + quaternion1[3] * quaternion2[1]
                - quaternion1[0] * quaternion2[2];
        result[2] = quaternion1[2] * quaternion2[3] - quaternion1[1] * quaternion2[0] + quaternion1[0] * quaternion2[1]
                + quaternion1[3] * quaternion2[2];
        result[3] = - quaternion1[0] * quaternion2[0] - quaternion1[1] * quaternion2[1] - quaternion1[2] * quaternion2[2]
                + quaternion1[3] * quaternion2[3];
        return result;
    }
    private double[] normalise(double[] vector) {
        // scale the coordinate vector (x,y,z) to form a unit vector
        double norm = Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        double[] result = {vector[0]/norm, vector[1]/norm, vector[2]/norm};
        return result;
    }
    private double[] crossProduct(double[] firstVector, double[] secondVector) {
        // calculate the cross product of two vector
        double newX = firstVector[1] * secondVector[2] - firstVector[2] * secondVector[1];
        double newY = firstVector[2] * secondVector[0] - firstVector[0] * secondVector[2];
        double newZ = firstVector[0] * secondVector[1] - firstVector[1] * secondVector[0];

        double[] result = {newX, newY, newZ};
        return result;
    }
    private double[] getRotationFaceTarget(double currentX, double currentY, double currentZ, double targetX, double targetY, double targetZ) {
        // takes in current coordinates and target coordinates and calculate the rotation quaternion
        double[] faceYorientation = {0, 0, -0.7071068, 0.7071068};
        double[] firstRotation = new double[4];
        double[] secondRotation = new double[4];

        //// first rotation after rotating to face -y direction
        double theta = Math.atan((currentZ-targetZ)/(currentY-targetY));
        firstRotation[0] = Math.sin(theta/2);
        firstRotation[1] = 0;
        firstRotation[2] = 0;
        firstRotation[3] = Math.cos(theta/2);

//        // for debugging
//        System.out.println("theta = " + theta);
//        System.out.println("firstRotation = " + Arrays.toString(firstRotation));
//        System.out.println("After first rotation: " + Arrays.toString(multiplyQuaternions(firstRotation,faceYorientation)));

        //// second rotation
        double adjacentLength = (currentY-targetY) / Math.cos(theta); // the adjacent for theta2 is the hypotenuse for theta1
        double secondTheta = Math.abs(Math.atan((targetX-currentX)/adjacentLength));
        // vector from current point to the point (currentX, targetY, targetZ)
        double[] vector1 = {currentX - currentX, targetY - currentY, targetZ - currentZ};
        // vector from current point to the point (targetX, targetY, targetZ)
        double[] vector2 = {targetX - currentX, targetY - currentY, targetZ - currentZ};
//        // for debugging
//        System.out.println("vector2 = " + Arrays.toString(vector2));
        // rotation axis
        double[] rotation_axis = normalise(crossProduct(vector1, vector2));
        // second rotation quaternion
        secondRotation[0] = rotation_axis[0] * Math.sin(secondTheta/2);
        secondRotation[1] = rotation_axis[1] * Math.sin(secondTheta/2);
        secondRotation[2] = rotation_axis[2] * Math.sin(secondTheta/2);
        secondRotation[3] = Math.cos(secondTheta/2);

        // combined all the rotations into 1
        double[] result = multiplyQuaternions(secondRotation, multiplyQuaternions(firstRotation, faceYorientation));

//        // for debugging
//        System.out.println("second theta = " + secondTheta);
//        System.out.println("rotation_axis = " + Arrays.toString(rotation_axis));
//        System.out.println("secondRotation = " + Arrays.toString(secondRotation));
//        double[] resultInverse = {-result[0], -result[1], -result[2], result[3]};
//        double[] originalOrientation = {1,0,0,0}; // originally the camera faces the x direction
//        double[] testGetDirectionVector = multiplyQuaternions(result, multiplyQuaternions(originalOrientation, resultInverse));
//        System.out.println("get direction vector " + Arrays.toString(testGetDirectionVector));
//        double[] correctDirectionVector = {targetX-currentX, targetY-currentY, targetZ-currentZ};
//        System.out.println("correct direction vector " + Arrays.toString(normalise(correctDirectionVector)));

        return result;
    }


}

