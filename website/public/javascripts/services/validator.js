CBR.Services.Validator = new Class({
    checkEmpty: "empty",
    checkEmail: "email",
    checkUsername: "username",
    checkDateInFuture: "in-future",
    checkDateInMaxTwoWeeks: "in-max-2-weeks",

    initialize: function (options) {
        this.options = options;

        for (var i=0; i<this._getFieldIds().length; i++) {
            var $field = jQuery("#" + this._getFieldIds()[i]);
            this._addBlurEvent($field);
            this._addValueChangedEvent($field);
        }
    },

    isValid: function() {
        var result = true;

        for (var i=0; i<this._getFieldIds().length; i++)
            if (!this._validateField(jQuery("#" + this._getFieldIds()[i])))
                result = false;

        return result;
    },

    _getFieldIds: function() {
        return this.options.fieldIds;
    },

    _get$empty: function ($field) {
        return this._get$error($field, this.checkEmpty);
    },

    _get$email: function ($field) {
        return this._get$error($field, this.checkEmail);
    },

    _get$username: function ($field) {
        return this._get$error($field, this.checkUsername);
    },

    _get$inFuture: function ($field) {
        return this._get$error($field, this.checkDateInFuture);
    },

    _get$inMaxTwoWeeks: function ($field) {
        return this._get$error($field, this.checkDateInMaxTwoWeeks);
    },

    _get$error: function ($field, checkType) {
        return $field.parent().find("p[data-check=" + checkType + "]");
    },

    _isToCheckIfEmpty: function ($field) {
        return this._get$empty($field).length === 1;
    },

    _isToCheckIfEmail: function ($field) {
        return this._get$email($field).length === 1;
    },

    _isToCheckIfUsername: function ($field) {
        return this._get$username($field).length === 1;
    },

    _isToCheckIfInFuture: function ($field) {
        return this._get$inFuture($field).length === 1;
    },

    _isToCheckIfInMaxTwoWeeks: function ($field) {
        return this._get$inMaxTwoWeeks($field).length === 1;
    },

    _isValidEmail: function (email) {
        var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
        return reg.test(email);
    },

    _isValidUsername: function (username) {
        var reg = /^([A-Za-z0-9_\-])+$/;
        return reg.test(username);
    },

    _isInFuture: function (dateStr) {
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

    _isInMaxTwoWeeks: function (dateStr) {
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

    _validateField: function($field) {

        // Empty?
        if (this._isToCheckIfEmpty($field) && !$field.val()) {
            this._flagInvalid($field);
            this._get$empty($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this._get$empty($field).slideUp(200, "easeInQuad");

        // Email?
        if (this._isToCheckIfEmail($field) && !this._isValidEmail($field.val())) {
            this._flagInvalid($field);
            this._get$email($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this._get$email($field).slideUp(200, "easeInQuad");

        // Username?
        if (this._isToCheckIfUsername($field) && !this._isValidUsername($field.val())) {
            this._flagInvalid($field);
            this._get$username($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this._get$username($field).slideUp(200, "easeInQuad");

        // In the future?
        if (this._isToCheckIfInFuture($field) && !this._isInFuture($field.val())) {
            this._flagInvalid($field);
            this._get$inFuture($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this._get$inFuture($field).slideUp(200, "easeInQuad");

        // In max 2 weeks?
        if (this._isToCheckIfInMaxTwoWeeks($field) && !this._isInMaxTwoWeeks($field.val())) {
            this._flagInvalid($field);
            this._get$inMaxTwoWeeks($field).slideDown(200, "easeOutQuad");
            return false;
        }
        else
            this._get$inMaxTwoWeeks($field).slideUp(200, "easeInQuad");

        this._flagValid($field);

        return true;
    },

    _addBlurEvent: function ($field) {
        var self = this;

        $field.blur(function () {
            self._validateField($field);
        });
    },

    _addValueChangedEvent: function ($field) {
        var self = this;

        $field.change(function () {
            self._validateField($field);
        });
    },

    _flagValid: function($field) {
        var $wrapper = $field.parent();
        $wrapper.removeClass("invalid");
        $wrapper.addClass("valid");
    },

    _flagInvalid: function($field) {
        var $wrapper = $field.parent();
        $wrapper.removeClass("valid");
        $wrapper.addClass("invalid");
    }
});
