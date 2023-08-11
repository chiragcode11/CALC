package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View)
    {
        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                    workingsTV?.append(view.text)

                canAddDecimal = false
            }
            else
                workingsTV?.append(view.text)

            canAddOperation = true
        }
    }

    fun operationAction(view: View)
    {
        if(view is Button && canAddOperation)
        {
            workingsTV?.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View)
    {
        workingsTV?.text = ""
    }

    fun backSpaceAction(view: View)
    {
        val length = workingsTV?.length()
        if (length != null) {
            if(length > 0)
                workingsTV?.text = workingsTV?.text?.subSequence(0, length - 1)
        }
    }

    fun equalsAction(view: View)
    {
        workingsTV?.text = calculateResults()
    }

    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in workingsTV.text)
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

}




//package com.example.calculator
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.View
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import org.w3c.dom.Text
//
//class MainActivity : AppCompatActivity() {
//
//    private var tvInput:TextView? = null
//    var lastnumeric : Boolean = false
//    var point : Boolean = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        tvInput = findViewById(R.id.tvInput)
//    }
//
//    fun allclear(view: View){
//        tvInput?.text=" "
//        lastnumeric=true
//    }
//
//    fun ondigit(view: View){
//        if(tvInput?.text == "0"){
//            tvInput?.text=" "
//        }
//        tvInput?.append((view as Button).text)
//        lastnumeric = true
//
//
//    }
//
//    fun backclear(view: View){
//        var length = tvInput?.length()
//        if (length != null) {
//            if(tvInput?.text == "0"){
//                tvInput?.text=tvInput?.text
//            }
//            else if(length > 0)
//                tvInput?.text = tvInput?.text?.subSequence(0,length-1 )
//        }
//    }
//
//
//    fun decimalpoint(view: View){
//        if(lastnumeric && !point){
//            point = true
//            tvInput?.append(".")
//            lastnumeric = false
//        }
//    }
//
//    fun operator(view: View){
//        tvInput?.text?.let{
//            if(lastnumeric && !operations(it.toString())){
//               tvInput?.append((view as Button).text)
//                lastnumeric = false
//                point = false
//            }
//        }
//    }
//
//    fun onEqual(view: View){
//        if(lastnumeric){
//            var tvvalue = tvInput?.text.toString()
//            var prefix = ""
//            try{
//                for(character in tvvalue) {
//                    if (tvvalue.contains("/")) {
//                        val splitvalue = tvvalue.split("/")
//                        var one = splitvalue[0]
//                        var two = splitvalue[1]
//                        if (prefix.isNotEmpty()) {
//                            one = prefix + one
//                        }
//                        tvInput?.text = rmzero((one.toDouble() / two.toDouble()).toString())
//                    } else if (tvvalue.contains("*")) {
//                        val splitvalue = tvvalue.split("*")
//                        var one = splitvalue[0]
//                        var two = splitvalue[1]
//                        if (prefix.isNotEmpty()) {
//                            one = prefix + one
//                        }
//                        tvInput?.text = rmzero((one.toDouble() * two.toDouble()).toString())
//                    } else if (tvvalue.contains("+")) {
//                        val splitvalue = tvvalue.split("+")
//                        var one = splitvalue[0]
//                        var two = splitvalue[1]
//                        if (prefix.isNotEmpty()) {
//                            one = prefix + one
//                        }
//                        tvInput?.text = rmzero((one.toDouble() + two.toDouble()).toString())
//                    } else if (tvvalue.startsWith("-")) {
//                        prefix = "-"
//                        tvvalue = tvvalue.substring(1)
//                    } else if (tvvalue.contains("-")) {
//                        val splitvalue = tvvalue.split("-")
//                        var one = splitvalue[0]
//                        var two = splitvalue[1]
//                        if (prefix.isNotEmpty()) {
//                            one = prefix + one
//                        }
//                        tvInput?.text = rmzero((one.toDouble() - two.toDouble()).toString())
//                    }
//                }
//
//            }catch(e:ArithmeticException){
//                e.printStackTrace()
//            }
//        }
//
//    }
//
//    private fun rmzero (result : String) : String{
//        var value = result
//        if (result.contains(".0")){
//            value = result.substring(0,result.length - 2 )
//        }
//        return value
//    }
//
//    private fun operations(value : String) : Boolean {
//        return if (value.startsWith("-")) {
//            false
//        } else {
//            value.contains("/") || value.contains("*") || value.contains("+") || value.contains("-")
//        }
//    }
//
//
//}