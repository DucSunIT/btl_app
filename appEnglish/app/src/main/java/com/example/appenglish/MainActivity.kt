package com.example.appenglish

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appenglish.databinding.ActivityMainBinding

@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var helper: DatabaseHelper
    private val listMember = mutableListOf<ListMember>()
    private val wordList = mutableListOf<String>()
    private lateinit var popupWindow: PopupWindow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // create viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        addEvents()

        val searchView: SearchView = findViewById(R.id.searchView)

        // Đảm bảo SearchView đã được khởi tạo xong trước khi tìm kiếm searchPlate
//        searchView.viewTreeObserver.addOnPreDrawListener {
//            val searchPlate = searchView.findViewById<LinearLayout>(androidx.appcompat.R.id.search_plate)
//            searchPlate?.setBackgroundColor(Color.TRANSPARENT) // Loại bỏ gạch chân
//            true
//        }

    }

    private fun addEvents() {
        handleLayoutVocabulary()
        handleClickProfile()
        displayListUser()
        handleClickVip()
        popupSuggest2()
    }

    /*
    * Hàm có chức năng xử lý sự kiện khi người dụng click vào
    * hình ảnh VIP sẽ hiện ra một thông báo (AlertDialog)*/
    private fun handleClickVip() {
        binding.imgPremium.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.apply {
                // tiêu đề dialog
                setTitle("Thông báo")
                // nội dung dialog
                setMessage("Chức năng đang phát triển !")
                // xử lí nút OK, người dùng bấm OK dialog sẽ ẩn đi
                setNegativeButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }
            }
            // hiển thị dialog
            dialog.show()
        }
    }

    /*Hàm có chức năng hiển thị các thành viên trong nhóm thông qua ListView*/
    private fun displayListUser() {
        /*Vẽ lên ListView sử dụng adapter, dữ liệu laasy từ csdl*/
        helper = DatabaseHelper(this)
        helper.openDatabase()
        handleDataMembers(helper)
        val customLV = CustomListMember(this, listMember)
        binding.lvMemberGroup.adapter = customLV


    }

    /*Hàm có chức năng xử lý sự kiện khi người dùng click vào hình ảnh
    * user trên màn hình, sẽ chuyển hướng người dùng sang LoginActivity*/
    private fun handleClickProfile() {
        binding.imgProfile.setOnClickListener {
            val intentProfile = Intent(this, LoginActivity::class.java)
            startActivity(intentProfile)
        }
    }

    /*Hàm có chức năng xử lý sự kiện khi người dùng click vào layout từ vựng
     * trên màn hình, sẽ chuyển hướng người dùng sang MainVocabulary*/
    private fun handleLayoutVocabulary() {
        binding.layoutDictonary.setOnClickListener {
            val intentMainVocabulary = Intent(this, MainVocabulary::class.java)
            startActivity(intentMainVocabulary)
        }
    }

    /*Hàm có chức năng truy vấn csdl và đẩy dữ liệu vào List database*/
    @SuppressLint("Recycle")
    private fun handleDataMembers(helper: DatabaseHelper) {
        val db = helper.readableDatabase
        val res = db.rawQuery("SELECT FULLNAME FROM MEMBERS", null)
        if (res.moveToFirst()) {
            listMember.clear()
            do {
                val fullName = res.getString(0)
                listMember.add(ListMember(R.drawable.profile, fullName, R.drawable.star_vip))
            } while (res.moveToNext())
        }

        res.close()
    }

    private fun popupSuggest() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_suggest, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            800
        )
        popupWindow.isFocusable = false
        popupWindow.isOutsideTouchable = true

        // Gắn RecyclerView vào PopupWindow
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.rvSuggest)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RvAdapter(wordList) { selectedItem ->
            binding.searchView.setQuery(selectedItem, false)
            popupWindow.dismiss()

            // Tạo intent để chuyển sang SearchWordActivity
            val intent = Intent(this, SearchWordActivity::class.java)
            val bundle = Bundle()

            // Truy vấn cơ sở dữ liệu để lấy thông tin chi tiết cho từ đã chọn
            val helper = DatabaseHelper(applicationContext)
            helper.openDatabase()
            val db = helper.readableDatabase
            val list = ArrayList<SaveDetailWord>()

            // Truy vấn cơ sở dữ liệu với từ đã chọn
            val res = db.rawQuery(
                "SELECT * FROM dictionary WHERE word LIKE ?",
                arrayOf("$selectedItem%"),
                null
            )
            if (res.moveToFirst()) {
                do {
                    val word = res.getString(res.getColumnIndexOrThrow("word"))
                    val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                    val type = res.getString(res.getColumnIndexOrThrow("type"))
                    val definition = res.getString(res.getColumnIndexOrThrow("definition"))
                    list.add(SaveDetailWord(word, ipa, R.drawable.loa, type, definition))
                } while (res.moveToNext())
            } else {
                Log.d("Database", "No data found for $selectedItem")
            }
            res.close()

            // Nếu có dữ liệu, thêm vào bundle
            if (list.isNotEmpty()) {
                bundle.putParcelableArrayList("LIST", list)
            }

            // Truyền bundle vào intent và chuyển tới SearchWordActivity
            intent.putExtras(bundle)
            startActivity(intent)


        }

        recyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(this, R.drawable.custom_line)!!
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        // Hiển thị PopupWindow khi SearchView được focus
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                popupWindow.showAsDropDown(binding.searchView)
            } else {
                popupWindow.dismiss()
            }
        }

        // Lọc gợi ý khi người dùng nhập
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                popupWindow.dismiss()

                if (!query.isNullOrEmpty()) {
                    Toast.makeText(this@MainActivity, "Search $query", Toast.LENGTH_SHORT).show()

                    // Tạo intent để chuyển sang SearchWordActivity
                    val intent = Intent(this@MainActivity, SearchWordActivity::class.java)

                    val bundle = Bundle()
                    val helper = DatabaseHelper(applicationContext)
                    helper.openDatabase()
                    val db = helper.readableDatabase
                    val list = ArrayList<SaveDetailWord>()

                    // Truy vấn cơ sở dữ liệu
                    val res = db.rawQuery(
                        "select * from dictionary where word like ?",
                        arrayOf("$query%"),
                        null
                    )
                    Log.d("QUERY", "$query")

                    // Kiểm tra kết quả truy vấn
                    if (res != null && res.moveToFirst()) {
                        do {
                            val word = res.getString(res.getColumnIndexOrThrow("word"))
                            val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                            val type = res.getString(res.getColumnIndexOrThrow("type"))
                            val definition = res.getString(res.getColumnIndexOrThrow("definition"))
                            Log.d("WORDMAIN", "$word")

                            list.add(SaveDetailWord(word, ipa, R.drawable.loa, type, definition))
                        } while (res.moveToNext())
                    }

                    Log.d("RES", "${res?.count}")
                    res?.close()

                    // Kiểm tra danh sách trước khi đưa vào bundle
                    if (list.isNotEmpty()) {
                        bundle.putParcelableArrayList("LIST", list)
                    } else {
                        Log.d("LIST", "No words found for the query")
                    }

                    // Truyền dữ liệu vào intent và mở SearchWordActivity
                    intent.putExtras(bundle)
                    Log.d("INTENT", "$intent")
                    startActivity(intent)

                    // Đóng bàn phím
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    popupWindow.dismiss()
                    return false
                }
                val query = "${newText}%"
                helper = DatabaseHelper(applicationContext)
                helper.openDatabase()
                val db = helper.readableDatabase
                wordList.clear()
                val res = db.rawQuery(
                    "select word, ipa, type from dictionary where word like ?", arrayOf(query), null
                )
                Log.d("QUERY", "$query")
                if (res.moveToFirst()) {
                    do {
                        val word = res.getString(res.getColumnIndexOrThrow("word"))
                        val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                        val type = res.getString(res.getColumnIndexOrThrow("type"))
                        Log.d("DATA", "Fetched word: $word")
                        wordList.add("$word \n/$ipa/ \n$type")
                    } while (res.moveToNext())
                }
                res.close()

                if (wordList.isEmpty()) {
                    popupWindow.dismiss()
                } else {
                    adapter.updateData(wordList)
                    if (!popupWindow.isShowing) {
                        popupWindow.showAsDropDown(binding.searchView)
                    }
                }
                return true
            }
        })
    }

    private fun popupSuggest2() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_suggest, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            800
        )
        popupWindow.isFocusable = false
        popupWindow.isOutsideTouchable = true

        // Gắn RecyclerView vào PopupWindow
        val recyclerView = popupView.findViewById<RecyclerView>(R.id.rvSuggest)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RvAdapter(wordList) { selectedItem ->
            // Đặt giá trị vào searchView và đóng PopupWindow
            binding.searchView.setQuery(selectedItem, false)
            popupWindow.dismiss()

            // Chuyển sang SearchWordActivity
            val intent = Intent(this, SearchWordActivity::class.java)
            val bundle = Bundle()

            // Truy vấn cơ sở dữ liệu để lấy thông tin chi tiết cho từ đã chọn
            val helper = DatabaseHelper(applicationContext)
            helper.openDatabase()
            val db = helper.readableDatabase
            val list = ArrayList<SaveDetailWord>()

            // Truy vấn cơ sở dữ liệu với từ đã chọn
            val res = db.rawQuery(
                "SELECT * FROM dictionary WHERE word LIKE ?",
                arrayOf("$selectedItem%"),
                null
            )
            if (res.moveToFirst()) {
                do {
                    val word = res.getString(res.getColumnIndexOrThrow("word"))
                    val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                    val type = res.getString(res.getColumnIndexOrThrow("type"))
                    val definition = res.getString(res.getColumnIndexOrThrow("definition"))
                    list.add(SaveDetailWord(
                        word ?: "Unknown",
                        "/${ipa ?: "Unknown IPA"}/",
                        R.drawable.loa,
                        type ?: "Unknown Type",
                        definition ?: "No definition"
                    ))

                } while (res.moveToNext())
            } else {
                Log.d("Database", "No data found for $selectedItem")
            }
            res.close()

            // Nếu có dữ liệu, thêm vào bundle
            if (list.isNotEmpty()) {
                bundle.putParcelableArrayList("LIST", list)
            }

            // Truyền bundle vào intent và chuyển tới SearchWordActivity
            intent.putExtras(bundle)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(this, R.drawable.custom_line)!!
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Hiển thị PopupWindow khi SearchView được focus
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                popupWindow.showAsDropDown(binding.searchView)
            } else {
                popupWindow.dismiss()
            }
        }

        // Lọc gợi ý khi người dùng nhập
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                popupWindow.dismiss()

                if (!query.isNullOrEmpty()) {
                    Toast.makeText(this@MainActivity, "Search $query", Toast.LENGTH_SHORT).show()

                    // Chuyển sang SearchWordActivity
                    val intent = Intent(this@MainActivity, SearchWordActivity::class.java)
                    val bundle = Bundle()
                    val helper = DatabaseHelper(applicationContext)
                    helper.openDatabase()
                    val db = helper.readableDatabase
                    val list = ArrayList<SaveDetailWord>()

                    // Truy vấn cơ sở dữ liệu
                    val res = db.rawQuery(
                        "select * from dictionary where word like ? limit 50",
                        arrayOf("$query%"),
                        null
                    )
                    Log.d("QUERY", "$query")

                    if (res != null && res.moveToFirst()) {
                        do {
                            val word = res.getString(res.getColumnIndexOrThrow("word"))
                            val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                            val type = res.getString(res.getColumnIndexOrThrow("type"))
                            val definition = res.getString(res.getColumnIndexOrThrow("definition"))
                            Log.d("WORDMAIN", "$word")

                            list.add(SaveDetailWord(
                                word ?: "Unknown",
                                "/${ipa ?: "Unknown IPA"}/",
                                R.drawable.loa,
                                type ?: "Unknown Type",
                                definition ?: "No definition"
                            ))

                        } while (res.moveToNext())
                    }

                    Log.d("RES", "${res?.count}")
                    res?.close()

                    // Kiểm tra danh sách trước khi đưa vào bundle
                    if (list.isNotEmpty()) {
                        bundle.putParcelableArrayList("LIST", list)
                    } else {
                        Log.d("LIST", "No words found for the query")
                    }

                    // Truyền dữ liệu vào intent và mở SearchWordActivity
                    intent.putExtras(bundle)
                    Log.d("INTENT", "$intent")
                    startActivity(intent)

                    // Đóng bàn phím
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    popupWindow.dismiss()
                    return false
                }

                val query = "${newText}%"
                val helper = DatabaseHelper(applicationContext)
                helper.openDatabase()
                val db = helper.readableDatabase
                wordList.clear()
                val res = db.rawQuery(
                    "select word, ipa, type from dictionary where word like ? limit 50", arrayOf(query), null
                )
                Log.d("QUERY", "$query")

                if (res.moveToFirst()) {
                    do {
                        val word = res.getString(res.getColumnIndexOrThrow("word"))
                        val ipa = res.getString(res.getColumnIndexOrThrow("ipa"))
                        val type = res.getString(res.getColumnIndexOrThrow("type"))
                        Log.d("DATA", "Fetched word: $word")
                        wordList.add("$word \n/$ipa/ \n$type")
                    } while (res.moveToNext())
                }
                res.close()

                if (wordList.isEmpty()) {
                    popupWindow.dismiss()
                } else {
                    adapter.updateData(wordList)
                    if (!popupWindow.isShowing) {
                        popupWindow.showAsDropDown(binding.searchView)
                    }
                }
                return true
            }
        })
    }


}