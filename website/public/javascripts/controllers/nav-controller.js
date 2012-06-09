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
            // Close all open
            jQuery(".sub-nav").slideUp(200, "easeInQuad");

            var $li = jQuery(this).parent();

            var $subNav = $li.find("> ul");
            if ($subNav.is(":visible"))
                self._closeDropDown($li);
            else
                self._openDropDown($li);
        });
    },

    _openDropDown: function ($li) {
        jQuery("> ul", $li).slideDown(200, "easeOutQuad");
    },

    _closeDropDown: function ($li) {
        jQuery("> ul", $li).slideUp(200, "easeInQuad");
    }
});