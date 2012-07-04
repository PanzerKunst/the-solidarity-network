CBR.Models.HelpResponse = new Class({
    Extends: CBR.Models.JsonSerializableModel,

    options: {  // Defaults
    },

    getId: function () {
        return this.options.id;
    },

    getRequestId: function () {
        return this.options.requestId;
    },

    getResponderId: function () {
        return this.options.responderId;
    },

    getText: function () {
        return this.options.text;
    },

    getCreationDatetime: function () {
        return this.options.creationDatetime;
    },


    /**
     * Frontend specific
     */

    getResponder: function () {
        return this.options.requester;
    },

    setResponder: function (requester) {
        this.options.requester = requester;
    }
});
