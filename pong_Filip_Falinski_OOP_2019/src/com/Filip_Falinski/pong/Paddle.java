package com.Filip_Falinski.pong;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Paddle {

    //Paddle location and dimensions
    private double x;
    private double y;
    private int width;
    private int height;

    //Variables to determine movement direction and speed
    private int move;
    private double speed;

    //Board dimension
    private int boardHeight;

    //Keyboard paddle controls (extend KeyAdapter)
    private PaddleControls control;

    Paddle(int x, int boardHeight, int width, int height, int moveUpKey, int moveDownKey, double speed)
    {
        this.width = width;
        this.height = height;
        this.boardHeight = boardHeight;
        this.x = x;
        this.y = (boardHeight - height)/2;

        control = new PaddleControls(moveUpKey, moveDownKey);
        this.move = 0;
        this.speed = speed;
    }

    public void resetLocation()
    {
        this.y = (boardHeight - height)/2;
    }

    public PaddleControls getControls()
    {
        return control;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setSpeed(double speed)
    {
        this.speed = (speed < 1) ? 1 : speed;
    }

    public void updateLocation(double deltaTime)
    {
        if (move > 0)
        {
            y += speed*deltaTime;
        }
        if (move < 0)
        {
            y -= speed*deltaTime;
        }

        //Move player back onto board if Y goes out of bounds
        if (y < 0)
        {
            y = 0;
        }
        if (y > boardHeight - height)
        {
            y = boardHeight - height;
        }
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect((int)x,(int)y,width,height);
    }

    private class PaddleControls extends KeyAdapter
    {
        private int upKey;
        private int downKey;

        PaddleControls(int up, int down)
        {
            this.upKey = up;
            this.downKey = down;
        }

        public void keyReleased(KeyEvent e)
        {
            if (e.getKeyCode() == upKey)
            {
                move = 0;
            }

            if (e.getKeyCode() == downKey)
            {
                move = 0;
            }
        }


        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() == upKey)
            {
                move = -1;
            }

            if (e.getKeyCode() == downKey)
            {
                move = 1;
            }
        }
    }
}
