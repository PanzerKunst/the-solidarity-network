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
        this._initSearchQuery();
    },

    _getQuery: function () {
        return this.options.query;
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

    _initSearchQuery: function() {
        if (this._getQuery() !== undefined) {
            var queryFieldValue = "";

            for (var key in this._getQuery())
                queryFieldValue += key + "=" + this._getQuery()[key] + " "; // Space is separator between params
        }

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
                requestData = this._getSearchRequestData(queryFieldValue);

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
    },

    _getSearchRequestData: function(queryFieldValue) {
        var requestData = {};

        var byPattern = /( |^)by=(\w+)/;
        var value = byPattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.by = value[2];

        if (CBR.isEmptyObject(requestData))
            return { query: queryFieldValue };

        return requestData;
    }
});
