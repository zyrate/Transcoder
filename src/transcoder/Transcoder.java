package transcoder;
import java.io.*;
/**
 * 文件转码器
 */
public class Transcoder {
    private String origin;  //源文件 - 转码前
    private String target;  //目标文件 - 转码后
    public Transcoder(String origin, String target){
        this.origin = origin;
        this.target = target;
    }
    public Transcoder(String origin){
        this.origin = origin;
        this.target = origin;//覆盖
    }
    /**
     * 根据指定编码读取源文件中的内容并以字符串形式返回
     * @param charsetName
     * @return
     */
    private String read(String charsetName){
        String content = ""; //根据指定编码读取的内容
        try {
            //这里不用bufferedReader是因为\r的问题
            InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(origin)), charsetName);
            char[] buff = new char[1];//必须+1
            while(reader.read(buff) != -1){
                content += new String(buff);
            }
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return content;
        }
    }
    private void write(String content, String charsetName){
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File(target)), charsetName);
            writer.write(content, 0, content.length());
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 开始转码
     * @param fromCharset 转码之前编码
     * @param toCharset 要转换的编码
     */
    public void convert(String fromCharset, String toCharset){
        String content = read(fromCharset);
        write(content, toCharset);
    }
}
