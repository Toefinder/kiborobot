# kiborobot
The code for the apk that would direct an Astrobee robot to complete the space tasks
+ Move to given locations of QR codes (there are 6 of these: P1-1, P1-2, P1-3, P2-1, P2-2, P2-3)
+ Avoid obstacles (keep out zone, or KOZ) and stay in keep in zone (KIZ)
+ Read the QR codes to extract location and orientation information of the point P3
+ Read AR tag at P3, locate target point and shine a laser at the target
## Structure of the repository: 
+ YourService.java: the main code
+ build.gradle: gradle file for Module App
+ log...: summarised log file from adb.log
+ cropRatio.txt: the crop ratio for cropping the image before passing it into QRCodeReader
## Overview of important methods in YourService.java
1) Main   
`runPlan1()`
This is the main function that will direct the astrobee
2) Move to a certain location,  with a certain orientation  
`private int moveToWrapper(double pos_x, double pos_y, double pos_z,
                               double qua_x, double qua_y, double qua_z,
                               double qua_w)`
`pos_x`, `pos_y`, `pos_z`: the location coordinates
`qua_x`, `qua_y`, `qua_z`, `qua_w`: the orientation quaternion
Return an integer, which is the number of tries the robot has done to move. This number can be used to check if the move is successful.
3) Functions to read QR codes  
  a) `private String readQRImage(Bitmap original)`   
  This function returns the information string from a Bitmap object, raising exceptions and returns **null** if fails to do so. 
 Inner workings utilise QRCodeReader from Zxing package.   
  b) `private Bitmap crop(Bitmap source)`  
  Crops a Bitmap image to a smaller piece for easier reading of QR code.    
  c) `private String getQR()`
 This function calls `api.getBitmapNavCam()` to get **Bitmap** camera image from NavCam, calls `crop()` to crop that image, and then calls `readQRImage()` until a correct string is obtained. If it fails after a maximum number of tries, an empty string is returned to the main function.
4) Functions to read AR codes  
  a) `private Mat cropMat(Mat source)`  
  Crops a Mat image to a smaller piece for easier reading of AR tag. 
 **Currently this is not used, because we are still not sure where the AR tag would lie in the frame**  
  b) `private String ARdetect(Mat source)` 
  Similar to `readQRImage()`, but uses Aruco from opencv.aruco instead of Zxing. `DICT_5X5_250` is used as the dictionary to read the AR tag. Unlike `readQRImage()`, this function return **empty string** if fails to read the AR tag.   
  c) `getAR()`  
  Quite similar to `getQR()`. This function calls `api.getMatNavCam()` to get **Mat** camera image from NavCam, then calls `ARdetect()` until a non-empty string is obtained. If it fails after a maximum number of tries, an empty string is returned to the main function.
5) Other move functions    
  a) `private int moveCloserWrapper(double pos_x, double pos_y, double pos_z,
                              double qua_x, double qua_y, double qua_z,
                              double qua_w)`  
  This function moves "forward" in the direction it's facing, until it nearly reaches the boundaries of the KIZ. Note that this function does not factor the KOZ, so it can only be used in certain situations. 
**Initially developed to move closer to the AR tag, but burrently this function is not used, since P3 is already close to the boundary of the KIZ**  
  b) `private double[] moveFaceIDWrapper(double pos_x, double pos_y, double pos_z,
                                  double qua_x, double qua_y, double qua_z,
                                  double qua_w)`  
  Arguments into this function are the location and orientation of P3, obtained from the QR codes. Note that `qua_w` is calculated in the main function, with the assumption that it's positive. **Potentially buggy, but can easily fix**
 This function moves to face the AR tag, with the first assumption that the y-coordinate of the plane containing the AR tag and target point is -10.20 (it's like assuming bay 7 is as long as bay 6). The second assumption is that the robot will point exactly at AR tag at P3 with the given orientation from the QR codes (it's like assuming that if we move the robot in the direction it's facing at P3, it will bump into the AR tag). The function first calculates the direction vector using the orientation quaternion. Then using the second assumption, it calculates the x and z coordinates of the AR tag.
 Returns a double with four elements. First element is number of tries to move to the new location. The latter three elements are the (x,y,z) coordinates of the new location that the robot moves to.   
  c) `private int moveFaceTargetWrapper(double pos_x, double pos_y, double pos_z)`  
  Arguments into this function are the location coordinates of the point facing the AR tag. These coordinates are obtained from `moveFaceIDWrapper`. 
 The function moves the robot to face the target. 
 Return the number of tries to move there.
 **Currently buggy, because the x and z coordinates of the target may be out of the KIZ. Currently the code handles this by checking the x and z coordinates, and if they are out of bound, it changs them to the values near the boundaries. This works, but the laser is not accurate.**
## How to contribute
1) Create a new branch and do a pull request. Please provide description for every git pull request that you do.
2) Do a test on the simulator, and get the adb.log file. Change the name of the log file to match the test number. Eg, test24 has adb14.log file. 
