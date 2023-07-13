package decoratorCelle;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class CellaSemplice extends JPanel implements ComponentCella {

    private JTextField text;

    public CellaSemplice() {
        text=new JTextField();
    }

    public void draw() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public void setFont(int n) {
        if(n==6 || n==5)
            text.setFont(new Font("Courier New", Font.BOLD, 20));
        else if(n==3 || n==4)
            text.setFont(new Font("Courier New", Font.BOLD, 35));
    }


    public JTextField getText()
    {
        return text;
    }






    @Override
    public void impostaTextField() {

        setLayout(new BorderLayout());
        text.setHorizontalAlignment(JTextField.CENTER);
        add(text, BorderLayout.CENTER);
    }
}
