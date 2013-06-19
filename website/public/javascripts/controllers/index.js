CBR.Controllers.Index = new Class({
    initialize: function (options) {
        this._initEvents();
    },

    _initEvents: function () {
        jQuery(".read-more-button").click(jQuery.proxy(this._expandReadMore, this));
    },

    _expandReadMore: function(e) {
        var $target = jQuery(e.currentTarget);
        $target.closest("li").addClass("expanded");
        $target.closest("li").children("p").slideDownCustom();
    }
});
