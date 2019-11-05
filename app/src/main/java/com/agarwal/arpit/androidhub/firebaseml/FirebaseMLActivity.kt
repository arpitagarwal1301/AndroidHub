package com.agarwal.arpit.androidhub.firebaseml

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Rational
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.agarwal.arpit.androidhub.AppController
import com.agarwal.arpit.androidhub.R
import com.agarwal.arpit.common.logging.HubLog
import com.agarwal.arpit.common.utils.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import kotlinx.android.synthetic.main.activity_firebase_ml.*
import java.io.File
import java.io.IOException

class FirebaseMLActivity : AppCompatActivity() {

    private val REQUEST_CAMERA_PERMISSION = 2


    //Preview
    private lateinit var mPreviewConfig: PreviewConfig
    private lateinit var mPreview: Preview
    private var isFrontCamera = true

    //Face detection , ImageCapture
    private lateinit var mImageCaptureConfig: ImageCaptureConfig
    private lateinit var mImgCap: ImageCapture
    private var mCaptureNewImage: Boolean = true


    //Object,Text Detection , ImageAnalysis
    private lateinit var imageAnalysisConfig: ImageAnalysisConfig
    private lateinit var mImageAnalysis: ImageAnalysis
    private lateinit var options: FirebaseVisionObjectDetectorOptions
    private lateinit var objectDetector: FirebaseVisionObjectDetector
    private lateinit var detector: FirebaseVisionTextRecognizer
    private lateinit var visionImage: FirebaseVisionImage
    private lateinit var mRect: Rect


    private var mCameraFilePath: File? = null
    private var correctImageCount = 0
    private var takeImageInput: Boolean = false
    private val MIN_CORRECT_IMG_COUNT = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_ml)

        if (isPermissionGranted()) {
            initCaptureCamera()
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CAMERA_PERMISSION
            )
        }


    }

    private fun initCaptureCamera() {

        val lensFacing = if (isFrontCamera) CameraX.LensFacing.FRONT else CameraX.LensFacing.BACK
        val targetAspectRatio: Rational = Rational(getScreenWidth(), getScreenHeight() - getStatusBarHeight());
        mPreviewConfig = PreviewConfig
                .Builder()
                .setLensFacing(lensFacing)
                .setTargetAspectRatio(targetAspectRatio)
                .build();

        mPreview = Preview(mPreviewConfig);

        mImageCaptureConfig = ImageCaptureConfig.Builder()
                .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(Surface.ROTATION_0)
                .setLensFacing(lensFacing)
                .setTargetAspectRatio(targetAspectRatio)
                .build();

        mImgCap = ImageCapture(mImageCaptureConfig);

        mPreview.setOnPreviewOutputUpdateListener(

                 //to update the surface texture we  have to destroy it first then re-add it
                Preview.OnPreviewOutputUpdateListener {

                    var parent: ViewGroup = texture_view.parent as ViewGroup
                    parent.removeView(texture_view)
                    parent.addView(texture_view, 0)
                    texture_view.setSurfaceTexture(it.getSurfaceTexture());
                }
        )

        CameraX.unbindAll();
        //bind to lifecycle:
        CameraX.bindToLifecycle(this, mPreview, mImgCap);


        mImgCap.takePicture(mCameraFilePath, object : ImageCapture.OnImageSavedListener {
            override fun onImageSaved(file: File) {
                CameraX.unbindAll()
                try {
                    mCaptureNewImage = false
                    var source = handleSamplingAndRotationBitmap(AppController.getContext(), Uri.fromFile(file))
                    source = scaleBitmap(source!!)

                    val image = FirebaseVisionImage.fromBitmap(source)
                    faceDetection(image)

                } catch (e: IOException) {
                    mCaptureNewImage = true
                }

            }

            override fun onError(imageCaptureError: ImageCapture.ImageCaptureError, message: String, cause: Throwable?) {
                if (cause != null) {
                    mCaptureNewImage = true
                }
            }
        })
    }

    private fun faceDetection(image: FirebaseVisionImage) {
        HubLog.d("faceDetection")
        val mDetector = FirebaseVision.getInstance().visionFaceDetector
        mDetector.detectInImage(image)
                .addOnSuccessListener { faces ->
                    // Task completed successfully
                    HubLog.d("faceDetection: onSuccess")
                    if (faces.size > 0) {
                        showToast("Face detected")
                    } else {
                        mCaptureNewImage = true
                        showToast("Face not detected")
                    }
                }
                .addOnFailureListener(
                        object : OnFailureListener {
                            override fun onFailure(e: Exception) {
                                HubLog.d("faceDetection: onFailure")
                                mCaptureNewImage = true
//                                initCamera()
                            }
                        })
    }


    private fun initDetectionCamera() {

        val lensFacing = CameraX.LensFacing.FRONT

        mPreviewConfig = PreviewConfig.Builder()
                .setLensFacing(lensFacing)
                .build()

        options = FirebaseVisionObjectDetectorOptions.Builder()
                .setDetectorMode(FirebaseVisionObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableClassification()
                .build()
        objectDetector = FirebaseVision.getInstance().getOnDeviceObjectDetector(options)
        detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        mCameraFilePath = getTempOutputMediaPathForAllFiles()

        imageAnalysisConfig = ImageAnalysisConfig.Builder()
                .setLensFacing(lensFacing)
                .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
                .build()

        mPreview = Preview(mPreviewConfig)

        mPreview.onPreviewOutputUpdateListener = Preview.OnPreviewOutputUpdateListener { output ->
            //to update the surface texture we  have to destroy it first then re-add it
            val parent = texture_view.getParent() as ViewGroup
            parent.removeView(texture_view)
            parent.addView(texture_view, 0)
            texture_view.setVisibility(View.VISIBLE)

            texture_view.setSurfaceTexture(output.surfaceTexture)
        }


        mImageAnalysis = ImageAnalysis(imageAnalysisConfig)
        mImageAnalysis.analyzer = object : ImageAnalysis.Analyzer {

            private val FRAME_DURATION_IN_MILLIS = 10
            private var lastAnalyzedTimestamp = 0L

            override fun analyze(imageProxy: ImageProxy?, rotationDegrees: Int) {

                if (correctImageCount <= MIN_CORRECT_IMG_COUNT && takeImageInput) {


                    val currentTimestamp = System.currentTimeMillis()

                    if (currentTimestamp - lastAnalyzedTimestamp >= FRAME_DURATION_IN_MILLIS) {

                        takeImageInput = false

                        if (imageProxy == null || imageProxy.image == null) {
                            takeImageInput = true
                            return
                        }

                        val mediaImage = imageProxy.image
                        val rotation = degreesToFirebaseRotation(rotationDegrees)
                        visionImage = FirebaseVisionImage.fromMediaImage(mediaImage!!, rotation)

                        documentDetection()

                        lastAnalyzedTimestamp = currentTimestamp
                    }
                }
            }
        }

        CameraX.unbindAll()

        CameraX.bindToLifecycle(this, mPreview, mImageAnalysis)

    }

    private fun documentDetection() {

        objectDetector.processImage(visionImage)
                .addOnSuccessListener { detectedObjects ->
                    // Task completed successfully
                    // ...
                    if (detectedObjects.size > 0) {

                        mRect = detectedObjects[0].boundingBox
                        val bitmap = scaleBitmap(visionImage.bitmap)
                        visionImage = FirebaseVisionImage.fromBitmap(bitmap)
                        textDetection()

                    } else {
                        correctImageCount = 0
                        takeImageInput = true
                    }
                }
                .addOnFailureListener(
                        object : OnFailureListener {
                            override fun onFailure(e: Exception) {
                                correctImageCount = 0
                                takeImageInput = true
                            }
                        })

    }

    private fun textDetection() {

        detector.processImage(visionImage)
                .addOnSuccessListener { firebaseVisionText ->
                    // Task completed successfully
                    // ...
                    if (!TextUtils.isEmpty(firebaseVisionText.text)) {
                        val msg = "Text detected $correctImageCount"

                        if (mRect != null && correctImageCount >= MIN_CORRECT_IMG_COUNT) {
                            val newBitmap = Bitmap.createBitmap(visionImage.bitmap, mRect.left, mRect.top, mRect.width(), mRect.height())
                            saveBitMapImage(mCameraFilePath!!, newBitmap)
                        } else {
                            correctImageCount = correctImageCount + 1
                            takeImageInput = true
                        }

                    } else {
                        correctImageCount = 0
                        takeImageInput = true
                    }
                }
                .addOnFailureListener {
                    correctImageCount = 0
                    takeImageInput = true
                }


    }


    fun degreesToFirebaseRotation(degrees: Int): Int {
        when (degrees) {
            0 -> return FirebaseVisionImageMetadata.ROTATION_0
            90 -> return FirebaseVisionImageMetadata.ROTATION_90
            180 -> return FirebaseVisionImageMetadata.ROTATION_180
            270 -> return FirebaseVisionImageMetadata.ROTATION_270
            else -> throw IllegalArgumentException(
                    "Rotation must be 0, 90, 180, or 270.")
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {


        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initCaptureCamera()
            }
        }
        TODO("In case : Never selected show alert dialog and open setting page'")

    }

}