CBR.Controllers.NavController = new Class({
    initialize: function (options) {
        this.options = options;
    },

    run: function () {
        this._initDropDowns();
    },

    _initDropDowns: function () {
        var self = this;

        var $title = jQuery("#header-nav span");

        $title.click(function () {
            var $subNav = $title.parent().find("> ul");
            if ($subNav.is(":visible"))
                self._closeDropDown($title.parent());
            else
                self._openDropDown($title.parent());
        });
    },

    _openDropDown: function ($li) {
        jQuery("> ul", $li).slideDown(200, "easeOutQuad");
    },

    _closeDropDown: function ($li) {
        jQuery("> ul", $li).slideUp(200, "easeInQuad");
    }
});