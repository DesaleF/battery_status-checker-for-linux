package battery_level_notify;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import javax.swing.JOptionPane;
/**
 *
 * @author backrest
 */
public class Reader_Thread implements Runnable{
    public static Process process=null; 
    public BufferedWriter writer=null;
    public Reader_Thread(Process process,BufferedWriter writer){
        this.process= process;
        this.writer=writer;
    }
    static int percentage;
    @Override
    public void run() {
        try{
            
            InputStream input = process.getInputStream();
            InputStream errinput = process.getErrorStream();
            Scanner reader = new Scanner(input);
            Scanner errreader = new Scanner(errinput);
            String percent = null;
            String cp = JOptionPane.showInputDialog(null, "enter critical battery percent", "Input", 1);
            percentage = Integer.parseInt(cp);
            boolean looper=true;
            while(looper){
                while(reader.hasNext()){
                    percent = reader.nextLine();
                    if(percent.contains("percentage")){
                        String temp = percent.trim();
                        String[] temp_array = temp.split(":");
                        temp_array = temp_array[1].split("%");
                        int percent_num = Integer.parseInt(temp_array[0].trim());
                        if(percent_num < percentage){
                            new Thread(new Speech()).start();
                            JOptionPane.showMessageDialog(null, "the battery is going to die\n please save everything you did");
                            looper=false;
                            writer.close();
                            reader.close();
                            break;
                        }else{
                            
                            OutputStream out = process.getOutputStream();
                            writer = new BufferedWriter(new OutputStreamWriter(out));
                            writer.write("upower -i /org/freedesktop/UPower/devices/battery_BAT0|grep -E \"time|to\\ empty|percentage|capacity\"");
                            writer.flush();
                            writer = new BufferedWriter(new OutputStreamWriter(out));
                            writer.write("exit\n");
                            writer.flush();
                        }
                    }
                }
                while(errreader.hasNext()){
                    String error=errreader.nextLine();
                    JOptionPane.showMessageDialog(null, "error occured detail is:\n"+error);
                }
                Thread.sleep(1000);
            }
        }catch(NumberFormatException |InterruptedException | HeadlessException | IOException er){
        }
    }    
}
