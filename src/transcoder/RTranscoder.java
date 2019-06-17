package transcoder;

import common.Util;
import common.Var;

import javax.swing.*;
import java.io.File;

/**
 * 递归转码器 - 起始一般是目录
 * 不在指定类型里的文件直接复制过去
 */

public class RTranscoder {
    private String originDir;
    private String targetDir;
    private String[] types;//要转码的文件类型，null代表全部转码

    private int total;//这个目录里总共有多少文件
    private int current;//已转换文件个数

    private JProgressBar bar;//进度条

    public RTranscoder(String originDir, String targetDir, String[] types, JProgressBar bar){
        this.originDir = originDir;
        this.targetDir = targetDir;
        this.types = types;
        this.bar = bar;
    }
    public RTranscoder(String originDir, String[] types, JProgressBar bar){
        this.originDir = originDir;
        this.targetDir = originDir;//覆盖
        this.types = types;
        this.bar = bar;
    }

    private void todo(File origin, String fromCharset, String toCharset){
        if(Var.interrupt)//中断
            return;

        File target = new File(origin.getPath().replaceAll(Util.toRegex(originDir), Util.toReplacement(targetDir)));
        if(origin.isDirectory()){
            if(!target.exists())
                target.mkdir();

            File[] files = origin.listFiles();
            for(int i = 0; i < files.length; i++){
                todo(files[i], fromCharset, toCharset);
            }
        }else{
            if(types == null || Util.arrContains(types, Util.getFileType(origin.getName())))
                new Transcoder(origin.getPath(), target.getPath()).convert(fromCharset, toCharset);
            else{//如果文件类型不在转换范围，直接复制过去
                Util.fileCopy(origin, target);
            }
            //显示进度
            current++;
            bar.setValue((int)(1.0*current/total*100));
        }
    }

    public void convert(String fromCharset, String toCharset){
        File dir = new File(originDir);
        total = 0;
        current = 0;
        count(dir);//数文件个数
        todo(dir, fromCharset, toCharset);
    }

    private void count(File dir){
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            for(int i = 0; i < files.length; i++){
                count(files[i]);
            }
        }else{
            total++;
        }
    }

}
