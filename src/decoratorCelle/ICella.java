package decoratorCelle;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.MouseAdapter;

public interface ICella {

    JTextField getText();
    void setTextLayoutConfig();
    void setFont(int n);
    void mySetBorder(Border border);
    void setMouseAdapter(MouseAdapter mouseAdapter) ;

    void setEnabled(boolean bool);


    }
