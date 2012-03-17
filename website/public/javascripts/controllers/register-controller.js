CBR.Controllers.Register = new Class({
    Extends: CBR.Controllers.BaseController,

    initialize: function(options) {
        this.parent(options);
    },

    getCountries: function() {
        return this.options.countries;
    },

    run: function () {
        this.getEl().html(
            Mustache.to_html(
                jQuery("#content-template").html(),
                this.getCountries()
            )
        );
    }
});
