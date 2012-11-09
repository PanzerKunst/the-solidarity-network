CBR.Controllers.SearchHelpRequests = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().append(
            Mustache.render(
                jQuery("#content-template").html(),
                this.options
            )
        );

        this._initElements();
        this._initValidation();
        this._initEvents();
    },

    _initElements: function () {
        this.$queryField = jQuery("#query");
        this.$searchResults = jQuery("#search-results");
    },

    _initValidation: function () {
        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "query"
            ]
        });
    },

    _initEvents: function () {
        jQuery("#search-button").click(jQuery.proxy(this._doSearch, this));
        jQuery("form").submit(jQuery.proxy(this._doSearch, this));
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
                            Mustache.render(
                                jQuery("#search-results-template").html(),
                                { helpRequests: _this._formatDates(JSON.parse(responseText)) }
                            )
                        );
                        jQuery("#search-returned-nothing").hide();
                        jQuery(".search-result").click(_this._navigateToHelpRequest);
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

    _navigateToHelpRequest: function (e) {
        e.preventDefault();

        var helpRequestId = jQuery(e.currentTarget).data("id");

        if (helpRequestId !== undefined)
            location.href = "/help-requests/" + helpRequestId;
    },

    /* TODO
     _getSearchedCity: function() {
     var query = this.$queryField.val();
     }, */

    _formatDates: function (helpRequests) {
        for (var i = 0; i < helpRequests.length; i++) {
            var currentHelpRequest = helpRequests[i];

            // Format submitted date
            currentHelpRequest.creationDatetime = this._formatDate(currentHelpRequest.creationDatetime);

            // Format expiry date
            currentHelpRequest.expiryDate = this._formatDate(currentHelpRequest.expiryDate);
        }

        return helpRequests;
    },

    _formatDate: function(yyyyMMdd) {
        var year = yyyyMMdd.substring(0, 4);
        var month = yyyyMMdd.substring(5, 7);
        var day = yyyyMMdd.substring(8, 10);

        var formatedDate = day + "/" + month;

        if (year !== new Date().getFullYear().toString())
            formatedDate += "/" + year;

        return formatedDate;
    }
});
