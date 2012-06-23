CBR.Controllers.SearchHelpRequests = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().append(
            Mustache.to_html(
                jQuery("#content-template").html(),
                this.options
            )
        );

        this._initValidation();

        jQuery("#search-button").click(jQuery.proxy(this._doSearch, this));
        jQuery("form").submit(jQuery.proxy(this._doSearch, this));
    },

    _initValidation: function () {
        jQuery(".field-error").hide();

        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "query"
            ]
        });
    },

    _doSearch: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            var helpRequest = new CBR.Models.HelpRequest({
                // TODO
            });

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/help-requests",
                onSuccess: function (responseText, responseXML) {
                    jQuery("#search-results").append(
                        Mustache.to_html(
                            jQuery("#search-results-template").html(),
                            {
                                helpRequests: JSON.parse(responseText)
                            }
                        )
                    );
                },
                onFailure: function (xhr) {
                    if (xhr.status === 401)
                        location.replace("/login");
                    else
                        alert("AJAX fail :(");
                }
            }).get();
        }
    }
});
