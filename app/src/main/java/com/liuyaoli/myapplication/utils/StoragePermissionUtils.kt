import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.liuyaoli.myapplication.MyApplication

object StoragePermissionUtils {

    private const val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 1001

    // 检查是否已授予读取外部存储的权限
    fun isReadExternalStoragePermissionGranted(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 请求读取外部存储权限
    fun requestReadExternalStoragePermission(activity: Activity) {
        if (!isReadExternalStoragePermissionGranted(activity)) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_PERMISSION_CODE
            )
        }
    }

    // 处理权限请求结果
    fun handlePermissionsResult(
        activity: Activity,
        requestCode: Int,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被授予，执行相应操作
                    // 例如：执行读取外部存储的操作
                    // readExternalStorage()
                } else {
                    // 权限被拒绝，可以向用户显示一个提示
                    Toast.makeText(activity, "读取外部存储权限被拒绝！", Toast.LENGTH_SHORT).show()
                }
            }
            // 可以添加其他权限请求的处理
        }
    }
}
