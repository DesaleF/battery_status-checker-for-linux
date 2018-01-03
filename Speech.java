package battery_level_notify;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/**
 *
 * @author backrest
 */
public class Speech implements Runnable{
    public static final String VOICE="kevin16";
    @Override
    public void run() {
        Voice voice =VoiceManager.getInstance().getVoice(VOICE);
        voice.allocate();
        try{
            voice.setVolume(100);
            voice.speak("the battery is going to die\n please save everything you did");
        }catch(Exception e){}
    }
    
}
