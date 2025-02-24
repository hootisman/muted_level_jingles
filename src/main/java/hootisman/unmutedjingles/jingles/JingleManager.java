package hootisman.unmutedjingles.jingles;

import hootisman.unmutedjingles.UnmutedJinglesConfig;
import hootisman.unmutedjingles.UnmutedJinglesPlugin;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class JingleManager {

    private Client client;

    private ClientThread clientThread;

    private UnmutedJinglesConfig config;

    private int jingleTick;

    private boolean isJingleQueued;

    public JingleManager(Client cli, ClientThread clithrd, UnmutedJinglesConfig cnfg){
        client = cli;
        clientThread = clithrd;
        config = cnfg;
        jingleTick = 0;
        isJingleQueued = false;
    }

    public void startJingle(){
        if (client.getMusicVolume() != 0) return;

        if (!isWindowClosed()){
            //there is an open window
            if (!isJingleQueued) isJingleQueued = true;
            return;
        }

        log.info("*j* unmuting for jingle...");
        //todo: 2) test invokelater (also in endjingle)
        clientThread.invoke(() -> {
            client.setMusicVolume(config.jingleVolume());
            jingleTick = 1;
            isJingleQueued = false;
        });
    }

    public void endJingle(){
        log.info("*j* jingle ended, muting...");
        //todo: 1) test invoke
        client.setMusicVolume(0);
        jingleTick = 0;
    }

    public void tickJingle(){
        if(isJingleQueued) startJingle();

        if(jingleTick > 10){
            endJingle();
        }else if (jingleTick != 0){
            jingleTick += 1;
            log.info("*G* " + jingleTick);
        }
    }
    //if widget S161.16 has 1 or more children, then return true
    public boolean isWindowClosed() {
        return client.getWidget(161, 16).getNestedChildren().length == 0;
    }

}
