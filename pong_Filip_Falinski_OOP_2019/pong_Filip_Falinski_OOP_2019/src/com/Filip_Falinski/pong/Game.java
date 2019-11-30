package com.Filip_Falinski.pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.Math;

public class Game extends JPanel {

    //game state
    private boolean gamePaused;
    private boolean gameCountDown;


    //Variables used for counting down between rounds
    private final double TIME_BETWEEN_ROUNDS = 3;
    private double countDownTime;
    private String countDownString;

    //Target nanoseconds / frame (default 60 frames/sec)
    private final int FRAMES_PER_SECOND = 60;
    private final long TARGET_TIME = 1000000000 / FRAMES_PER_SECOND;

    //Board dimensions
    private int boardWidth;
    private int boardHeight;

    private final double DEFAULT_GAME_SPEED = 10;

    //Player paddle variables
    private int paddleWidth;
    private int paddleHeight;
    private int playerOneX;
    private int playerTwoX;

    //Player paddle controls
    private final int MOVE_PLAYER_ONE_UP = KeyEvent.VK_UP;
    private final int MOVE_PLAYER_ONE_DOWN = KeyEvent.VK_DOWN;
    private final int MOVE_PLAYER_TWO_UP = KeyEvent.VK_W;
    private final int MOVE_PLAYER_TWO_DOWN = KeyEvent.VK_S;

    //Ball variables
    private int ballDiameter;

    //Player score variables
    private int playerOneScore;
    private int playerTwoScore;

    //Game object classes
    private Paddle playerOnePaddle;
    private Paddle playerTwoPaddle;
    private Ball ball;

    Game(int boardWidth, int boardHeight)
    {
        //Initialize board
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.setBackground(Color.BLACK);

        //Determine paddle dimensions
        paddleHeight = boardWidth/6;
        paddleWidth = paddleHeight/5;

        //Determine paddle x locations
        int distFromEdge = boardWidth/10;
        playerOneX = distFromEdge;
        playerTwoX = boardWidth - (distFromEdge + paddleWidth);

        //Initialize paddles
        playerOnePaddle = new Paddle(playerOneX, boardHeight, paddleWidth, paddleHeight, MOVE_PLAYER_ONE_UP, MOVE_PLAYER_ONE_DOWN, DEFAULT_GAME_SPEED);
        playerTwoPaddle = new Paddle(playerTwoX, boardHeight, paddleWidth, paddleHeight, MOVE_PLAYER_TWO_UP, MOVE_PLAYER_TWO_DOWN, DEFAULT_GAME_SPEED);

        //Create ball object with board/paddle/ball diameter information
        ballDiameter = paddleWidth;
        ball = new Ball(boardWidth, boardHeight, paddleWidth, paddleHeight, ballDiameter, DEFAULT_GAME_SPEED);
        ball.randomizeDyDx();

        //Set the current game state
        this.gamePaused = false;
        this.gameCountDown = false;
        this.playerOneScore = 0;
        this.playerTwoScore = 0;

        setFocusable(true);
        requestFocusInWindow();


        //Bind keys to player paddles
        this.addKeyListener(playerOnePaddle.getControls());
        this.addKeyListener(playerTwoPaddle.getControls());
    }


    public Dimension getPreferredSize()
    {
        return new Dimension(boardWidth,boardHeight);
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        ball.draw(g);
        playerOnePaddle.draw(g);
        playerTwoPaddle.draw(g);
        drawScores(g);

        //gameCountDown indicates round has not started yet
        if (gameCountDown == true)
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));
            FontMetrics fm = getFontMetrics( getFont() );
            int width = fm.stringWidth(countDownString);
            g.drawString(countDownString, (boardWidth - width)/2, boardHeight/10);
        }
    }

    private void drawScores(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));
        FontMetrics fm = getFontMetrics( getFont() );
        String playerOneScoreString = Integer.toString(playerOneScore);
        int width = fm.stringWidth(playerOneScoreString);


    }

    public int getPlayerOneScore()
    {
        return playerOneScore;
    }

    public int getPlayerTwoScore()
    {
        return playerTwoScore;
    }

    public void resetBoard()
    {
        playerOnePaddle.resetLocation();
        playerTwoPaddle.resetLocation();
        ball.resetLocation();
    }

    public void newRound()
    {
        resetBoard();
        gameCountDown = true;
        countDownTime = TIME_BETWEEN_ROUNDS;
    }

    public void newGame()
    {
        playerOneScore = 0;
        playerTwoScore = 0;
        newRound();
    }

    public void startGame()
    {
        gamePaused = false;
        gameCountDown = true;
        countDownTime = TIME_BETWEEN_ROUNDS;
        gameLoop();
    }

    public void setSpeed(double speed)
    {
        ball.setSpeed(speed);
        playerOnePaddle.setSpeed(speed);
        playerTwoPaddle.setSpeed(speed);
    }

    private void gameLoop()
    {
        //Time since last gameLoop update
        long lastUpdateTime = System.nanoTime();

        //Used to calculate frame updates
        double deltaTime = 0;

        while (true)
        {
            requestFocusInWindow();
            long currentTime = System.nanoTime();
            long timeToUpdate = currentTime - lastUpdateTime;
            lastUpdateTime = System.nanoTime();
            deltaTime = (double)timeToUpdate/TARGET_TIME;

            //Update and repaint all objects for this frame
            this.update(deltaTime);
            this.repaint(0,0,boardWidth,boardHeight);

            //Thread timing information
            try
            {
                Thread.sleep((lastUpdateTime - System.nanoTime() + TARGET_TIME)/1000000);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void update(double deltaTime)
    {
        if (!gameCountDown)
        {
            //Update all game object locations
            playerOnePaddle.updateLocation(deltaTime);
            playerTwoPaddle.updateLocation(deltaTime);
            ball.updateLocation(playerOnePaddle.getX(), playerOnePaddle.getY(), playerTwoPaddle.getX(), playerTwoPaddle.getY(), deltaTime);

            //Start a new round and increment score if necessary
            testIfPlayerScored();
        }
        else
        {
            //Continue counting down time before match begins
            displayCountDown(deltaTime);
        }
    }

    private void displayCountDown(double deltaTime)
    {
        countDownTime -= deltaTime*TARGET_TIME/1000000000;
        if (countDownTime > 0)
        {
            countDownString = "Match begins in ";
            countDownString += Integer.toString((int)Math.ceil(countDownTime));
        }
        else
        {
            countDownString = "";
            gameCountDown = false;
        }
    }

    private void testIfPlayerScored()
    {
        if (ball.getX() > boardWidth - ballDiameter)
        {
            playerOneScore++;
            newRound();
        }

        if (ball.getX() < 0)
        {
            playerTwoScore++;
            newRound();
        }
    }

}
