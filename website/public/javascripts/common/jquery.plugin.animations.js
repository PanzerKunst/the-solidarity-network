(function ($) {
    $.fn.slideDownCustom = function () {
        if (Modernizr.mq("screen and (min-width: 49em)"))
            return this.slideDown();

        return this.show();
    };

    $.fn.slideUpCustom = function () {
        if (Modernizr.mq("screen and (min-width: 49em)"))
            return this.slideUp();

        return this.hide();
    };
})(jQuery);
