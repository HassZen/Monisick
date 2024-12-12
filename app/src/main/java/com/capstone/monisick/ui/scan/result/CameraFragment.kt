package com.capstone.monisick.ui.scan.result

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capstone.monisick.R
import com.capstone.monisick.data.database.entity.ImageResult
import com.capstone.monisick.data.utils.createFile
import com.capstone.monisick.databinding.FragmentCameraBinding
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.util.FileUtils
import java.io.File
import java.util.UUID

class CameraFragment : Fragment() {
    private var _bindings: FragmentCameraBinding? = null
    private val bindings get() = _bindings
    private var camerasSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imgCapture: ImageCapture? = null
    private lateinit var soundsPool: SoundPool
    private var soundsId = 0
    private var soundsPoolLoaded = false
    private val uCropContract = object : ActivityResultContract<Uri, Uri>() {
        override fun createIntent(context: Context, input: Uri): Intent {
            val outputUri = Uri.fromFile(File(context.cacheDir, "${UUID.randomUUID()}.jpg"))
            return UCrop.of(input, outputUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1080, 1080)
                .withOptions(UCrop.Options().apply {
                    setCompressionQuality(80)
                    setFreeStyleCropEnabled(true)
                })
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return if (resultCode == Activity.RESULT_OK && intent != null) {
                UCrop.getOutput(intent) ?: Uri.EMPTY
            } else {
                Uri.EMPTY
            }
        }
    }
    private val cropImageLauncher = registerForActivityResult(uCropContract) { uri ->
        if (uri.toString().isNotEmpty()) {
            movesToUpload(
                ImageResult(
                    imagePath = uri.path!!,
                    imageUri = uri,
                    isFromCamera = true
                )
            )
        } else {
            Toast.makeText(requireContext(), "Pemangkasan gambar gagal", Toast.LENGTH_SHORT).show()
        }
    }
    private val launchesGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val resolvedUri =
                FileUtils.getPath(requireContext(), it)?.let { path -> File(path).toUri() } ?: it
            cropImageLauncher.launch(resolvedUri)
        } ?: run {
            Toast.makeText(requireContext(), "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindings = FragmentCameraBinding.inflate(inflater, container, false)
        return bindings?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchesPermission.launch(PERMISSIONS.first())
        soundsPool = SoundPool.Builder()
            .setMaxStreams(1)
            .build()
        soundsPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) soundsPoolLoaded = true
        }
        soundsId = soundsPool.load(requireActivity(), R.raw.shutter_camera, 1)
        setsUpView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    private fun setsUpView() {
        bindings?.apply {
            btnCapture.setOnClickListener {
                if (soundsPoolLoaded) {
                    soundsPool.play(soundsId, 1f, 1f, 1, 0, 1f)
                }
                takesPhoto()
            }
            btnAddGallery.setOnClickListener {
                val mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                launchesGallery.launch(PickVisualMediaRequest(mediaType))
            }
            btnFlipCamera.setOnClickListener {
                camerasSelector =
                    if (camerasSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                    else CameraSelector.DEFAULT_BACK_CAMERA
                startsCamera()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        startsCamera()
    }

    private fun startsCamera() {
        val camerasProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        camerasProviderFuture.addListener({
            val camerasProvider = camerasProviderFuture.get()
            val previews = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(bindings?.viewFinder?.surfaceProvider)
                }
            imgCapture = ImageCapture.Builder().build()
            try {
                camerasProvider.unbindAll()
                camerasProvider.bindToLifecycle(
                    this,
                    camerasSelector,
                    previews,
                    imgCapture
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.open_camera_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun takesPhoto() {
        val imgCapture = imgCapture ?: return
        val photoFiles = createFile(requireActivity().application)
        val outputsOptions = ImageCapture.OutputFileOptions.Builder(photoFiles).build()
        imgCapture.takePicture(
            outputsOptions,
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    cropImageLauncher.launch(photoFiles.toUri()) // Launch UCrop
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.taking_photo_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun movesToUpload(imageResult: ImageResult) {
        val action = CameraFragmentDirections.actionCameraFragment2ToResultFragment(
            imageResult.copy(imagePath = imageResult.imagePath)
        )
        findNavController().navigate(action)
    }

    private val launchesPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted and !isAllPermissionsGranted()) {
            Toast.makeText(requireActivity(), getString(R.string.access_denied), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun isAllPermissionsGranted() = PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        soundsPool.release()
        _bindings = null
    }

    companion object {
        val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}