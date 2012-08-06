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

        this.$queryField = jQuery("#query");
        this.$searchResults = jQuery("#search-results");

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
            /* TODO var helpRequest = new CBR.Models.HelpRequest();

            var searchedCity = this._getSearchedCity();
            if (searchedCity !== undefined)
                helpRequest.setRequester(new CBR.Models.User({
                    city: searchedCity
                })); */

            var requestData;
            var query = this.$queryField.val();
            if (query !== "")
                requestData = { query: query };

            var _this = this;

            new Request({
                url: "/api/help-requests",
                data: requestData,
                onSuccess: function (responseText, responseXML) {
                    if (this.status === _this.httpStatusCode.noContent) {
                        _this.$searchResults.html("");
                        jQuery("#search-returned-nothing").show();
                    }
                    else {
                        _this.$searchResults.html(
                            Mustache.to_html(
                                jQuery("#search-results-template").html(),
                                { helpRequests: JSON.parse(responseText) }
                            )
                        );
                        jQuery("#search-returned-nothing").hide();
                        jQuery("tr").click(_this._navigateToHelpRequest);
                    }
                },
                onFailure: function (xhr) {
                    if (xhr.status === _this.httpStatusCode.unauthorized)
                        location.replace("/login");
                    else
                        alert("AJAX fail :(");
                }
            }).get();
        }
    },

    _navigateToHelpRequest: function(e) {
        e.preventDefault();

        var helpRequestId = jQuery(e.currentTarget).data("id");

        if (helpRequestId !== undefined)
            location.href = "/help-requests/" + helpRequestId;
    },

    _getSearchedCity: function() {
        var query = this.$queryField.val();
    }
});
