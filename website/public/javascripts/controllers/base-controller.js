CBR.Controllers.BaseController = new Class({
    initialize: function (options) {
        this.options = options;
    },

    getEl: function () {
        return jQuery(this.options.el);
    }
});