CBR.Controllers.TemplateController = new Class({
    initialize: function (options) {
        this.options = options;
    },

    getEl: function () {
        return jQuery(this.options.el);
    },

    setActivePill: function (e) {
        e.preventDefault();

        var $a = jQuery(e.currentTarget);
        var $li = $a.parent();
        var $ul = $li.parent();
        $ul.children().removeClass("active");

        $li.addClass("active");

        $a.trigger("active-toggled");
    },

    initElements: function () {
        jQuery("select", this.options.el).select2({minimumResultsForSearch: 20});

        this._applyModernizrRules();
    },

    getParameterByName: function (name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(window.location.search);
        if (results === null)
            return undefined;

        return decodeURIComponent(results[1].replace(/\+/g, " "));
    },

    formatDate: function (yyyyMMdd) {
        var year = yyyyMMdd.substring(0, 4);
        var month = yyyyMMdd.substring(5, 7);
        var day = yyyyMMdd.substring(8, 10);

        var now = new Date();

        var formattedDate = day + "/" + month;

        if (parseInt(year, 10) !== now.getFullYear())
            formattedDate += "/" + year;

        return formattedDate;
    },

    formatDatetime: function (yyyyMMddHHmmss) {
        var year = yyyyMMddHHmmss.substring(0, 4);
        var month = yyyyMMddHHmmss.substring(5, 7);
        var day = yyyyMMddHHmmss.substring(8, 10);
        var hour = yyyyMMddHHmmss.substring(11, 13);
        var minute = yyyyMMddHHmmss.substring(14, 16);

        var now = new Date();

        var formattedDatetime = "";

        if (parseInt(day, 10) !== now.getDate() || parseInt(month, 10) !== now.getMonth() + 1)
            formattedDatetime += day + "/" + month;

        if (parseInt(year, 10) !== now.getFullYear())
            formattedDatetime += "/" + year;

        return formattedDatetime + " " + hour + ":" + minute;
    },

    getSearchRequestData: function (queryFieldValue) {
        var requestData = {};

        var usernamePattern = /( |^)username=(\w+)/;
        var value = usernamePattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.username = value[2];

        var firstNamePattern = /( |^)firstName=(\*?\w+\*?)/;
        value = firstNamePattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.firstName = value[2];

        var lastNamePattern = /( |^)lastName=(\*?\w+\*?)/;
        value = lastNamePattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.lastName = value[2];

        var cityPattern = /( |^)city=(\*?\w+\*?)/;
        value = cityPattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.city = value[2];

        var countryPattern = /( |^)country=(\*?\w+\*?)/;
        value = countryPattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.country = value[2];

        var respondedByPattern = /( |^)respondedBy=(\w+)/;
        value = respondedByPattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.respondedBy = value[2];

        if (CBR.isEmptyObject(requestData))
            return { query: queryFieldValue };

        return requestData;
    },

    _applyModernizrRules: function () {
        if (!Modernizr.input.placeholder) {
            jQuery(".mdnz-polyfill.placeholder").show();
        }
    },

    httpStatusCode: {
        noContent: 204,
        unauthorized: 401
    }
});