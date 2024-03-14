package com.example.passkeeper

import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    private val prefNaME = "MyPref"
    private var isHaveBiometric: Boolean = true
    private lateinit var biometricPrompt: BiometricPrompt
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}