CBR.Services.Validator = new Class({
    checkEmpty: "empty",
    checkEmail: "email",
    checkUsername: "username",
    checkDateInFuture: "in-future",
    checkDateInMaxTwoWeeks: "in-max-2-weeks",

    initialize: function (options) {
        this.options = options;

        for (var i=0; i<this.getFieldIds().length; i++) {
            var $field = jQuery("#" + this.getFieldIds()[i]);
            this.addBlurEvent($field);
            this.addValueChangedEvent($field);
        }
    },

    getFieldIds: function() {
        return this.options.fieldIds;
    },

    get$empty: function ($field) {
        return this.get$error($field, this.checkEmpty);
    },

    get$email: function ($field) {
        return this.get$error($field, this.checkEmail);
    },

    get$username: function ($field) {
        return this.get$error($field, this.checkUsername);
    },

    get$inFuture: function ($field) {
        return this.get$error($field, this.checkDateInFuture);
    },

    get$inMaxTwoWeeks: function ($field) {
        return this.get$error($field, this.checkDateInMaxTwoWeeks);
    },

    get$error: function ($field, checkType) {
        return $field.parent().find("p[data-check=" + checkType + "]");
    },

    isToCheckIfEmpty: function ($field) {
        return this.get$empty($field).length === 1;
    },

    isToCheckIfEmail: function ($field) {
        return this.get$email($field).length === 1;
    },

    isToCheckIfUsername: function ($field) {
        return this.get$username($field).length === 1;
    },

    isToCheckIfInFuture: function ($field) {
        return this.get$inFuture($field).length === 1;
    },

    isToCheckIfInMaxTwoWeeks: function ($field) {
        return this.get$inMaxTwoWeeks($field).length === 1;
    },

    isValidEmail: function (email) {
        var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        return reg.test(email);
    },

    isValidUsername: function (username) {
        var reg = /^([A-Za-z0-9_\-])+$/;
        return reg.test(username);
    },

    isInFuture: function (dateStr) {
        var yearMonthDay = dateStr.split("-");
        var year = parseInt(yearMonthDay[0], 10);
        var month = parseInt(yearMonthDay[1], 10);
        var day = parseInt(yearMonthDay[2], 10);

        var date = new Date(year, month-1, day);
        var now = new Date();

        var oneDayInMillis = 1000*60*60*24;
        var nbDaysDifference = Math.ceil((date - now) / oneDayInMillis);

        return nbDaysDifference > 0;
    },

    isInMaxTwoWeeks: function (dateStr) {
        var yearMonthDay = dateStr.split("-");
        var year = parseInt(yearMonthDay[0], 10);
        var month = parseInt(yearMonthDay[1], 10);
        var day = parseInt(yearMonthDay[2], 10);

        var date = new Date(year, month-1, day);
        var inTwoWeeks = new Date();
        inTwoWeeks.setDate(inTwoWeeks.getDate() + 14);

        var oneDayInMillis = 1000*60*60*24;
        var nbDaysDifference = Math.ceil((inTwoWeeks - date) / oneDayInMillis);

        return nbDaysDifference >= 0;
    },

    validateField: function($field) {

        // Empty?
        if (this.isToCheckIfEmpty($field) && !$field.val()) {
            this.get$empty($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this.get$empty($field).slideUp(200, "easeInQuad");

        // Email?
        if (this.isToCheckIfEmail($field) && !this.isValidEmail($field.val())) {
            this.get$email($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this.get$email($field).slideUp(200, "easeInQuad");

        // Username?
        if (this.isToCheckIfUsername($field) && !this.isValidUsername($field.val())) {
            this.get$username($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this.get$username($field).slideUp(200, "easeInQuad");

        // In the future?
        if (this.isToCheckIfInFuture($field) && !this.isInFuture($field.val())) {
            this.get$inFuture($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this.get$inFuture($field).slideUp(200, "easeInQuad");

        // In max 2 weeks?
        if (this.isToCheckIfInMaxTwoWeeks($field) && !this.isInMaxTwoWeeks($field.val())) {
            this.get$inMaxTwoWeeks($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this.get$inMaxTwoWeeks($field).slideUp(200, "easeInQuad");

        return true;
    },

    isValid: function() {
        var result = true;

        for (var i=0; i<this.getFieldIds().length; i++)
            if (!this.validateField(jQuery("#" + this.getFieldIds()[i])))
                result = false;

        return result;
    },

    addBlurEvent: function ($field) {
        var self = this;

        $field.blur(function () {
            self.validateField($field);
        });
    },

    addValueChangedEvent: function ($field) {
        var self = this;

        $field.change(function () {
            self.validateField($field);
        });
    }
});
