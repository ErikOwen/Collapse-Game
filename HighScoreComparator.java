import java.util.Comparator;

/**
 * Compares two highscores.
 * 
 * @author Erik Owen
 * @version 1
 */
public class HighScoreComparator implements Comparator<HighScore>{

    @Override
    public int compare(HighScore x, HighScore y)
    {
        int returnVal = 0;
        if(x.getScore() < y.getScore())
        {
            returnVal = -1;
        }
        if(x.getScore() > y.getScore()){
            returnVal = 1;
        }
        if(x.getScore() == y.getScore())
        {
            if(x.getName().compareTo(y.getName()) > 0)
            {
                    returnVal = 1;
            }
            if(x.getName().compareTo(y.getName()) < 0)
            {
                    returnVal = -1;
            }
        }
        
        return returnVal;
    }
}