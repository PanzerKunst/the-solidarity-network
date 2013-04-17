CBR.Models.Reference = new Class({
    Extends: CBR.Models.JsonSerializableModel,

    options: {  // Defaults
    },

    getId: function () {
        return this.options.id;
    },

    getFromUserId: function () {
        return this.options.fromUserId;
    },

    getToUserId: function () {
        return this.options.toUserId;
    },

    getWasHelped: function () {
        return this.options.wasHelped;
    },

    getRatingId: function () {
        return this.options.ratingId;
    },

    getText: function () {
        return this.options.text;
    },

    getCreationDatetime: function () {
        return this.options.creationDatetime;
    }
});
