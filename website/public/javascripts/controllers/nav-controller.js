CBR.Controllers.NavController = new Class({
    initialize: function (options) {
        this.options = options;
    },

    run: function () {
        // Init dropdowns
        jQuery("#header-nav li > span").click(jQuery.proxy(this._dropDownSubMenu, this));

        // Init menu
        jQuery("#header-nav > span").click(jQuery.proxy(this._dropDownWholeMenu, this));

        this._initElements();
    },

    _initElements: function () {
        this.$menu = jQuery("#header-nav > ul");
    },

    _dropDownSubMenu: function (e) {
        e.preventDefault();

        // Close all open
        jQuery(".sub-nav").slideUpCustom();

        var $li = jQuery(e.currentTarget).parent();

        var $subNav = $li.children(".sub-nav");
        if ($subNav.is(":visible"))
            this._closeDropDown($subNav);
        else
            this._openDropDown($subNav);
    },

    _openDropDown: function ($subNav) {
        $subNav.slideDownCustom();
    },

    _closeDropDown: function ($subNav) {
        $subNav.slideUpCustom();
    },

    _dropDownWholeMenu: function () {
        if (this.$menu.is(":visible"))
            this.$menu.slideUpCustom();
        else
            this.$menu.slideDownCustom();
    }
});
