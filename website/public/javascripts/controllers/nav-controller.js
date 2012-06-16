CBR.Controllers.NavController = new Class({
    initialize: function (options) {
        this.options = options;
    },

    run: function () {
        this._initDropDowns();
    },

    _initDropDowns: function () {
        var _this = this;

        var $title = jQuery("#header-nav span");

        $title.click(function () {
            // Close all open
            jQuery(".sub-nav").slideUp(200, "easeInQuad");

            var $li = jQuery(this).parent();

            var $subNav = $li.find("> ul");
            if ($subNav.is(":visible"))
                _this._closeDropDown($li);
            else
                _this._openDropDown($li);
        });
    },

    _openDropDown: function ($li) {
        jQuery("> ul", $li).slideDown(200, "easeOutQuad");
    },

    _closeDropDown: function ($li) {
        jQuery("> ul", $li).slideUp(200, "easeInQuad");
    }
});