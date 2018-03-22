package cite.ansteph.beerly.model;

/**
 * Created by loicstephan on 2018/03/22.
 */

public class Leaderboard {

    String rank, username, signNum;


    public Leaderboard() {
    }


    public Leaderboard(String rank, String username, String signNum) {
        this.rank = rank;
        this.username = username;
        this.signNum = signNum;
    }


    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSignNum() {
        return signNum;
    }

    public void setSignNum(String signNum) {
        this.signNum = signNum;
    }
}
