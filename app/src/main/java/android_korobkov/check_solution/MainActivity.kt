package android_korobkov.check_solution

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android_korobkov.check_solution.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Объявление переменных для элементов пользовательского интерфейса
    lateinit var summary: TextView
    lateinit var correct: TextView
    lateinit var incorrect: TextView
    lateinit var percent: TextView
    lateinit var first: TextView
    lateinit var second: TextView
    lateinit var operation: TextView
    lateinit var answer: TextView
    lateinit var correctBtn: Button
    lateinit var incorrectBtn: Button
    lateinit var start: Button
    lateinit var minText: TextView
    lateinit var maxText: TextView
    lateinit var avgText: TextView

    // Переменные для хранения статистики
    var min = Int.MAX_VALUE
    var max = Int.MIN_VALUE
    var avgSummary = 0
    var correctAns = 0
    var incorrectAns = 0

    // Список операций для генерации примеров
    val operations = listOf("+", "-", "*", "/")

    // Переменные для отслеживания времени
    var seconds = 0
    val totalSeconds = 1000
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация переменных для элементов пользовательского интерфейса с использованием View Binding
        summary = binding.summary
        correct = binding.correct
        incorrect = binding.incorrect
        percent = binding.percent
        first = binding.first
        second = binding.second
        operation = binding.operation
        answer = binding.answer
        minText = binding.minText
        maxText = binding.maxText
        avgText = binding.avgText
        correctBtn = binding.correctBtn
        incorrectBtn = binding.incorrectBtn
        start = binding.start

        // Установка слушателей для кнопок
        correctBtn.setOnClickListener {
            btnCallback()
        }
        incorrectBtn.setOnClickListener {
            btnCallback()
        }
        start.setOnClickListener {
            start()
        }

        // Начальное состояние кнопок
        correctBtn.isEnabled = false
        incorrectBtn.isEnabled = false

        // Инициализация таймера
        timer = object : CountDownTimer(totalSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                seconds++
            }

            override fun onFinish() {}
        }.start()
    }

    // Обработчик нажатия на кнопку "Правильно" и "Неправильно"
    private fun btnCallback() {
        updateTime()
        correctBtn.isEnabled = false
        incorrectBtn.isEnabled = false
        start.isEnabled = true

        val ans = getAns().toInt().toString()

        if (answer.text.toString() != ans) {
            correctAns++
        } else {
            incorrectAns++
        }

        updateUI()
    }

    // Обновление пользовательского интерфейса
    private fun updateUI() {
        correct.text = correctAns.toString()
        incorrect.text = incorrectAns.toString()
        summary.text = (correctAns + incorrectAns).toString()
        percent.text =
            String.format("%.2f", (correctAns.toDouble() / (correctAns + incorrectAns) * 100)) + "%"
    }

    // Инициализация нового примера
    private fun makeNewExpr() {
        first.text = Random.nextInt(10, 100).toString()
        second.text = Random.nextInt(10, 100).toString()
        operation.text = operations[Random.nextInt(0, 4)]

        // Проверка, чтобы деление было целым
        while (operation.text == "/" && first.text.toString().toDouble() % second.text.toString()
                .toDouble() != 0.0
        ) {
            first.text = Random.nextInt(10, 100).toString()
            second.text = Random.nextInt(10, 100).toString()
        }

        // Установка случайного ответа
        if (Random.nextInt(0, 2) == 1) {
            answer.text = getAns().toInt().toString()
        } else {
            answer.text = Random.nextInt(10, 100).toString()
        }
    }

    // Получение ответа на текущий пример
    private fun getAns(): Double {
        when (operation.text) {
            "+" -> {
                return first.text.toString().toDouble() + second.text.toString().toDouble()
            }

            "-" -> {
                return first.text.toString().toDouble() - second.text.toString().toDouble()
            }

            "*" -> {
                return first.text.toString().toDouble() * second.text.toString().toDouble()
            }

            "/" -> {
                return first.text.toString().toDouble() / second.text.toString().toDouble()
            }
        }
        return 0.0
    }

    // Обновление времени и статистики по времени
    private fun updateTime() {
        if (seconds < min) {
            min = seconds
        }
        if (seconds > max) {
            max = seconds
        }
        avgSummary += seconds

        minText.text = min.toString()
        maxText.text = max.toString()
        avgText.text = String.format("%.2f", avgSummary.toDouble() / (correctAns + incorrectAns))
    }

    // Обработка нажатия на кнопку "Старт"
    private fun start() {
        start.isEnabled = false
        correctBtn.isEnabled = true
        incorrectBtn.isEnabled = true
        answer.isEnabled = true
        answer.setBackgroundColor(Color.TRANSPARENT)

        makeNewExpr()

        seconds = 0
    }
}
