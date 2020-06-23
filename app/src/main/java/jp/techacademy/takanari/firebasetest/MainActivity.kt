package jp.techacademy.takanari.firebasetest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAdapter: SoundListAdapter
    private lateinit var mListView: ListView
    //    private lateinit var mSoundRef: DatabaseReference
    private lateinit var mQuestionArrayList: ArrayList<Question>
//    val dataBaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var mDatabaseReference: DatabaseReference
    private var msoundref: DatabaseReference? = null
    val user = FirebaseAuth.getInstance().currentUser

    private var mTitleRef: DatabaseReference? = null




    private val mEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            val map = dataSnapshot.value as Map<String, String>

            val title = dataSnapshot.key

            val question = Question(title.toString())
            mQuestionArrayList.add(question)
            mAdapter.notifyDataSetChanged()
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
        setContentView(R.layout.activity_main)

        mDatabaseReference = FirebaseDatabase.getInstance().reference


        // ListViewの準備
        mListView = findViewById(R.id.listView)
        mAdapter = SoundListAdapter(this)
        mQuestionArrayList = ArrayList<Question>()
        mAdapter.setQuestionArrayList(mQuestionArrayList)
        mListView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->

            if (user == null) {
                // ログインしていなければログイン画面に遷移させる
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }else {
                val intent = Intent(applicationContext, SoundActivity::class.java)
                startActivity(intent)
            }
        }

        if (user != null) {
            msoundref = mDatabaseReference.child(user.uid)
            msoundref!!.addChildEventListener(mEventListener)
        }




        mListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext,SoundActivity::class.java)
            intent.putExtra("title", mQuestionArrayList[position].title)
            startActivity(intent)
        }

        //todo 削除機能
        mListView.setOnItemLongClickListener { parent, view, position, id ->

            val task = parent.adapter.getItem(position) as Question

            val title = mQuestionArrayList[position].title

            mTitleRef = mDatabaseReference.child(user!!.uid).child(title)

            // ダイアログを表示する
            val builder = AlertDialog.Builder(this@MainActivity)

            builder.setTitle("削除")
            builder.setMessage(task.title + "を削除しますか")

            builder.setPositiveButton("OK") { _, _ ->


                mTitleRef!!.removeValue()
                mQuestionArrayList.clear()
                mAdapter.setQuestionArrayList(mQuestionArrayList)
                mListView.adapter = mAdapter
                msoundref!!.addChildEventListener(mEventListener)


            }
            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()
            true
        }
    }


    //設定がうまくできているか＊あまり気にしない
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(applicationContext, SettingActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}