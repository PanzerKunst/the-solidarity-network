CBR.Models.HelpRequest = new Class({
    Extends: CBR.Models.JsonSerializableModel,

    options: {  // Defaults
    },

    getId: function () {
        return this.options.id;
    },

    getRequesterId: function () {
        return this.options.requesterId;
    },

    getTitle: function () {
        return this.options.title;
    },

    getDescription: function () {
        return this.options.description;
    },

    getCreationDatetime: function () {
        return this.options.creationDatetime;
    },

    getExpiryDate: function () {
        return this.options.expiryDate;
    }
});
