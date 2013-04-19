CBR.Controllers.HelpDashboard = new Class({
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

        this.initElements();
        this._initValidation();
        this._initEvents();
        this._initSearchQuery();
    },

    _getUser: function () {
        return this.options.user;
    },

    initElements: function () {
        this.parent();

        this.$queryField = jQuery("#query");
        this.$searchResults = jQuery("#search-results");
        this.$searchResultedNothingParagraph = jQuery("#search-returned-nothing");
        this.$searchResultsTemplate = jQuery("#search-results-template");

        // To use correct CSS declaration
        jQuery(this.getEl()).addClass("search-help-requests");
    },

    _initValidation: function () {
        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "query"
            ]
        });
    },

    _initEvents: function () {
        jQuery("#search").click(jQuery.proxy(this._doSearch, this));
        jQuery("form").submit(jQuery.proxy(this._doSearch, this));
    },

    _initSearchQuery: function () {
        // Space is separator between params
        var queryFieldValue = "country=" + this._getUser().country.name + " city=" + this._getUser().city;

        this.$queryField.val(queryFieldValue);
        this._doSearch();
    },

    _doSearch: function (e) {
        if (e !== undefined)
            e.preventDefault();

        if (this.validator.isValid()) {
            var requestData;
            var queryFieldValue = this.$queryField.val();
            if (queryFieldValue !== "")
                requestData = this.getSearchRequestData(queryFieldValue);

            var _this = this;

            new Request({
                url: "/api/help-requests?isOwnFiltered=true",
                data: requestData,
                onSuccess: function (responseText, responseXML) {
                    if (this.status === _this.httpStatusCode.noContent) {
                        _this.$searchResults.html("");
                        _this.$searchResultedNothingParagraph.show();
                    }
                    else {
                        _this.$searchResults.html(
                            Mustache.render(
                                _this.$searchResultsTemplate.html(),
                                { helpRequests: _this._formatDates(JSON.parse(responseText)) }
                            )
                        );
                        _this.$searchResultedNothingParagraph.hide();
                        jQuery(".clickable").click(jQuery.proxy(_this._navigateToHelpRequest));
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

    _formatDates: function (helpRequests) {
        for (var i = 0; i < helpRequests.length; i++) {
            var currentHelpRequest = helpRequests[i];

            // Format submitted date
            currentHelpRequest.creationDatetime = this.formatDate(currentHelpRequest.creationDatetime);

            // Format expiry date
            currentHelpRequest.expiryDate = this.formatDate(currentHelpRequest.expiryDate);
        }

        return helpRequests;
    }
});
