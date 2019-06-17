package view;

import common.MyPane;
import common.Util;
import common.Var;
import transcoder.RTranscoder;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainView extends JFrame{
    private JPanel pTop, pCenter, pBottom;
    private JPanel pOri, pTar;
    private JLabel lOri, lTar, lChooseType, lFrom, lTo;
    private JTextField tOri, tTar;
    private JButton bOri, bTar, bConvert, bCancel, bClear, bInterrupt;
    private JCheckBox cCover, cCopy;///是否覆盖，其余文件是否复制
    private JComboBox<String> cbType, cbFrom, cbTo;
    private MyPane taChosen;
    private JProgressBar bar;//进度条

    private boolean doCover = false, doCopy = true;
    private boolean isAll = true;//是否全部转码
    private boolean converting = false;//正在转换
    private String[] totalTypes = {"全部", "java", "c", "txt", "py", "html", "jsp", "xml", "css", "js"};//候选类型
    private String[] charsets = {"GB2312", "GBK", "GB18030", "Unicode", "UTF-8", "UTF-16"};//字符集
    private ArrayList<String> selectedTypes = new ArrayList<>();//选中类型
//    private Thread t_convert;//转码线程  -  不知道为什么停止不了线程，只能用标记法了

    public MainView(){
        super("文件批量转码器");
        setSize(600, 450);

        pTop = Util.createTitledPanel("选择目录", 100, 140, "2-1");
        pCenter = Util.createTitledPanel("转码选项", 100, 220, "null");
        pBottom = Util.createTitledPanel("操作及进度", 100, 120, "null");

        pOri = Util.createPanel(100, 100, "null");
        pTar = Util.createPanel(100, 100, "null");
        lOri = Util.createLabel("源文件目录", 5, 5, 70, 20);
        tOri = Util.createField(5, 27, 500, 28);
        bOri = Util.createButton("浏览", 510, 27, 70, 27);
        lTar = Util.createLabel("目标路径", 5, 5, 70, 20);
        tTar = Util.createField(5, 27, 500, 28);
        bTar = Util.createButton("浏览", 510, 27, 70, 27);

        cCover = Util.createCheckBox("覆盖源目录", false, 5, 20, 90, 20);
        cCopy = Util.createCheckBox("复制其余类型文件", true, 95, 20, 150, 20);
        lChooseType = Util.createLabel("添加 / 移除要转码的文件类型", 310, 20, 180, 20);
        cbType = Util.createComboBox(480, 20, 100, 20);
        taChosen = Util.createArea(5, 50, 470, 50);
        bClear = Util.createButton("清空", 475, 73, 60, 25);
        lFrom = Util.createLabel("从", 116, 115, 20, 20);
        lTo = Util.createLabel("转 换 到", 282, 115, 50, 20);
        cbFrom = Util.createComboBox(140, 110, 130, 30);
        cbTo = Util.createComboBox(340, 110, 130, 30);

        bConvert = Util.createButton("开始转码", 130, 80, 90, 30);
        bInterrupt = Util.createButton("中断", 250, 80, 90, 30);
        bCancel = Util.createButton("取消", 370, 80, 90, 30);
        bar = Util.createBar(10, 30, 570, 30);

        pBottom.add(bInterrupt);
        pBottom.add(bar);
        pBottom.add(bCancel);
        pBottom.add(bConvert);

        pCenter.add(cbTo);
        pCenter.add(cbFrom);
        pCenter.add(lFrom);
        pCenter.add(lTo);
        pCenter.add(taChosen);
        pCenter.add(cbType);
        pCenter.add(lChooseType);
        pCenter.add(cCopy);
        pCenter.add(cCover);
        pCenter.add(bClear);

        pTar.add(bTar);
        pTar.add(tTar);
        pTar.add(lTar);
        pOri.add(bOri);
        pOri.add(tOri);
        pOri.add(lOri);
        pTop.add(pOri);
        pTop.add(pTar);

        this.add(pTop, BorderLayout.NORTH);
        this.add(pCenter, BorderLayout.CENTER);
        this.add(pBottom, BorderLayout.SOUTH);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        addListener();
        addHandler();

        setVisible(true);
    }

    private void init(){
        for(int i = 0; i < totalTypes.length; i++){
            cbType.addItem(totalTypes[i]);
        }
        for(int i = 0; i < charsets.length; i++){
            cbFrom.addItem(charsets[i]);
            cbTo.addItem(charsets[i]);
        }
        taChosen.ta.setText("全部");
    }

    private void setHandler(JComponent co, String type){
        co.setTransferHandler(new TransferHandler(){
            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    if(!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
                        return false;
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
                    List list = (List) o;//文件列表
                    if(type.equals("ori"))
                       tOri.setText(list.get(0).toString());//只取第一个文件
                    else if(type.equals("tar") && !doCover)
                        tTar.setText(list.get(0).toString());//只取第一个文件

                    return true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                for (int i = 0; i < flavors.length; i++) {
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void addHandler(){
        setHandler(pOri, "ori");
        setHandler(pTar, "tar");
        setHandler(tOri, "ori");
        setHandler(tTar, "tar");
    }

    private void addListener(){
        bOri.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = selectFile();
                if(file != null){
                    tOri.setText(file.getPath());
                }
            }
        });
        bTar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = selectFile();
                if(file != null){
                    tTar.setText(file.getPath());
                }
            }
        });
        cbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOrRemove((String)cbType.getSelectedItem());
            }
        });
        cCover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cCover.isSelected()){
                    doCover = true;
                    lTar.setEnabled(false);
                    tTar.setEnabled(false);
                    bTar.setEnabled(false);
                }else{
                    doCover = false;
                    lTar.setEnabled(true);
                    tTar.setEnabled(true);
                    bTar.setEnabled(true);
                }
            }
        });
        cCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cCopy.isSelected()){
                    doCopy = true;
                }else{
                    doCopy = false;
                }
            }
        });
        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(converting){
                    int option = JOptionPane.showConfirmDialog(null, "转码正在进行，确定要中断（不可逆）吗？");
                    if(option == JOptionPane.YES_OPTION){
                        System.exit(0);
                    }else{
                        return;
                    }
                }else
                   System.exit(0);
            }
        });
        bConvert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convert();
            }
        });
        bClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taChosen.ta.setText("");
                selectedTypes.clear();
                isAll = false;
            }
        });
        bInterrupt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(converting){
                    int option = JOptionPane.showConfirmDialog(null, "中断后转码不可逆，请确认！");
                    if(option == JOptionPane.YES_OPTION){
                        Var.interrupt = true;//中断
                        converting = false;
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "没有进行中的任务！");
                }
            }
        });
    }

    //转换
    private void convert(){
        String origin = tOri.getText();
        String targetDir = tTar.getText();
        if(!new File(origin).exists()){
            JOptionPane.showMessageDialog(null, "源文件目录不存在！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!new File(targetDir).exists() && !doCover){
            JOptionPane.showMessageDialog(null, "目标路径不存在！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String target = new File(targetDir).getPath()+ "\\" + new File(origin).getName();
        //开始转换
        new Thread(){
            @Override
            public void run() {
                bConvert.setEnabled(false);
                bar.setValue(0);
                converting = true;
                if(doCover){
                    new RTranscoder(origin, isAll?null:Util.toArray(selectedTypes), bar)
                            .convert((String)cbFrom.getSelectedItem(),
                                    (String)cbTo.getSelectedItem());
                }else{
                    new RTranscoder(origin, target, isAll?null:Util.toArray(selectedTypes), bar)
                            .convert((String)cbFrom.getSelectedItem(),
                                    (String)cbTo.getSelectedItem());
                }
                bConvert.setEnabled(true);
                converting = false;
                if(!Var.interrupt)
                    JOptionPane.showMessageDialog(null, "转码完成！", "成功", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "转码中断！", "中断", JOptionPane.INFORMATION_MESSAGE);
                Var.interrupt = false;
            }
        }.start();
    }

    private void addOrRemove(String type){
        if(type.equals("全部")){
            isAll = true;
            selectedTypes.clear();
            taChosen.ta.setText("全部");
            return;
        }
        isAll = false;
        if(!selectedTypes.contains(type)){
            selectedTypes.add(type);
        }else{
            selectedTypes.remove(type);
        }
        taChosen.ta.setText(" ");
        for(int i = 0; i < selectedTypes.size(); i++){
            taChosen.ta.append(selectedTypes.get(i)+"  ");
        }
    }

    private File selectFile(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = chooser.showOpenDialog(this);
        if(option == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFile();
        }else{
            return null;
        }
    }

}
