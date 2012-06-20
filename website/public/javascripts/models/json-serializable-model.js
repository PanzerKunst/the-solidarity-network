CBR.Models.JsonSerializableModel = new Class({
    initialize: function (options) {
        this.options = options;
    },

    toJSON: function () {
        return this.options;
    }
});