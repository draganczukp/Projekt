package tk.draganczuk.projekt.multimedia;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import tk.draganczuk.projekt.R;

public class TakePhotoActivity extends AppCompatActivity {

	private static final String TAG = "AndroidCameraApi";
	private TextureView textureView;

	protected CameraDevice cameraDevice;
	protected CameraCaptureSession cameraCaptureSessions;
	protected CaptureRequest.Builder captureRequestBuilder;
	private Size imageDimension;
	private ImageReader imageReader;
	private static final int REQUEST_CAMERA_PERMISSION = 200;
	private Handler mBackgroundHandler;
	private HandlerThread mBackgroundThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.activity_take_photo);
		textureView = findViewById(R.id.texture);
		assert textureView != null;
		textureView.setSurfaceTextureListener(textureListener);

		long totalTime = TimeUnit.SECONDS.toMillis(5);

		TextView timerText = findViewById(R.id.timerText);

		new CountDownTimer(totalTime, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				timerText.setText(Long.toString(TimeUnit.SECONDS.convert(millisUntilFinished, TimeUnit.MILLISECONDS)));
			}

			@Override
			public void onFinish() {
				timerText.setText(Long.toString(TimeUnit.SECONDS.convert(totalTime, TimeUnit.MILLISECONDS)));
				takePicture();
				Toast.makeText(TakePhotoActivity.this, "Photo taken", Toast.LENGTH_SHORT).show();
			}
		}.start();

	}

	TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
			openCamera();
		}

		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
		}

		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
			return false;
		}

		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		}
	};
	private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
		@Override
		public void onOpened(@NotNull CameraDevice camera) {
			cameraDevice = camera;
			createCameraPreview();
		}

		@Override
		public void onDisconnected(@NotNull CameraDevice camera) {
			cameraDevice.close();
		}

		@Override
		public void onError(@NotNull CameraDevice camera, int error) {
			cameraDevice.close();
			cameraDevice = null;
		}
	};

	protected void startBackgroundThread() {
		mBackgroundThread = new HandlerThread("Camera Background");
		mBackgroundThread.start();
		mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
	}

	protected void stopBackgroundThread() {
		mBackgroundThread.quitSafely();
		try {
			mBackgroundThread.join();
			mBackgroundThread = null;
			mBackgroundHandler = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void takePicture() {

		Objects.requireNonNull(cameraDevice);

		try {
			List<Surface> outputSurfaces = new ArrayList<>(2);

			outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

			final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
			captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

			final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
				@Override
				public void onCaptureCompleted(@NotNull CameraCaptureSession session,
				                               @NotNull CaptureRequest request,
				                               @NotNull TotalCaptureResult result) {
					super.onCaptureCompleted(session, request, result);
					createCameraPreview();
				}
			};

			cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
				@Override
				public void onConfigured(@NotNull CameraCaptureSession session) {
					try {
						session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
					} catch (CameraAccessException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onConfigureFailed(@NotNull CameraCaptureSession session) {
				}
			}, mBackgroundHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	protected void createCameraPreview() {
		try {
			SurfaceTexture texture = textureView.getSurfaceTexture();

			texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
			Surface surface = new Surface(texture);

			captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			captureRequestBuilder.addTarget(surface);
			cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
				@Override
				public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
					if (cameraDevice == null) {
						return;
					}
					cameraCaptureSessions = cameraCaptureSession;
					updatePreview();
				}

				@Override
				public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
					Toast.makeText(TakePhotoActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
				}
			}, null);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	private void openCamera() {
		CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

		try {
			String cameraId = Objects.requireNonNull(manager).getCameraIdList()[0];

			CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
			StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

			imageDimension = Objects.requireNonNull(map).getOutputSizes(SurfaceTexture.class)[0];

			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
				return;
			}
			manager.openCamera(cameraId, stateCallback, null);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	protected void updatePreview() {

		captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

		try {
			cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	private void closeCamera() {
		if (cameraDevice != null) {
			cameraDevice.close();
			cameraDevice = null;
		}
		if (imageReader != null) {
			imageReader.close();
			imageReader = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		startBackgroundThread();
		if (textureView.isAvailable()) {
			openCamera();
		} else {
			textureView.setSurfaceTextureListener(textureListener);
		}
	}

	@Override
	protected void onPause() {
		Log.e(TAG, "onPause");
		closeCamera();
		stopBackgroundThread();
		super.onPause();
	}

}
