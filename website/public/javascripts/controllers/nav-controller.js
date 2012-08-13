CBR.Controllers.NavController = new Class({
    initialize: function (options) {
        this.options = options;
    },

    run: function () {
        // Init dropdowns
        jQuery("#header-nav li > span").click(jQuery.proxy(this._dropDownSubMenu, this));

        // Init menu
        jQuery("#header-nav > span").click(jQuery.proxy(this._dropDownWholeMenu, this));
    },

    _dropDownSubMenu: function (e) {
        e.preventDefault();

        // Close all open
        jQuery(".sub-nav").slideUp(200, "easeInQuad");

        var $li = jQuery(e.currentTarget).parent();

        var $subNav = $li.find("> ul");
        if ($subNav.is(":visible"))
            this._closeDropDown($li);
        else
            this._openDropDown($li);
    },

    _openDropDown: function ($li) {
        jQuery("> ul", $li).slideDown(200, "easeOutQuad");
    },

    _closeDropDown: function ($li) {
        jQuery("> ul", $li).slideUp(200, "easeInQuad");
    },

    _dropDownWholeMenu: function () {
        var $menu = jQuery("#header-nav > ul");

        if ($menu.is(":visible"))
            $menu.slideUp(200, "easeInQuad");
        else
            $menu.slideDown(200, "easeOutQuad");
    }
});