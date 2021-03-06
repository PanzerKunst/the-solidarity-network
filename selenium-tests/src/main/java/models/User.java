package models;

public class User {
    public static final String NEW_HR_SUBSCRIPTION_FREQUENCY_NONE = "NONE";
    public static final String NEW_HR_SUBSCRIPTION_FREQUENCY_EACH_NEW_REQUEST = "EACH_NEW_REQUEST";
    public static final String NEW_HR_SUBSCRIPTION_FREQUENCY_DAILY = "DAILY";
    public static final String NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY = "WEEKLY";

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String city;
    private String countryId;
    private String streetAddress;
    private String postCode;
    private String description;
    private Boolean isSubscribedToNews = true;
    private String subscriptionToNewHelpRequests = NEW_HR_SUBSCRIPTION_FREQUENCY_WEEKLY;

    public User(String firstName,
                String lastName,
                String email,
                String username,
                String password,
                String city,
                String countryId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.city = city;
        this.countryId = countryId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSubscribedToNews() {
        return isSubscribedToNews;
    }

    public void setSubscribedToNews(Boolean subscribedToNews) {
        this.isSubscribedToNews = subscribedToNews;
    }

    public String getSubscriptionToNewHelpRequests() {
        return subscriptionToNewHelpRequests;
    }

    public void setSubscriptionToNewHelpRequests(String subscriptionToNewHelpRequests) {
        this.subscriptionToNewHelpRequests = subscriptionToNewHelpRequests;
    }

    public boolean isSubscribedToNewHelpRequests() {
        return subscriptionToNewHelpRequests != NEW_HR_SUBSCRIPTION_FREQUENCY_NONE;
    }
}
