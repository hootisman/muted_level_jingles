package hootisman.unmutedjingles.jingles;

import hootisman.unmutedjingles.UnmutedJinglesConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.runelite.api.gameval.SpriteID;

import javax.inject.Inject;

@RequiredArgsConstructor(staticName = "of")
public class JingleInfo{

    public enum Type{
        LEVEL("level"),
        LEAGUES("leagues");

        public final String folderName;
        private Type(String folderName){
            this.folderName = folderName;
        }
    }

    @Getter
    @NonNull
    String fileName;
    @Getter
    @NonNull
    Type type;
    /*
    @Getter
    @NonNull
    int startTime;

     */

    public int getPriority(UnmutedJinglesConfig config){
        int priority = 1;
        switch (type){
            case LEVEL:
                priority = config.levelPriority();
                break;
            case LEAGUES:
                priority = config.leaguesPriority();
                break;
            default:
                break;
        }
        return priority;
    }
}
