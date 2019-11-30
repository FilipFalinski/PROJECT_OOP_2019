package com.Filip_Falinski.pong;

import java.awt.*;

public class Ball {

    private double diameter;
    private double x;
    private double y;

    private int timesHit;

    private double angle;
    private double speed;
    private double dx;
    private double dy;

    private int boardHeight;
    private int boardWidth;
    private int paddleHeight;
    private int paddleWidth;

    Ball(int boardWidth, int boardHeight, int paddleWidth, int paddleHeight, int diameter, double speed) {

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.paddleHeight = paddleHeight;
        this.paddleWidth = paddleWidth;
        this.resetLocation();
        this.diameter = diameter;
        this.speed = speed;

        this.randomizeDyDx();
    }

    public void resetLocation() {

        this.x = (boardWidth - diameter)/2;
        this.y = (boardHeight - diameter)/2;
        this.randomizeDyDx();
    }

    public void updateLocation(double playerOneX, double playerOneY, double playerTwoX, double playerTwoY, double deltaTime) {

        if (timesHit % 2 == 0)
        {

        }

        // ball will bounce if top of board is reached
        if (y < 0) {

            y = 1;
            dy *= -1;
        }

        //ball will bounce if bottom of board is reached
        if (y > boardHeight - diameter) {

            y = boardHeight - diameter - 1;
            dy *= -1;
        }

        //check if ball is colliding with player1 paddle
        if (dx < 0
                && multisampleTestCollision(playerOneX, playerOneY, playerOneX + paddleWidth, playerOneY + paddleHeight, deltaTime)) {

            timesHit++;
            x = playerOneX + paddleWidth + 1;
            dx *= -1;
        }

        //check if ball is colliding with player2 paddle
        if (dx > 0
                && multisampleTestCollision(playerTwoX, playerTwoY, playerTwoX + paddleWidth, playerTwoY + paddleHeight, deltaTime)) {

            timesHit++;
            x = playerTwoX - diameter - 1;
            dx *= -1;
        }
        x += dx*deltaTime;
        y += dy*deltaTime;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public void setSpeed(double speed) {

        //prevent ball from stopping


        this.speed = (speed < 1) ? 1 : speed;

        //store direction of dx, dy
        boolean dxWasNegative = (this.dx < 0) ? true : false;
        boolean dyWasNegative = (this.dy < 0) ? true : false;

        //calculatenew movement speed


        this.dx = this.speed*(double)Math.cos(Math.toRadians(this.angle));
        this.dy = this.speed*(double)Math.sin(Math.toRadians(this.angle));

        //restore direction of movement before the speed was changed


        if (this.dx > 0 && dxWasNegative)
            dx = -dx;
        if (this.dy > 0 && dyWasNegative)
            dy = -dy;
    }

    public double getSpeed()
    {
        return speed;
    }

    public void randomizeDyDx() {

        double randNumber = (double)Math.random();
        randNumber *= 360;
        this.angle = randNumber;

        //Monte-Carlo style randomization to make sure dy is within reasonable values

        if (-.03 < (double)Math.cos(Math.toRadians(this.angle))
                && (double)Math.cos(Math.toRadians(this.angle)) < 0.3)
            randomizeDyDx();
        else {
            this.dx = this.speed*(double)Math.cos(Math.toRadians(this.angle));
            this.dy = this.speed*(double)Math.sin(Math.toRadians(this.angle));
        }
    }

    public void draw(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillOval((int)x,(int)y,(int)diameter,(int)diameter);
    }

    private boolean testAABBCollision(double ax, double ay, double aX, double aY, double deltaTime) {
        int bx = (int)(this.x + this.dx*deltaTime);
        int bX = (int)(this.x + this.diameter + this.dx*deltaTime);
        int by = (int)(this.y + this.dy*deltaTime);
        int bY = (int)(this.y + this.diameter + this.dy*deltaTime);
        return !(aX < bx || aY < by || bX < ax || bY < ay);
    }

    private boolean multisampleTestCollision(double ax, double ay, double aX, double aY, double deltaTime)
    {
        if (testAABBCollision(ax, ay, aX, aY, deltaTime))
        {
            return true;
        }
        if (this.dx*deltaTime > this.diameter || this.dy*deltaTime > this.diameter)
        {
            int dxSteps = (int)((deltaTime*this.dx)/(this.diameter));
            int dySteps = (int)((deltaTime*this.dy)/(this.diameter));
            int multisampleSteps = Math.max(dxSteps, dySteps);

            for (int currentStep = 0; currentStep < multisampleSteps; ++currentStep)
            {
                double dxStep = dx*currentStep/multisampleSteps;
                double dyStep = dy*currentStep/multisampleSteps;

                if (testAABBCollision(ax, ay, aX, aY, deltaTime))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
