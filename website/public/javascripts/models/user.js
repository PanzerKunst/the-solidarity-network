CBR.User = new Class({
    Extends: CBR.JsonSerializableModel,

    options: {  // Defaults
    },

    GetId: function() {
        return this.options.id;
    },

    GetUsername: function() {
        return this.options.username;
    },

    GetFirstName: function() {
        return this.options.firstName;
    },

    GetLastName: function() {
        return this.options.lastName;
    },

    GetEmail: function() {
        return this.options.email;
    },

    GetPassword: function() {
        return this.options.password;
    },

    GetStreetAddress: function() {
        return this.options.streetAddress;
    },

    GetPostCode: function() {
        return this.options.postCode;
    },

    GetCity: function() {
        return this.options.city;
    },

    GetCountryId: function() {
        return this.options.countryId;
    }
});
