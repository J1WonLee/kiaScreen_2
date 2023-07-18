package com.copy.kiascreen

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.copy.kiascreen.application.KiaSampleApplication
import com.copy.kiascreen.databinding.DialogCheckPermissionBinding
import com.copy.kiascreen.util.PermissionUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FragmentIntroPermission : DialogFragment() {
    private val reGrants = arrayListOf<String>()
    private lateinit var binding : DialogCheckPermissionBinding
    private var onBackPressedCallback : OnBackPressedCallback? = null

    private var rejectPermissions = ""
    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
            permissions -> permissions.entries.forEach {
                Log.d("permissionTest", "${it.key} = ${it.value}")
                if (!it.value) {
                    rejectPermissions += ", ${it.key}"
                }
             }
        initFragmentResult()
    }


    override fun onStart() {
        super.onStart()
        this.isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCheckPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // request permission
//        requestPermission()
        binding.btnConfirm.setOnClickListener {
//            initFragmentResult()
            requestMultiplePermissions.launch(PermissionUtil.PERMISSIONS)
        }
    }

    private fun initFragmentResult() {
        Log.d("permissionTest", "initFragmentResult")
        dismiss()
        setFragmentResult("requestKey", bundleOf("resultKey" to true))
    }

    override fun onDestroy() {
        super.onDestroy()
        onBackPressedCallback?.remove()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}