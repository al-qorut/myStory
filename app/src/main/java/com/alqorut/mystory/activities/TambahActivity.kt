package com.alqorut.mystory.activities

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.alqorut.mystory.R
import com.alqorut.mystory.api.ApiConfig
import com.alqorut.mystory.api.response.UploadResponse
import com.alqorut.mystory.api.response.errorResponse
import com.alqorut.mystory.data.RepoStory
import com.alqorut.mystory.databinding.ActivityMainBinding
import com.alqorut.mystory.databinding.ActivityTambahStoryBinding
import com.alqorut.mystory.helpers.Config
import com.alqorut.mystory.helpers.isProgress
import com.alqorut.mystory.helpers.msg
import com.alqorut.mystory.helpers.toas
import com.alqorut.mystory.helpers.uriToFile
import com.alqorut.mystory.interfaces.StoryListener
import com.alqorut.mystory.models.Story
import com.alqorut.mystory.viewmodels.MainViewModel
import com.alqorut.mystory.viewmodels.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

class TambahActivity : AppCompatActivity(), StoryListener {
    private lateinit var binding: ActivityTambahStoryBinding

    private var getFile: File? = null
    private val MAXIMAL_SIZE = 1000000
    private lateinit var mainViewModel: MainViewModel
    private lateinit var repo : RepoStory
    private lateinit var config: Config

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.not_permisi),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun setupViewModel() {
        config = Config(this)
        val apiService = ApiConfig().getApiService(config.token.toString())
        repo = RepoStory(apiService)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repo)
        ).get(MainViewModel::class.java)
        mainViewModel.listener = this
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        setupViewModel()
        binding.camera.setOnClickListener { startCamera() }
        binding.galery.setOnClickListener { startGallery() }
        binding.upload.setOnClickListener { addStory() }

    }
    private fun addStory(){
        val desc = binding.descripsi.text.toString()

        when{
            getFile == null->{
                Toast.makeText(this@TambahActivity, getString(R.string.file_belum_ada), Toast.LENGTH_SHORT).show()
            }
            desc.isEmpty()->{
                Toast.makeText(this@TambahActivity, getString(R.string.masukan_desc), Toast.LENGTH_SHORT).show()
            }else ->{
               tambahStory()
            }
        }
    }

    override fun onSukses() {
        finish()
    }

    override fun onError(msg: String) {
        toas(this, msg)
        isProgress(this, false)
    }

    override fun onErrorResponse(eror: errorResponse) {
        super.onErrorResponse(eror)
        isProgress(this, false)
        msg(eror.message.toString())
        toas(this, eror.message.toString())
    }
    private fun tambahStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description =
                "Ini adalah deksripsi gambar".toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            mainViewModel.tambahStory(config.token.toString(), imageMultipart, description,"0".toRequestBody(),"0".toRequestBody())
        }
    }

    override fun onMulai() {
        super.onMulai()
        isProgress(this, true)
    }
    override fun onUploadResponse(response: Result<UploadResponse>) {
        super.onUploadResponse(response)
        if(response.isSuccess){
            toas(this,"upload suksess...")
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@TambahActivity)
                getFile = myFile
                binding.imagePhoto.setImageURI(uri)
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding.imagePhoto.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }
    fun rotateFile(file: File, isBackCamera: Boolean = false) {
        val matrix = Matrix()
        val bitmap = BitmapFactory.decodeFile(file.path)
        val rotation = if (isBackCamera) 90f else -90f
        matrix.postRotate(rotation)
        if (!isBackCamera) {
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
        val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }
}