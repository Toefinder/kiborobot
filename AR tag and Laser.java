
// reading the AR tag and moving the probe to correctly position the laser


// attempt to turn on laser control
        api.laserControl(true);
        Log.i(TAG, "laser on");

//        moveToWrapper(11.5, -5.7, 4.5, 0, -0.7071068, 0, 0.7071068);

private String getAR() {
        String value = null;
        int loopCounter = 0;
        final int LOOP_MAX = 3; // 200 is actually too long
        for (loopCounter = 0; loopCounter < LOOP_MAX; loopCounter++) {
            Bitmap snapshot = api.getBitmapNavCam();
            Log.i("getAR", "snapshot acquired");
            value = readARImage(snapshot);
            if (value != null) {
                Log.i("getAR", "number of AR tries = " + loopCounter);
                return value;
            }
//            if ((loopCounter) % 5 == 0) {
//                rotateRelativeWrapper('x'); // rotate by 90 degrees about x
//                Log.i("getQR", "rotated 90 degrees about x");
//            }
        }
        Log.i("getAR", "number of AR tries = " + loopCounter);
        return "";
    }
    private String readARImage(Bitmap snapshot) {
        cv::Ptr<cv::aruco::Dictionary> dictionary = cv::aruco::getPredefinedDictionary(cv::aruco::DICT_5X5_250)
        //Arrays to store the ids and corners
        std::vector<int> ids; 
        std::vector<std::vector<cv::Point2f> > corners;
        cv::aruco::detectMarkers(Bitmap snapshot, dictionary, corners, ids); //function
        
        //Camera calibration parameters from the doc
        camera_matrix:
            rows: 3
            cols: 3
            data: [344.173397, 0.000000, 630.793795, 0.000000, 344.277922, 487.033834, 0.000000, 0.000000, 1.000000]
        distortion_model: plumb_bob
        distortion_coefficients:
            rows: 1
            cols: 5
            data: [-0.152963, 0.017530, -0.001107, -0.000210, 0.000000]

        cv::Mat camera_matrix, distortion_coefficients;
        std::vector<cv::Vec3d> rvecs, tvecs;
        cv::aruco::estimatePoseSingleMarkers(corners, 0.05, camera_matrix, distortion_coefficients, rvecs, tvecs);
  
        }
    }
