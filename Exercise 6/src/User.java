public class User {
    private String currentUsername;
    private String latestUsername;
    private String password;
    private String accountCreated; // Fixed variable name
    private String email;

    // Constructor
    public User(String currentUsername, String latestUsername, String password, String email, String accountCreated) {  
        this.currentUsername = currentUsername;
        this.latestUsername = latestUsername;
        this.password = password;
        this.accountCreated = accountCreated;
        this.email = email;
    }

    // Getter and Setter for currentUsername (Old username)
    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    // Getter and Setter for latestUsername
    public String getLatestUsername() {
        return latestUsername;
    }

    public void setLatestUsername(String latestUsername) {
        this.latestUsername = latestUsername;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for accountCreated (Fixed method names)
    public String getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(String accountCreated) {
        this.accountCreated = accountCreated;
    }
}
