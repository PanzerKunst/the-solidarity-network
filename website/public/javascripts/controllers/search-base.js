CBR.Controllers.SearchBase = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    getSearchRequestData: function (queryFieldValue) {
        var requestData = {};

        var titlePattern = /( |^)title=((\*?\w+\*?)|("\*?(\w|\s)+\*?"))/;
        var value = titlePattern.exec(queryFieldValue);
        if (value && value.length === 6)
            requestData.title = value[2].replace(/"/g, "");

        var descPattern = /( |^)desc=((\*?\w+\*?)|("\*?(\w|\s)+\*?"))/;
        value = descPattern.exec(queryFieldValue);
        if (value && value.length === 6)
            requestData.desc = value[2].replace(/"/g, "");

        var usernamePattern = /( |^)username=(\w+)/;
        value = usernamePattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.username = value[2];

        var firstNamePattern = /( |^)firstName=((\*?\w+\*?)|("\*?(\w|\s)+\*?"))/;
        value = firstNamePattern.exec(queryFieldValue);
        if (value && value.length === 6)
            requestData.firstName = value[2].replace(/"/g, "");

        var lastNamePattern = /( |^)lastName=((\*?\w+\*?)|("\*?(\w|\s)+\*?"))/;
        value = lastNamePattern.exec(queryFieldValue);
        if (value && value.length === 6)
            requestData.lastName = value[2].replace(/"/g, "");

        var cityPattern = /( |^)city=((\*?\w+\*?)|("\*?(\w|\s)+\*?"))/;
        value = cityPattern.exec(queryFieldValue);
        if (value && value.length === 6)
            requestData.city = value[2].replace(/"/g, "");

        var countryPattern = /( |^)country=((\*?\w+\*?)|("\*?(\w|\s)+\*?"))/;
        value = countryPattern.exec(queryFieldValue);
        if (value && value.length === 6)
            requestData.country = value[2].replace(/"/g, "");

        var repliedByPattern = /( |^)repliedBy=(\w+)/;
        value = repliedByPattern.exec(queryFieldValue);
        if (value && value.length === 3)
            requestData.repliedBy = value[2];

        if (CBR.isEmptyObject(requestData))
            return { query: queryFieldValue };

        return requestData;
    }
});
