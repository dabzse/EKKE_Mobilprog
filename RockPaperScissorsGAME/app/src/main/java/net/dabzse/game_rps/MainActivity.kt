package net.dabzse.game_rps


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import net.dabzse.game_rps.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding
    private lateinit var timer : CountDownTimer
    private var isPaused = false
    private var remainingTime: Long = 5000
    private var computerChoice: String = ""
    private var playerScore = 0.0
    private var computerScore = 0.0
    private var firstRound = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPause.setOnClickListener()
        {
            pauseGame()
        }

        binding.btnRock.setOnClickListener()
        {
            if (!isPaused) rockPressed()
        }

        binding.btnPaper.setOnClickListener()
        {
            if (!isPaused) paperPressed()
        }

        binding.btnScissors.setOnClickListener()
        {
            if (!isPaused) scissorsPressed()
        }

        startNewRound()
    }

    private fun startNewRound()
    {
        computerChoice = getRandomChoice()
        remainingTime = 5000
        if (!firstRound) { startTimer() }
        else { firstRound = false }
    }

    private fun startTimer()
    {
        timer = object : CountDownTimer(remainingTime, 100)
        {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long)
            {
                remainingTime = millisUntilFinished
                binding.timeLeft.text = String.format("%.3f", millisUntilFinished / 1000.0)
            }

            override fun onFinish()
            {
                computerWins()
            }
        }.start()
    }

    private fun getRandomChoice(): String
    {
        return listOf("rock", "paper", "scissors").random()
    }

    private fun rockPressed() { playerChose("rock") }

    private fun paperPressed() { playerChose("paper") }

    private fun scissorsPressed() { playerChose("scissors") }

    private fun playerChose(playerChoice: String)
    {
        if (isPaused) return

        try
        {
            timer.cancel()
        }
        catch (e: Exception)
        {
            Log.e("MainActivity", "Error canceling timer", e)
            // e.printStackTrace()
        }

        showChoices(playerChoice, computerChoice)
        determineWinner(playerChoice, computerChoice)
        checkGameOver()
        startNewRound()
    }

    private fun showChoices(playerChoice: String, computerChoice: String)
    {
        val playerImageView: ImageView = binding.playerChose
        val computerImageView: ImageView = binding.computerChose

        playerImageView.setImageResource(getImageResource(playerChoice))
        computerImageView.setImageResource(getImageResource(computerChoice))
    }

    private fun getImageResource(choice: String): Int
    {
        return when (choice)
        {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            else -> R.drawable.scissors
        }
    }

    private fun determineWinner(playerChoice: String, computerChoice: String)
    {
        when
        {
            playerChoice == computerChoice ->
            {
                playerScore += 0.5
                computerScore += 0.5
            }

            (playerChoice == "rock" && computerChoice == "scissors") ||
            (playerChoice == "paper" && computerChoice == "rock") ||
            (playerChoice == "scissors" && computerChoice == "paper") ->
            {
                playerScore += 1
            }

            else ->
            {
                computerScore += 1
            }
        }
        updateScores()
    }

    private fun updateScores()
    {
        binding.playerScore.text = playerScore.toString().toEditable()
        binding.computerScore.text = computerScore.toString().toEditable()
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun computerWins()
    {
        computerScore += 1
        updateScores()
        checkGameOver()
        startNewRound()
    }

    private fun checkGameOver()
    {
        if ((playerScore >= 10 && playerScore >= computerScore + 2) ||
            (computerScore >= 10 && computerScore >= playerScore + 2))
        {

            playerScore = 0.0
            computerScore = 0.0
            updateScores()
        }
    }

    private fun pauseGame()
    {
        isPaused = true

        try
        {
            timer.cancel()
        }
        catch (e: Exception) {
            Log.e("MainActivity", "Error canceling timer", e)
            // e.printStackTrace()
        }

        val intent = Intent(this, Pause::class.java)
        startActivity(intent)
    }

    override fun onResume()
    {
        super.onResume()
        if (isPaused)
        {
            isPaused = false
            if (!firstRound && (playerScore != 0.0 || computerScore != 0.0))
            {
                startTimer()
            }
        }
    }
}

