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

    getCreationDate: function () {
        return this.options.creationDate;
    },

    getExpiryDate: function () {
        return this.options.expiryDate;
    },


    /**
     * Frontend specific
     */

    getRequester: function () {
        return this.options.requester;
    },

    setRequester: function (requester) {
        this.options.requester = requester;
    }
});
