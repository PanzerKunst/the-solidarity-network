(function ($) {
    $.fn.slideDownAnimated = function (options) {
        var options = options || {};
        var duration = options.duration || 200;

        return this.animate({
                opacity: 1,
                lineHeight: 1.4
            },
            duration,
            options.complete
        );
    };

    $.fn.slideUpAnimated = function (options) {
        var options = options || {};
        var duration = options.duration || 200;

        return this.animate({
                opacity: 0,
                lineHeight: 0
            },
            duration,
            options.complete
        );
    };

    $.fn.hideBeforeSlideDown = function () {
        return this.css("line-height", 0)
            .css("opacity", 0);
    };

    /* Unused for now
    $.fn.isVisibleForAnimations = function () {
        return this.css("line-height") !== "0px" && this.css("opacity") !== "0";
    }; */
})(jQuery);
