package com.example.skin_cancer_detection

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Button
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.view.GravityCompat;
import com.google.android.material.navigation.NavigationView;
import com.example.skin_cancer_detection.databinding.ActivityMainBinding
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.util.*
import java.text.SimpleDateFormat




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var isNavigationOpen = false
    lateinit var binding:ActivityMainBinding
    lateinit var filePath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var requestGalleryLauncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            try{
                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                val option = BitmapFactory.Options()
                option.inSampleSize=calRatio

                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream,null,option)
                inputStream!!.close()
                inputStream=null

                bitmap?.let{
                    binding.imageView2.setImageBitmap(bitmap)
                }?: let{
                    Log.d("kkang","bitmap null")
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.Gallerybutton2.setOnClickListener{

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type="image/*"
            requestGalleryLauncher.launch(intent)
        }

        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            val calRatio = calculateInSampleSize(
                Uri.fromFile(File(filePath)),
                resources.getDimensionPixelSize(R.dimen.imgSize),
                resources.getDimensionPixelSize(R.dimen.imgSize)
            )

            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                binding.imageView2.setImageBitmap(bitmap)
            }
        }

        binding.Camerabutton.setOnClickListener{

            val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File?= getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file=File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            filePath=file.absolutePath
            val photoURI: Uri= FileProvider.getUriForFile(
                this,
                "com.example.skin_cancer_detection.fileprovider", file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            requestCameraFileLauncher.launch(intent)

        }

        setToolbar()

        navigationView.setNavigationItemSelectedListener(this)

    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)


            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
   
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }





    private fun setToolbar(){
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar!!.setDisplayShowTitleEnabled(false)




    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)		
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            android.R.id.home->{ 
                drawerLayout.openDrawer(GravityCompat.START)
            }
            R.id.menu_about-> Snackbar.make(toolbar,"About menu pressed",Snackbar.LENGTH_SHORT).show()

        }
        return super.onOptionsItemSelected(item)
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){  

            R.id.hospital-> startActivity(Intent(this@MainActivity,Hospital::class.java))       }

        drawerLayout.closeDrawers() 
        return false
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }

    }











}
