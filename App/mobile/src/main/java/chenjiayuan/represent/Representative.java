package chenjiayuan.represent;

/**
 * Created by chenjiayuan on 2/27/16.
 */
public class Representative {
    private String name;
    private String role;
    private String party;
    private String email;
    private String website;
    private String lastTweet;
    private String term;

    public Representative(String name, String role, String party, String email, String website, String lastTweet, String term) {
        super();
        this.name = name;
        this.role = role;
        this.party = party;
        this.email = email;
        this.website = website;
        this.lastTweet = lastTweet;
        this.term = term;
    }

    public String getName() {
        return name;
    }
    public String getRole() { return role; }
    public String getParty() {
        return party;
    }
    public String getEmail() {
        return email;
    }
    public String getWebsite() {
        return website;
    }
    public String getLastTweet() {
        return lastTweet;
    }
    public String getTerm() {
        return term;
    }
}
/*

(Senator or Representative) Full Name
        A picture of them
        Party (i.e. Democrat, Republican, Independent) - You can use color with D, R, I to encode this as well.
        Email (with link)
        Website (with link)
        Last Tweet they posted to their Twitter account
        (MORE INFO button or some signifier - See below)
*/