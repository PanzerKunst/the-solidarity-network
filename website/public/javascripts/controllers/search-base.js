CBR.Controllers.SearchBase = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
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

        var repliedByPattern = /( |^)repliedBy=(\w+)/;
        value = repliedByPattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.repliedBy = value[2];

        if (CBR.isEmptyObject(requestData))
            return { query: queryFieldValue };

        return requestData;
    }
});
