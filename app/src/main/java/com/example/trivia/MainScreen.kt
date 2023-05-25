package com.example.trivia

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonArray
import kotlin.random.Random
import okhttp3.OkHttpClient
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.net.URL

class MainScreen: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private var num=1
    private var selectedOption=""
    private var selectedbtn=0
    private var scr=0
    private var randomize=IntArray(4)
    private var btnclicked=false
    private var finalscr="0"
    private var nxtscrn: Int?=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)
        val quest: TextView=findViewById(R.id.question)
        val opt1: Button=findViewById(R.id.opt1)
        val opt2: Button=findViewById(R.id.opt2)
        val opt3: Button=findViewById(R.id.opt3)
        val opt4: Button=findViewById(R.id.opt4)
        val btn: Button=findViewById(R.id.next)
        val subbtn: Button=findViewById(R.id.submitbutton)
        val score: TextView=findViewById(R.id.score)
        val fnsbtn: Button=findViewById(R.id.finish)
        val fnltxt: TextView=findViewById(R.id.fnlscr)
        val rstbtn: Button=findViewById(R.id.rstrt)
        for(i in 0..3){
            randomize[i]=i
        }
        score.text="$scr"
         num=1
        val bundle: Bundle?=intent.extras
        nxtscrn=bundle?.getInt("check")
        val ques=ArrayList<functionality>()
        if(nxtscrn==1) {
            for (i in 0 until 49) {
                ques.add(readjson(i, "file.json"))
            }
        }
        else if(nxtscrn==2){
            for( i in 0 until 50){
                ques.add(readjson(i,"science.json"))
            }
        }
        else if(nxtscrn==3){
            for( i in 0 until 50){
                ques.add(readjson(i,"computers.json"))
            }
        }
        else if(nxtscrn==4){
            for( i in 0 until 50){
                ques.add(readjson(i,"general_knowledge.json"))
            }
        }
        else if(nxtscrn==5){
            for( i in 0 until 50){
                ques.add(readjson(i,"sports.json"))
            }
        }
        quest.text=ques[num-1].question
        opt1.text=ques[num-1].incorrectAnswer[0]
        opt2.text=ques[num-1].incorrectAnswer[1]
        opt3.text=ques[num-1].correctAnswer
        opt4.text=ques[num-1].incorrectAnswer[2]
        val listOfbtns=listOfButtons(opt1, opt2, opt3, opt4)
        for(i in 0..3){
            listOfbtns.get(i).setBackgroundColor(Color.BLACK)
        }

        for(i in 0..3){
            getselctedOption(listOfbtns,i)
        }

        btn.setOnClickListener {
            try {
                btnclicked=false
                num++
                val index1: Int = Random.nextInt(0, 4)
                val index2: Int = Random.nextInt(0, 4)
                swap(randomize, index1, index2)
                quest.text = ques[num - 1].question
                listOfbtns.get(randomize[0]).text = ques[num - 1].incorrectAnswer[0]
                listOfbtns.get(randomize[1]).text = ques[num - 1].incorrectAnswer[1]
                listOfbtns.get(randomize[2]).text = ques[num - 1].correctAnswer
                listOfbtns.get(randomize[3]).text = ques[num - 1].incorrectAnswer[2]
                for (i in 0..3) {
                    listOfbtns.get(i).setBackgroundColor(Color.BLACK)
                    listOfbtns.get(i).isClickable = true
                }
            }
            catch (e:IndexOutOfBoundsException){
                Toast.makeText(this, "Questions finished",Toast.LENGTH_SHORT)
            }
        }
        subbtn.setOnClickListener {
            if (btnclicked) {
                if (selectedOption == ques[num - 1].correctAnswer) {
                    listOfbtns.get(selectedbtn).setBackgroundColor(Color.GREEN)
                    Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show()
                    scr++
                    score.text = "$scr"
                } else {
                    listOfbtns.get(selectedbtn).setBackgroundColor(Color.RED)
                    Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show()
                }

                for (i in 0..3) {
                    listOfbtns.get(i).isClickable = false
                }
            }
            else{
                Toast.makeText(this,"No option selected, click next",Toast.LENGTH_SHORT).show()
            }
        }
        fnsbtn.setOnClickListener {
            val shrd: SharedPreferences=getSharedPreferences("fnlscr", MODE_PRIVATE)
            val editor=shrd.edit()
            subbtn.isClickable=false
            btn.isClickable=false
            fnsbtn.isClickable=false
            for (i in 0..3) {
                listOfbtns.get(i).isClickable = false
            }
             finalscr="$scr"
            editor.putString("finalstr",finalscr)
            editor.apply()
            fnltxt.text=finalscr
        }
        rstbtn.setOnClickListener {
            num=1
            scr=0
            score.text="$scr"
            val index1: Int = Random.nextInt(0, 4)
            val index2: Int = Random.nextInt(0, 4)
            swap(randomize, index1, index2)
            quest.text = ques[num - 1].question
            listOfbtns.get(randomize[0]).text = ques[num - 1].incorrectAnswer[0]
            listOfbtns.get(randomize[1]).text = ques[num - 1].incorrectAnswer[1]
            listOfbtns.get(randomize[2]).text = ques[num - 1].correctAnswer
            listOfbtns.get(randomize[3]).text = ques[num - 1].incorrectAnswer[2]
            subbtn.isClickable=true
            btn.isClickable=true
            fnsbtn.isClickable=true
            for (i in 0..3) {
                listOfbtns.get(i).isClickable = true
                listOfbtns.get(i).setBackgroundColor(Color.BLACK)
            }
        }
        val gtshrd: SharedPreferences=getSharedPreferences("fnlscr", MODE_PRIVATE)
        val str=gtshrd.getString("finalstr","")
        fnltxt.text=str
    }
    private fun listOfButtons(opt1: Button,opt2: Button,opt3: Button,opt4: Button): ArrayList<Button>{
        val listofbtns=ArrayList<Button>( )
        listofbtns.add(0,opt1)
        listofbtns.add(1,opt2)
        listofbtns.add(2,opt3)
        listofbtns.add(3,opt4)
        return listofbtns
    }
    private fun getselctedOption(list: ArrayList<Button>,i: Int){
        list.get(i).setOnClickListener {
            list.get(i).setBackgroundColor(Color.YELLOW)
            selectedbtn=i
            selectedOption=list.get(i).text.toString()
            btnclicked=true
            if(i==0){
                list.get(i+1).setBackgroundColor(Color.BLACK)
                list.get(i+2).setBackgroundColor(Color.BLACK)
                list.get(i+3).setBackgroundColor(Color.BLACK)
            }
            if(i==1){
                list.get(i-1).setBackgroundColor(Color.BLACK)
                list.get(i+1).setBackgroundColor(Color.BLACK)
                list.get(i+2).setBackgroundColor(Color.BLACK)
            }
            if(i==2){
                list.get(i-2).setBackgroundColor(Color.BLACK)
                list.get(i-1).setBackgroundColor(Color.BLACK)
                list.get(i+1).setBackgroundColor(Color.BLACK)
            }
            if(i==3){
                list.get(i-3).setBackgroundColor(Color.BLACK)
                list.get(i-2).setBackgroundColor(Color.BLACK)
                list.get(i-1).setBackgroundColor(Color.BLACK)
            }
        }
    }
    fun swap(arr: IntArray, index1: Int, index2: Int) {
        val temp = arr[index1]
        arr[index1] = arr[index2]
        arr[index2] = temp
    }
    fun readjson(i: Int, name: String): functionality{
        var json: String?=null
        var data: functionality?=null
        try{
            val inptstrm: InputStream=assets.open(name)
            json=inptstrm.bufferedReader().use{it.readText()}
            var jsonArray=JSONArray(json)
                val jsnobj=jsonArray.getJSONObject(i)
                val question: String = Html.fromHtml(jsnobj.getString("question")).toString();
                val incorrectAnswer = jsnobj.getJSONArray("incorrect_answers")
                val correctAnswer = jsnobj.getString("correct_answer")
                val arrayList = ArrayList<String>()
                for (i in 0 until incorrectAnswer.length()){
                    val element=incorrectAnswer.getString(i)
                    arrayList.add(element)
                }
                val que = functionality( question, arrayList, correctAnswer)
               data=que

        }
        catch (e: IOException){

        }
        return data!!
    }

}