package jp.techacademy.takanari.firebasetest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.ArrayList

class SoundListAdapter(context: Context) : BaseAdapter() {

    private var mLayoutInflater: LayoutInflater
    private var mQuestionArrayList = ArrayList<Question>()

    init {
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return mQuestionArrayList.size
    }

    override fun getItem(position: Int): Any {
        return mQuestionArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView


        if (convertView == null) {
            convertView =
                mLayoutInflater!!.inflate(R.layout.activity_sound_list_adapter, parent, false)!!
        }

        val titleTextView = convertView.findViewById<View>(R.id.titleTextView) as TextView
        titleTextView.text = mQuestionArrayList[position].title

        return convertView
    }

    fun setQuestionArrayList(questionArrayList: ArrayList<Question>) {
        mQuestionArrayList = questionArrayList
    }
}