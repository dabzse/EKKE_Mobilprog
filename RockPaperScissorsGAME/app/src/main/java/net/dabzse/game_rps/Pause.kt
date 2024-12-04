package net.dabzse.game_rps

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Pause : AppCompatActivity()
{

    lateinit var btn_play: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pause)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))
        {
                v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btn_play = findViewById(R.id.btn_play)
        btn_play.setOnClickListener()
        {
            continue_game()
        }
    }

    private fun continue_game()
    {
        finish()
    }
}

