package com.example.brickgame

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameView : AppCompatActivity() {

    private lateinit var scoreTotal: TextView
    private lateinit var paddle: View
    public lateinit var ball: View
    private lateinit var brickContainer: LinearLayout

    private var ballX = 0f
    private var ballY = 0f
    private var ballSpeedX = 0f

    private var ballSpeedY = 0f

    private var paddleX = 0f

    private var score = 0


    private val brickRow = 10

    private val brickColumn = 6
    private val brickWidth = 120
    private val brickHeight = 30
    private val brickMargin = 1



    private var life = 3



//    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_view)

        scoreTotal = findViewById(R.id.total)
        paddle = findViewById(R.id.paddle)
        ball = findViewById(R.id.ball)
        brickContainer = findViewById(R.id.bricks)


        val play = findViewById<Button>(R.id.play)


        play.setOnClickListener {
            initialiseBricks()
            start()

            play.visibility = View.INVISIBLE


        }

    }

    private fun initialiseBricks() {
        val brickWidthAndMargin = (brickWidth + brickMargin).toInt()

        for (row in 0 until brickRow) {
            val rowLayout = LinearLayout(this)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            rowLayout.layoutParams = params

            for (col in 0 until brickColumn) {
                val brick = View(this)
                val brickParams = LinearLayout.LayoutParams(brickWidth, brickHeight)
                brickParams.setMargins(brickMargin, brickMargin, brickMargin, brickMargin)
                brick.layoutParams = brickParams
                brick.setBackgroundColor(Color.RED)
                rowLayout.addView(brick)

            }

            brickContainer.addView(rowLayout)
        }
    }

    private fun ballMove() {
        ballX += ballSpeedX
        ballY += ballSpeedY

        ball.x = ballX
        ball.y = ballY
    }

    private fun paddleMove(x: Float) {
        paddleX = x - paddle.width / 2
        paddle.x = paddleX
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun checkColl() {

        val screenWidth = resources.displayMetrics.widthPixels.toFloat()
        val screenHeight = resources.displayMetrics.heightPixels.toFloat()

        if (ballX <= 0 || ballX + ball.width >= screenWidth) {
            ballSpeedX *= -1
        }

        if (ballY <= 0) {
            ballSpeedY *= -1
        }

        // hits with paddle
        if (ballY + ball.height >= paddle.y && ballY + ball.height <= paddle.y + paddle.height
            && ballX + ball.width >= paddle.x && ballX <= paddle.x + paddle.width
        ) {
            ballSpeedY *= -1
            score++
            scoreTotal.text = "Score: $score"
        }

        //  paddle misses the ball
        if (ballY + ball.height >= screenHeight) {

            resetBallPos()
        }


        for (row in 0 until brickRow) {
            val rowLayout = brickContainer.getChildAt(row) as LinearLayout

            val rowTop = rowLayout.y + brickContainer.y
            val rowBottom = rowTop + rowLayout.height

            for (col in 0 until brickColumn) {
                val brick = rowLayout.getChildAt(col) as View

                if (brick.visibility == View.VISIBLE) {
                    val brickLeft = brick.x + rowLayout.x
                    val brickRight = brickLeft + brick.width
                    val brickTop = brick.y + rowTop
                    val brickBottom = brickTop + brick.height

                    if (ballX + ball.width >= brickLeft && ballX <= brickRight
                        && ballY + ball.height >= brickTop && ballY <= brickBottom
                    ) {
                        brick.visibility = View.INVISIBLE
                        ballSpeedY *= -1
                        score++
                        scoreTotal.text = "Score: $score"
                        return
                    }
                }
            }
        }


        if (ballY + ball.height >= screenHeight - 100) {
            // Reduce the number of life
            life--

            if (life > 0 ) {
                Toast.makeText(this, "$life balls left ", Toast.LENGTH_SHORT).show()
            }


            paddle.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        paddleMove(event.rawX)
                    }
                }
                true
            }

            if (life <= 0) {

                gameIsOver()
            } else {

                resetBallPos()
                start()

            }
        }

    }

    private fun resetBallPos() {

        val displayMetrics = resources.displayMetrics
        val screenDensity = displayMetrics.density

        val screenWidth = displayMetrics.widthPixels.toFloat()
        val screenHeight = displayMetrics.heightPixels.toFloat()

        ballX = screenWidth / 2 - ball.width / 2
        ballY = screenHeight / 2 - ball.height / 2 +525

        ball.x = ballX
        ball.y = ballY

        ballSpeedX = 0 * screenDensity
        ballSpeedY = 0 * screenDensity


        paddleX = screenWidth / 2 - paddle.width / 2
        paddle.x = paddleX

    }

    private fun gameIsOver() {

        scoreTotal.text = "The Game is Over"

        val play = findViewById<Button>(R.id.play)

        play.visibility = View.VISIBLE



        val intent = Intent(this, MainActivityEnd::class.java)
        intent.putExtra("Score", score)
        startActivity(intent)
    }




    @SuppressLint("ClickableViewAccessibility")
    private fun paddleMovement() {

        paddle.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    paddleMove(event.rawX)

                }
            }
            true

        }
    }


    private fun start() {
        paddleMovement()
        val displayMetrics = resources.displayMetrics
        val screenDensity = displayMetrics.density

        val screenWidth = displayMetrics.widthPixels.toFloat()
        val screenHeight = displayMetrics.heightPixels.toFloat()

        paddleX = screenWidth / 2 - paddle.width / 2
        paddle.x = paddleX

        ballX = screenWidth / 2 - ball.width / 2
        ballY = screenHeight / 2 - ball.height / 2

        val brickHeightAndMargin = (brickHeight
                + brickMargin * screenDensity).toInt()

        ballSpeedX = 5 * screenDensity
        ballSpeedY = -5 * screenDensity

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = Long.MAX_VALUE
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            ballMove()
            checkColl()
        }
        animator.start()
    }

}

