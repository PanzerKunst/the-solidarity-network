CBR.HelpRequest = new Class({
    Extends: CBR.JsonSerializableModel,

    options: {  // Defaults
    },

    GetId: function() {
        return this.options.id;
    },

    GetDescription: function() {
        return this.options.description;
    },

    GetExpiryDate: function() {
        return this.options.expiryDate;
    }
});
