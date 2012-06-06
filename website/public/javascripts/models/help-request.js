CBR.HelpRequest = new Class({
    Extends: CBR.JsonSerializableModel,

    options: {  // Defaults
    },

    getId: function() {
        return this.options.id;
    },

    getDescription: function() {
        return this.options.description;
    },

    getExpiryDate: function() {
        return this.options.expiryDate;
    }
});
