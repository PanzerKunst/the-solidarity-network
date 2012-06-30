CBR.Controllers.TemplateController = new Class({
    initialize: function (options) {
        this.options = options;
    },

    getEl: function () {
        return jQuery(this.options.el);
    },

    httpStatusCode: {
        noContent: 204,
        unauthorized: 401
    }
});