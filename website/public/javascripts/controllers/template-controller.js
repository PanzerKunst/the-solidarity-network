CBR.Controllers.TemplateController = new Class({
    initialize: function (options) {
        this.options = options;
    },

    getEl: function () {
        return jQuery(this.options.el);
    },

    setActivePill: function (e) {
        e.preventDefault();

        var $a = jQuery(e.currentTarget);
        var $li = $a.parent();
        var $ul = $li.parent();
        $ul.children().removeClass("active");

        $li.addClass("active");

        $a.trigger("active-toggled");
    },

    initElements: function () {
        jQuery("select", this.options.el).select2({minimumResultsForSearch: 20});
    },

    formatDate: function(yyyyMMdd) {
        var year = yyyyMMdd.substring(0, 4);
        var month = yyyyMMdd.substring(5, 7);
        var day = yyyyMMdd.substring(8, 10);

        var today = new Date();

        var formattedDate = day + "/" + month;

        if (year !== today.getFullYear().toString())
            formattedDate += "/" + year;

        return formattedDate;
    },

    httpStatusCode: {
        noContent: 204,
        unauthorized: 401
    }
});