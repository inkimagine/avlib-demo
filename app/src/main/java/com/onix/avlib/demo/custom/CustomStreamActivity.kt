package com.onix.avlib.demo.custom

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import com.onix.avlib.demo.ARG_SERVER_URL
import com.onix.avlib.demo.R
import com.onix.avlib.demo.util.ext.argument

class CustomStreamActivity : AppCompatActivity() {

    private val serverUrl by argument<String>(ARG_SERVER_URL)

    private val permissionHandleOptions: QuickPermissionsOptions by lazy {
        QuickPermissionsOptions(
            handleRationale = false,
            rationaleMessage = getString(R.string.alert_permissions),
            permanentlyDeniedMessage = getString(R.string.alert_permissions_denied_message),
            permissionsDeniedMethod = ::onPermissionDenied,
            permanentDeniedMethod = ::onPermissionDenied
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streamer)

        runWithPermissions(
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
            options = permissionHandleOptions
        ) { openCameraFragment() }
    }

    private fun openCameraFragment() {
        val tag = CustomStreamFragment::class.java.simpleName
        val fm = supportFragmentManager
        val streamFragment = fm.findFragmentByTag(tag)

        if (streamFragment == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer, CustomStreamFragment.instance(serverUrl), tag
            ).commitAllowingStateLoss()
        }
    }

    private fun onPermissionDenied(request: QuickPermissionsRequest) {
        AlertDialog.Builder(this)
            .setMessage(R.string.alert_permissions_denied_message)
            .setCancelable(false)
            .setPositiveButton(R.string.close) { _, _ -> finish() }
            .show()
    }
}
