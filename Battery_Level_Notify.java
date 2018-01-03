package battery_level_notify;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 *
 * @author backrest
 */
public class Battery_Level_Notify{

    public static Process process=null;
    public static BufferedWriter writer=null;
    public static void main(String[] args) {
        
        ProcessBuilder pb = new ProcessBuilder("/bin/bash");
        String command = "upower -i /org/freedesktop/UPower/devices/battery_BAT0|grep -E \"time|to\\ empty|percentage|capacity\"";
        try{
            pb.redirectErrorStream(true);
            process=pb.start(); 
            OutputStream out = process.getOutputStream();
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(command);
            writer.flush();
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write("exit\n");
            writer.flush();
        }catch(Exception ex){
        }
        Reader_Thread rt=new Reader_Thread(process,writer);
        Thread subThread=new Thread(rt);
        subThread.start();
    }
    
}
