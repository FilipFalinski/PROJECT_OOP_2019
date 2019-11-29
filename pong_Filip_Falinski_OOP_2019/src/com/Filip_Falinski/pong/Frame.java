package com.Filip_Falinski.pong;


import net.java.dev.designgridlayout.*;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;


public class Frame extends JFrame {

    private Game game;

    //frame buttons/slider
    private JButton newGameButton;
    private JButton quitButton;
    private JSlider gameSpeedSlider;

    //labels
    private Hashtable<Integer,JLabel> sliderLabel;

    //listeners
    private ButtonListener gameButtonListener;
    private SliderListener gameSpeedSliderListener;

    public Frame() {

        super("Pong");
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        DesignGridLayout gamePanel = new DesignGridLayout(panel);

        this.game = new Game(700,600);
        gamePanel.row().grid().add(game);

        //slider to control speed of Pong game
        gameSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
        gameSpeedSliderListener = new SliderListener();
        gameSpeedSlider.addChangeListener(gameSpeedSliderListener);

        //turn on labels at major tick marks.
        gameSpeedSlider.setMajorTickSpacing(100);
        gameSpeedSlider.setMinorTickSpacing(10);
        gameSpeedSlider.setPaintTicks(true);
        gameSpeedSlider.setPaintLabels(true);

        //create the label table
        sliderLabel = new Hashtable<Integer,JLabel>();
        sliderLabel.put(0, new JLabel("Slow") );
        sliderLabel.put(100, new JLabel("Game Speed") );
        sliderLabel.put(200, new JLabel("Fast") );
        gameSpeedSlider.setLabelTable( sliderLabel );
        gamePanel.row().grid().add(gameSpeedSlider);

        //new game and quit buttons/listeners
        gameButtonListener = new ButtonListener();
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(gameButtonListener);
        quitButton = new JButton("Quit");
        quitButton.addActionListener(gameButtonListener);
        gamePanel.row().grid().add(newGameButton, quitButton);

        this.add(panel);
        this.pack();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public void startGame()
    {
        this.game.startGame();
    }

    private class SliderListener implements ChangeListener {

        public void stateChanged(ChangeEvent e)
        {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting())
            {
                double speed = source.getValue();
                speed = 10*speed/100;
                game.setSpeed(speed);
            }
        }
    }

    private class ButtonListener implements ActionListener {
        ButtonListener()
        {
        }

        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand().equals("New Game"))
            {
                game.newGame();
            }
            if (e.getActionCommand().equals("Quit"))
            {
                System.exit(0);
            }
        }
    }
}
