package common;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * 工具类
 */
public class Util {
    public static String toRegex(String content){
        content = content.replaceAll("\\\\", "\\\\\\\\");
        content = content.replaceAll("\\.", "\\\\.");
        content = content.replaceAll("\\^", "\\\\^");
        content = content.replaceAll("\\$", "\\\\\\$");//$有特殊含义
        content = content.replaceAll("\\?", "\\\\?");
        content = content.replaceAll("\\+", "\\\\+");
        content = content.replaceAll("\\*", "\\\\*");
        content = content.replaceAll("\\{", "\\\\{");
        content = content.replaceAll("\\}", "\\\\}");
        content = content.replaceAll("\\[", "\\\\[");
        content = content.replaceAll("\\]", "\\\\]");
        content = content.replaceAll("\\(", "\\\\(");
        content = content.replaceAll("\\)", "\\\\)");
        content = content.replaceAll("\\|", "\\\\|");
        return content;
    }
    public static String toReplacement(String content){
        content = content.replaceAll("\\\\", "\\\\\\\\");
        content = content.replaceAll("\\$", "\\\\\\$");//$有特殊含义
        return content;
    }
    /**
     * 得到文件类型
     * 一律小写
     * @param fileName
     * @return
     */
    public static String getFileType(String fileName){
        return fileName == null ? fileName : fileName.substring(fileName.lastIndexOf('.')+1, fileName.length()).toLowerCase();
    }

    public static boolean arrContains(String[] arr, String str){
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(str))
                return true;
        }
        return false;
    }

    public static void fileCopy(File org, File tar){
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(org));
            byte[] buff = new byte[in.available()];
            in.read(buff);

            //两个不能写到一起，一定要先读后写，否则同一个文件会出错
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tar));
            out.write(buff);
            out.flush();

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void switchLayout(JComponent comp, String layout){
        switch (layout){
            case "border":
                comp.setLayout(new BorderLayout());
                break;
            case "flow":
                comp.setLayout(new FlowLayout());
                break;
            case "2-1":
                comp.setLayout(new GridLayout(2, 1));
                break;
            case "null":
                comp.setLayout(null);
                break;
        }
    }

    public static JPanel createTitledPanel(String title, int width, int height, String layout){
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(width, height));
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.lightGray, 2), title));
        switchLayout(p, layout);
        return p;
    }

    public static JPanel createPanel(int width, int height, String layout){
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(width, height));
        switchLayout(p, layout);
        return p;
    }

    public static JLabel createLabel(String text, int x, int y, int width, int height){
        JLabel l = new JLabel(text);
        l.setBounds(x, y, width, height);
        return l;
    }

    public static JTextField createField(int x, int y, int width, int height){
        JTextField tf = new JTextField();
        tf.setBounds(x, y, width, height);
        return tf;
    }

    public static JButton createButton(String text, int x, int y, int width, int height){
        JButton b = new JButton();
        b.setText(text);
        b.setBounds(x, y, width, height);
        b.setBackground(Color.lightGray);
        return b;
    }

    public static JCheckBox createCheckBox(String text, boolean state, int x, int y, int width, int height){
        JCheckBox box = new JCheckBox();
        box.setText(text);
        box.setBounds(x, y, width, height);
        box.setSelected(state);
        return box;
    }

    public static JComboBox<String> createComboBox(int x, int y, int width, int height){
        JComboBox<String> box = new JComboBox<>();
        box.setBounds(x, y, width, height);
        return box;
    }

    public static MyPane createArea(int x, int y, int width, int height){
        return new MyPane(x, y, width, height);
    }

    public static JProgressBar createBar(int x, int y, int width, int height){
        JProgressBar bar = new JProgressBar();
        bar.setBounds(x, y, width, height);
        bar.setMaximum(100);
        return bar;
    }

    public static String[] toArray(ArrayList<String> list){
        String[] arr = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            arr[i] = list.get(i);
        }
        return arr;
    }

}
