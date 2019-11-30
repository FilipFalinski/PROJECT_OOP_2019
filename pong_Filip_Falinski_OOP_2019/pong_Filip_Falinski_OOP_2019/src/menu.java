import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menu {
    private JButton START;
    private JPanel menu;


    public menu() {
        START.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }

    public static void main(String[] args) {


        JFrame frame = new JFrame("menu" );
        frame.setContentPane(new menu().menu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);

    }
}
