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

    setTitle: function (title) {
        this.options.title = title;
    },

    getText: function () {
        return this.options.text;
    },

    getCreationDatetime: function () {
        return this.options.creationDatetime;
    },

    getReplyToMessageId: function () {
        return this.options.replyToMessageId;
    }
});
