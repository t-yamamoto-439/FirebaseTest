package jp.techacademy.takanari.firebasetest

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sound.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class SoundActivity : AppCompatActivity(), View.OnClickListener, DatabaseReference.CompletionListener{

    // 譜面データ
    private val soundList0 = ArrayList<SoundDto>()//休符
    private val soundList1 = ArrayList<SoundDto>()//ド
    private val soundList2 = ArrayList<SoundDto>()//レ
    private val soundList3 = ArrayList<SoundDto>()//ミ
    private val soundList4 = ArrayList<SoundDto>()//ファ
    private val soundList5 = ArrayList<SoundDto>()//ソ
    private val soundList6 = ArrayList<SoundDto>()//ラ
    private val soundList7 = ArrayList<SoundDto>()//シ
    private val soundList8 = ArrayList<SoundDto>()//ド(高)
    private val soundList9 = ArrayList<SoundDto>()//レ(高)
    private val soundList10 = ArrayList<SoundDto>()//ミ(高)
    private val soundList11 = ArrayList<SoundDto>()//ファ(高)


    //タイマーが動いてる時に時間数える変数
    private var mTimer: Timer? = null
    //不明　ハンドラーを入れてる？
    private var mHandler = Handler()

    private lateinit var msoundref: DatabaseReference

    private lateinit var mTitleRef: DatabaseReference

    private  var mTitle:String?= null

    val user = FirebaseAuth.getInstance().currentUser

    private var mAdapter: SoundActivityAdapter = SoundActivityAdapter()


    var i = 0

    var count = 0

    //マスのtruefalse管理する変数
    var x1 = ""
    var x2 = ""
    var x3 = ""
    var x4 = ""
    var x5 = ""
    var x6 = ""
    var x7 = ""
    var x8 = ""
    var x9 = ""
    var x10 = ""
    var x11 = ""


    private val mEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
//            val map = dataSnapshot.value as Map<String, Any>

            val Title = dataSnapshot.key
//            val Test = dataSnapshot.value as Map<String, String>

            val body = dataSnapshot.child("$count").value as String


            if (Title  == "scaleA") {
                for (i in 0 until 59) {
                    val titleTest = body
                    if (titleTest.toInt() == 1) {
                        val test = SoundDto(true)
                        soundList1.add(test)
                        mAdapter.notifyDataSetChanged()


                    } else if (titleTest.toInt() == 0) {
                        val test = SoundDto(false)
                        soundList1.add(test)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }

        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {

        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound)

        val extras = intent.extras
        if (extras != null){
            mTitle = extras.getString("title")
        }


        savebutton.setOnClickListener(this)

        val dataBaseReference = FirebaseDatabase.getInstance().reference
        val uid = FirebaseAuth.getInstance().currentUser!!.uid


        msoundref = dataBaseReference.child(uid)

        view1.text="ド"
        view2.text="レ"
        view3.text="ミ"
        view4.text="ファ"
        view5.text="ソ"
        view6.text="ラ"
        view7.text="シ"
        view8.text="ド"
        view9.text="レ"
        view10.text="ミ"
        view11.text="ファ"


        if (mTitle == null){
            newCreate()
        }else {
            LoadMelody()
        }


        startbutton.setOnClickListener(mOnstartClickListener)
        Sound.getInstance(this)


        recyclerView1.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView1.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView1.adapter = SoundActivityAdapter(soundList1) { position ->
            //音を鳴らすコード書くとよい

            if (soundList1[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_D)
            }
            else if (soundList1[position].addflag == true){
            }
        }
        recyclerView2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView2.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView2.adapter = SoundActivityAdapter(soundList2) { position ->
            //音を鳴らすコード書くとよい

            if (soundList2[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_Re)
            }
            else if (soundList2[position].addflag == true){
            }
        }
        recyclerView3.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView3.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView3.adapter = SoundActivityAdapter(soundList3) { position ->
            //音を鳴らすコード書くとよい


            if (soundList3[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_M)
            }
            else if (soundList3[position].addflag == true){
            }
        }
        recyclerView4.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView4.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView4.adapter = SoundActivityAdapter(soundList4) { position ->
            //音を鳴らすコード書くとよい

            if (soundList4[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_F)
            }
            else if (soundList4[position].addflag == true){
            }
        }
        recyclerView5.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView5.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView5.adapter = SoundActivityAdapter(soundList5) { position ->
            //音を鳴らすコード書くとよい

            if (soundList5[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_So)
            }
            else if (soundList5[position].addflag == true){
            }
        }
        recyclerView6.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView6.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView6.adapter = SoundActivityAdapter(soundList6) { position ->
            //音を鳴らすコード書くとよい

            if (soundList6[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_Ra)
            }
            else if (soundList6[position].addflag == true){
            }
        }
        recyclerView7.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView7.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView7.adapter = SoundActivityAdapter(soundList7) { position ->
            //音を鳴らすコード書くとよい


            if (soundList7[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_Si)
            }
            else if (soundList7[position].addflag == true){
            }
        }
        recyclerView8.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView8.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView8.adapter = SoundActivityAdapter(soundList8) { position ->

            if (soundList8[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_D2)
            }
            else if (soundList8[position].addflag == true){
            }

        }
        recyclerView9.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView9.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView9.adapter = SoundActivityAdapter(soundList9) { position ->

            if (soundList9[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_Re2)
            }
            else if (soundList9[position].addflag == true){
            }
        }
        recyclerView10.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView10.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView10.adapter = SoundActivityAdapter(soundList10) { position ->

            if (soundList10[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_M2)
            }
            else if (soundList10[position].addflag == true){
            }
        }
        recyclerView11.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // 1
        recyclerView11.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL)) // 2
        recyclerView11.adapter = SoundActivityAdapter(soundList11) { position ->

            if (soundList11[position].addflag == false) {
                Sound.getInstance(this).playSound(Sound.SOUND_F2)
            }
            else if (soundList11[position].addflag == true){
            }

        }
        if (user != null) {
            mTitleRef = dataBaseReference.child(user.uid).child(mTitle.toString())
            mTitleRef!!.addChildEventListener(mEventListener)
        }
    }




    private val mOnstartClickListener = View.OnClickListener {
        //タイマーが動いてない時
        if (mTimer == null) {

            mTimer = Timer()

            mTimer!!.schedule(object : TimerTask() {

                override fun run() {

                    mHandler.post {
                        if (soundList1[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_D)
                        }
                        else if (soundList1[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }

                        if (soundList2[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_Re)
                        }
                        else if (soundList2[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }
                        if (soundList3[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_M)
                        }
                        else if (soundList3[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }
                        if (soundList4[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_F)
                        }
                        else if (soundList4[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }
                        if (soundList5[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_So)
                        }
                        else if (soundList5[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }
                        if (soundList6[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_Ra)
                        }
                        else if (soundList6[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }

                        if (soundList7[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_Si)
                        }
                        else if (soundList7[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }
                        if (soundList8[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_D2)
                        }
                        else if (soundList8[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }
                        if (soundList9[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_Re2)
                        }
                        else if (soundList9[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }
                        if (soundList10[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_M2)
                        }
                        else if (soundList10[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }
                        if (soundList11[i].addflag == true) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_F2)
                        }
                        else if (soundList11[i].addflag == false) {
                            Sound.getInstance(this@SoundActivity).playSound(Sound.SOUND_N)
                        }

                        i+=1
                        if (i == 60){
                            mTimer!!.cancel()
                            mTimer = null
                            i = 0
                        }
                    }
                }
            }, 500, 500) // 最初に始動させるまで 500ミリ秒、ループの間隔を 500ミリ秒 に設定
            //動いている時
        } else if (mTimer != null) {
            mTimer!!.cancel()
            //ヌルにする
            mTimer = null
            i = 0
        }
    }

    override fun onClick(v: View){
        if (v === savebutton) {

            val title = titleText.text.toString()

//            のちのちいる
//            if (title.isEmpty()) {
//                // タイトルが入力されていない時はエラーを表示するだけ
//                Snackbar.make(v, "タイトルを入力して下さい", Snackbar.LENGTH_LONG).show()
//                return
//            }


            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

            //タイトルの下に音階11音セット
            val msoundref2 = msoundref.child(title).child("scaleA")
            val msoundref3 = msoundref.child(title).child("scaleB")
            val msoundref4 = msoundref.child(title).child("scaleC")
            val msoundref5 = msoundref.child(title).child("scaleD")
            val msoundref6 = msoundref.child(title).child("scaleE")
            val msoundref7 = msoundref.child(title).child("scaleF")
            val msoundref8 = msoundref.child(title).child("scaleG")
            val msoundref9 = msoundref.child(title).child("scaleH")
            val msoundref10 = msoundref.child(title).child("scaleI")
            val msoundref11 = msoundref.child(title).child("scaleJ")
            val msoundref12 = msoundref.child(title).child("scaleK")

            //マスとtrueとfalseを管理
            val data = HashMap<String, String>()
            val data2 = HashMap<String, String>()
            val data3 = HashMap<String, String>()
            val data4 = HashMap<String, String>()
            val data5 = HashMap<String, String>()
            val data6 = HashMap<String, String>()
            val data7 = HashMap<String, String>()
            val data8 = HashMap<String, String>()
            val data9 = HashMap<String, String>()
            val data10 = HashMap<String, String>()
            val data11 = HashMap<String, String>()

            for (i in 0 until 60) {

                //マスのtruefalseを管理
                if(soundList1[i].addflag==true){
                    x1 = 1.toString()
                }else{
                    x1 = 0.toString()
                }
                if(soundList2[i].addflag==true){
                    x2 = 1.toString()
                }else{
                    x2 = 0.toString()
                }
                if(soundList3[i].addflag==true){
                    x3 = 1.toString()
                }else{
                    x3 = 0.toString()
                }
                if(soundList4[i].addflag==true){
                    x4 = 1.toString()
                }else{
                    x4 = 0.toString()
                }
                if(soundList5[i].addflag==true){
                    x5 = 1.toString()
                }else{
                    x5 = 0.toString()
                }
                if(soundList6[i].addflag==true){
                    x6 = 1.toString()
                }else{
                    x6 = 0.toString()
                }
                if(soundList7[i].addflag==true){
                    x7 = 1.toString()
                }else{
                    x7 = 0.toString()
                }
                if(soundList8[i].addflag==true){
                    x8 = 1.toString()
                }else{
                    x8 = 0.toString()
                }
                if(soundList9[i].addflag==true){
                    x9 = 1.toString()
                }else{
                    x9 = 0.toString()
                }
                if(soundList10[i].addflag==true){
                    x10 = 1.toString()
                }else{
                    x10 = 0.toString()
                }
                if(soundList11[i].addflag==true){
                    x11 = 1.toString()
                }else{
                    x11 = 0.toString()
                }

                //マスとtruefalseを入れる準備
                data["$count"] = x1
                data2["$count"] = x2
                data3["$count"] = x3
                data4["$count"] = x4
                data5["$count"] = x5
                data6["$count"] = x6
                data7["$count"] = x7
                data8["$count"] = x8
                data9["$count"] = x9
                data10["$count"] = x10
                data11["$count"] = x11

                //マスとtruefalseを入れる(1:1など)
                msoundref2.setValue(data)
                msoundref3.setValue(data2)
                msoundref4.setValue(data3)
                msoundref5.setValue(data4)
                msoundref6.setValue(data5)
                msoundref7.setValue(data6)
                msoundref8.setValue(data7)
                msoundref9.setValue(data8)
                msoundref10.setValue(data9)
                msoundref11.setValue(data10)
                msoundref12.setValue(data11)

                count++

            }
            count=0
        }
    }

    override fun onComplete(databaseError: DatabaseError?, databaseReference: DatabaseReference) {
        progressBar.visibility = View.GONE

        //Firebaseへの保存が完了したらfinishメソッドを呼び出してActivityを閉じる
        if (databaseError == null) {
            finish()
        } else {
            Snackbar.make(findViewById(android.R.id.content), "保存に失敗しました", Snackbar.LENGTH_LONG).show()
        }
    }

    fun newCreate(){
        for (i in 0 until 60) {
            soundList1.add(
                SoundDto(
                )
            )
            soundList2.add(
                SoundDto(
                )
            )
            soundList3.add(
                SoundDto(
                )
            )
            soundList4.add(
                SoundDto(
                )
            )
            soundList5.add(
                SoundDto(
                )
            )
            soundList6.add(
                SoundDto(
                )
            )
            soundList7.add(
                SoundDto(
                )
            )
            soundList8.add(
                SoundDto(
                )
            )
            soundList9.add(
                SoundDto(
                )
            )
            soundList10.add(
                SoundDto(
                )
            )
            soundList11.add(
                SoundDto(
                )
            )
            soundList0.add(
                SoundDto(
                )
            )
        }
    }

    fun LoadMelody(){
        for (i in 0 until 60) {
            soundList2.add(
                SoundDto(
                )
            )
            soundList3.add(
                SoundDto(
                )
            )
            soundList4.add(
                SoundDto(
                )
            )
            soundList5.add(
                SoundDto(
                )
            )
            soundList6.add(
                SoundDto(
                )
            )
            soundList7.add(
                SoundDto(
                )
            )
            soundList8.add(
                SoundDto(
                )
            )
            soundList9.add(
                SoundDto(
                )
            )
            soundList10.add(
                SoundDto(
                )
            )
            soundList11.add(
                SoundDto(
                )
            )
            soundList0.add(
                SoundDto(
                )
            )
        }
    }
}