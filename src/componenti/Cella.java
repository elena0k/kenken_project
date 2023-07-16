package componenti;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class Cella extends JPanel {

    private JTextField text = new JTextField();
    private JTextField vincolo;
    private PlainDocument doc;
    private int n;

    public Cella() {
        doc = new PlainDocument();
        filtraText();
        setTextLayoutConfig();
        text.setDocument(doc);
    }

    public void setN(int n) {
        this.n = n;
    }

    public void filtraText() {
        doc.setDocumentFilter(new DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
                if (!text.matches("//d")) {
                    return; // Ignora l'inserimento
                }
                int num = Integer.parseInt(text);
                if (num > 0 && num <= n)
                    super.insertString(fb, offset, text, attrs);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (!text.matches("\\d")) {
                    return; // Ignora la sostituzione
                }
                int num = Integer.parseInt(text);
                if (num > 0 && num <= n)
                    super.replace(fb, offset, length, text, attrs);
            }
        });

        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateInput();
            }

            private void validateInput() {
                String input = text.getText();
                if (input.length() > 1 || (input.length() == 1 && !Character.isDigit(input.charAt(0)))) {
                    SwingUtilities.invokeLater(() -> {
                        text.setText(input.substring(0, Math.min(input.length(), 1)));
                    });
                }
            }
        });
    }

    public void setTextLayoutConfig() {
        setLayout(new BorderLayout());
        text.setHorizontalAlignment(JTextField.CENTER);
        add(text, BorderLayout.CENTER);
    }

    public void setVincolo(int num, String op) {
        vincolo = new JTextField();
        if (op != null)
            vincolo.setText("" + num + op);
        else
            vincolo.setText(num + "");
        vincolo.setEnabled(false);
        add(vincolo, BorderLayout.NORTH);
        repaint();
    }

    public void cleanVincolo() {
        remove(vincolo);
        revalidate();
        repaint();
    }

    public boolean isCellaSemplice() {
        return vincolo == null;
    }


    public void setFont(int n) {
        if (n == 6 || n == 5)
            text.setFont(new Font("Courier New", Font.BOLD, 20));
        else if (n == 3 || n == 4)
            text.setFont(new Font("Courier New", Font.BOLD, 35));
    }


    public JTextField getTextField() {
        return text;
    }

    public void mySetBackground(Color color) {
        text.setBackground(color);
        if (vincolo != null)
            vincolo.setBackground(color);
    }

    public String getText() {
        return text.getText();
    }
    public void cleanText(){text.setText("");}


    public void setEnabled(boolean bool) {
        text.setEnabled(bool);
    }

    public void mySetBorder(Border border) {
        setBorder(border);
    }

    public void setMouseAdapter(MouseAdapter mouseAdapter) {
        text.addMouseListener(mouseAdapter);
    }
}
