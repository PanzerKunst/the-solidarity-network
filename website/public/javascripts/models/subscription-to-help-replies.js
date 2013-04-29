CBR.Models.SubscriptionToHelpReplies = new Class({
    Extends: CBR.Models.JsonSerializableModel,

    options: {  // Defaults
    },

    getId: function () {
        return this.options.id;
    },

    getRequestId: function () {
        return this.options.requestId;
    }
});
