package com.devapp.fr.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devapp.fr.databinding.ActivityInformationUserBinding

class InformationUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInformationUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {

    }
}