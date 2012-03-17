CBR.Controllers.Register = new Class({
    Extends: CBR.Controllers.MustacheController,

    initialize: function(options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().html(
            Mustache.to_html(
                jQuery("#content-template").html(),
                this.options
            )
        );
    }
});
