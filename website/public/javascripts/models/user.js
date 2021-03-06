CBR.Models.User = new Class({
    Extends: CBR.Models.JsonSerializableModel,

    options: {  // Defaults
    },

    getId: function() {
        return this.options.id;
    },

    getUsername: function() {
        return this.options.username;
    },

    getFirstName: function() {
        return this.options.firstName;
    },

    getLastName: function() {
        return this.options.lastName;
    },

    getEmail: function() {
        return this.options.email;
    },

    getPassword: function() {
        return this.options.password;
    },

    setPassword: function(password) {
        this.options.password = password;
    },

    getStreetAddress: function() {
        return this.options.streetAddress;
    },

    getPostCode: function() {
        return this.options.postCode;
    },

    getCity: function() {
        return this.options.city;
    },

    getCountryId: function() {
        return this.options.countryId;
    },

    getIsSubscribedToNews: function() {
        return this.options.isSubscribedToNews;
    },

    setIsSubscribedToNews: function(isSubscribedToNews) {
        this.options.isSubscribedToNews = isSubscribedToNews;
    },

    getSubscriptionToNewHelpRequests: function() {
        return this.options.subscriptionToNewHelpRequests;
    },

    setSubscriptionToNewHelpRequests: function(subscriptionToNewHelpRequests) {
        this.options.subscriptionToNewHelpRequests = subscriptionToNewHelpRequests;
    }
});
