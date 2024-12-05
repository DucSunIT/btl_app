package database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.appenglish.R
import data_class.SaveDetailWord
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

open class DatabaseHelper(var context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private var DATABASE_NAME = "dictionary3.db"
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    // hàm có chức năng mở db có sẵn
    fun openDatabase(): SQLiteDatabase {
        val dbPathFile = context.getDatabasePath(DATABASE_NAME)
        val file = File(dbPathFile.toString())
        /*kiểm tra xem db đã tồn tại chưa*/
        if (!dbPathFile.exists()) {
            Log.d("NOTI", "Database chưa tồn tại, sao chép database")
            copyDatabase(dbPathFile)
        } else {
            Log.d("NOTI", "Database đã tồn tại tại: ${dbPathFile.path}")
        }
        return SQLiteDatabase.openDatabase(dbPathFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    // hàm có chức năng copy db vào trong thư mục database của ứng dụng
    private fun copyDatabase(dbPathFile: File) {
        try {
            // Tạo thư mục cha nếu chưa tồn tại
            dbPathFile.parentFile?.mkdirs()

            val openDb = context.assets.open(DATABASE_NAME)
            val outputStream = FileOutputStream(dbPathFile)

            val buffer = ByteArray(1024)
            // ghi db
            while (openDb.read(buffer) > 0) {
                outputStream.write(buffer)
                Log.d("NOTI", "Đang sao chép database...")
            }
            // đẩy dữ liệu vào ouputstream
            outputStream.flush()
            outputStream.close()
            openDb.close()
            Log.d("NOTI", "Sao chép Database thành công")
        } catch (e: IOException) {
            Log.e("ERROR", "Lỗi sao chép Database: ${e.message}")
        }
    }

}