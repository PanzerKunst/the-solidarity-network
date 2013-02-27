CBR.Models.Message = new Class({
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

    getTitle: function () {
        return this.options.title;
    },

    getContent: function () {
        return this.options.content;
    },

    getCreationDatetime: function () {
        return this.options.creationDatetime;
    },


    /**
     * Frontend specific
     */

    getFromUser: function () {
        return this.options.fromUser;
    },

    setFromUser: function (user) {
        this.options.fromUser = user;
    },

    getToUser: function () {
        return this.options.toUser;
    },

    setToUser: function (user) {
        this.options.toUser = user;
    }
});
