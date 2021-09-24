package ru.geekbrains.textclient_1423

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.geekbrains.textclient_1423.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val historySource = HistorySource(contentResolver)
        historySource.query()
        binding.insert.setOnClickListener {
            historySource.insert(HistoryEntity(111,"City111",100))
        }

        binding.get.setOnClickListener {
            historySource.getHistory()
        }
        binding.getByPosition.setOnClickListener {
            historySource.getCityByPosition(1)
        }
        binding.update.setOnClickListener {
            historySource.update(HistoryEntity(1, "Гонолулу", 53))
        }
        binding.delete.setOnClickListener {
            historySource.delete(HistoryEntity(2))
        }

    }
}