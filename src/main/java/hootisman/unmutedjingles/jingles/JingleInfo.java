package hootisman.unmutedjingles.jingles;

import hootisman.unmutedjingles.UnmutedJinglesConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class JingleInfo{

    public enum Type{
        LEVEL("level"),
        LEAGUES("leagues"),
        OTHER("other");

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
            case OTHER:
                priority = config.combatPriority();
                break;
            default:
                break;
        }
        return priority;
    }

    public boolean isEnabled(UnmutedJinglesConfig config){
        boolean isEnabled = true;
        switch (type){
            case LEVEL:
                isEnabled = config.levelEnabled();
                break;
            case LEAGUES:
                isEnabled = config.leaguesEnabled();
                break;
            case OTHER:
                isEnabled = config.combatEnabled();
                break;
            default:
                break;
        }
        return isEnabled;
    }
}
