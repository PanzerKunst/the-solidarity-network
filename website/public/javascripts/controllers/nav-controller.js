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
        jQuery(".sub-nav").slideUpAnimated({
            complete: function () {
                jQuery(this).hide();
            }
        });

        var $li = jQuery(e.currentTarget).parent();

        var $subNav = $li.find("> .sub-nav");
        if ($subNav.is(":visible"))
            this._closeDropDown($li);
        else
            this._openDropDown($li);
    },

    _openDropDown: function ($li) {
        jQuery("> .sub-nav", $li).show(0, function () {
            jQuery(this).slideDownAnimated();
        });
    },

    _closeDropDown: function ($li) {
        jQuery("> .sub-nav", $li)
            .slideUpAnimated({
                complete: function () {
                    jQuery(this).hide();
                }
            });
    },

    _dropDownWholeMenu: function () {
        if (this.$menu.is(":visible"))
            this.$menu.slideUpAnimated({
                complete: function () {
                    jQuery(this).hide();
                }
            });
        else
            this.$menu.show(0, function () {
                jQuery(this).slideDownAnimated();
            });
    }
});
