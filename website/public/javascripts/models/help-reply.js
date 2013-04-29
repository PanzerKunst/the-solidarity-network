CBR.Models.HelpReply = new Class({
    Extends: CBR.Models.JsonSerializableModel,

    options: {  // Defaults
    },

    getId: function () {
        return this.options.id;
    },

    getRequestId: function () {
        return this.options.requestId;
    },

    getReplierId: function () {
        return this.options.replierId;
    },

    getText: function () {
        return this.options.text;
    },

    getCreationDatetime: function () {
        return this.options.creationDatetime;
    }
});
