package common;

import javax.swing.*;
import java.awt.*;

public class MyPane extends JPanel{
    public JTextArea ta = new JTextArea();
    private JScrollPane pane = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    public MyPane(int x, int y, int width, int height){
        ta.setFont(new Font("微软雅黑", 0, 16));
        ta.setBackground(new Color(227, 227, 227));
        ta.setEditable(false);
        ta.setLineWrap(false);
        this.setBounds(x, y, width, height);
        this.setLayout(new BorderLayout());
        this.add(pane);
    }
}
