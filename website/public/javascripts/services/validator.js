CBR.Services.Validator = new Class({
    checkEmpty: "empty",
    checkEmail: "email",
    checkUsername: "username",
    checkDateInFuture: "in-future",
    checkDateInMaxTwoWeeks: "in-max-2-weeks",
    checkLongEnough: "long-enough",

    initialize: function (options) {
        this.options = options;

        for (var i = 0; i < this._getFieldIds().length; i++) {
            var $field = jQuery("#" + this._getFieldIds()[i]);

            if ($field.hasClass("pills"))
                this._addClickEvents($field);
            else {
                this._addBlurEvent($field);
                this._addValueChangedEvent($field);
            }
        }
    },

    isValid: function () {
        var result = true;

        for (var i = 0; i < this._getFieldIds().length; i++)
            if (!this._validateField(jQuery("#" + this._getFieldIds()[i]), false))
                result = false;

        return result;
    },

    flagValid: function ($field) {
        var $wrapper = $field.parent();
        $wrapper.removeClass("invalid");
        $wrapper.addClass("valid");
    },

    flagInvalid: function ($field) {
        var $wrapper = $field.parent();
        $wrapper.removeClass("valid");
        $wrapper.addClass("invalid");
    },

    isFlaggedInvalid: function ($field) {
        return $field.parent().hasClass("invalid");
    },

    _getFieldIds: function () {
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

    _get$longEnough: function ($field) {
        return this._get$error($field, this.checkLongEnough);
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

    _isToCheckIfLongEnough: function ($field) {
        return this._get$longEnough($field).length === 1;
    },

    _isValidEmail: function (email) {
        if (email === "")
            return true;

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

        var date = new Date(year, month - 1, day);
        var now = new Date();

        var oneDayInMillis = 1000 * 60 * 60 * 24;
        var nbDaysDifference = Math.ceil((date - now) / oneDayInMillis);

        return nbDaysDifference > 0;
    },

    _isInMaxTwoWeeks: function (dateStr) {
        var yearMonthDay = dateStr.split("-");
        var year = parseInt(yearMonthDay[0], 10);
        var month = parseInt(yearMonthDay[1], 10);
        var day = parseInt(yearMonthDay[2], 10);

        var date = new Date(year, month - 1, day);
        var inTwoWeeks = new Date();
        inTwoWeeks.setDate(inTwoWeeks.getDate() + 14);

        var oneDayInMillis = 1000 * 60 * 60 * 24;
        var nbDaysDifference = Math.ceil((inTwoWeeks - date) / oneDayInMillis);

        return nbDaysDifference >= 0;
    },

    _isLongEnough: function(value, minLength) {
        return value.length >= minLength;
    },

    _validateField: function ($field, isOnBlur) {

        // Empty?
        if (this._isToCheckIfEmpty($field)) {
            if ($field.hasClass("nav-pills") &&
                $field.children(".active").length === 0) {

                this.flagInvalid($field);
                this._get$empty($field).slideDownCustom();
                return false;
            }
            if (!$field.hasClass("nav-pills") &&
                !$field.val().trim()) {

                if (!isOnBlur) {
                    this.flagInvalid($field);
                    this._get$empty($field).slideDownCustom();
                }
                return false;
            }

            this._get$empty($field).slideUpCustom();
        }

        // Email?
        if (this._isToCheckIfEmail($field)) {
            if (!this._isValidEmail($field.val())) {
                this.flagInvalid($field);
                this._get$email($field).slideDownCustom();
                return false;
            }

            this._get$email($field).slideUpCustom();
        }

        // Username?
        if (this._isToCheckIfUsername($field)) {
            if (!this._isValidUsername($field.val())) {
                this.flagInvalid($field);
                this._get$username($field).slideDownCustom();
                return false;
            }
            this._get$username($field).slideUpCustom();
        }

        // In the future?
        if (this._isToCheckIfInFuture($field)) {
            if (!this._isInFuture($field.val())) {
                this.flagInvalid($field);
                this._get$inFuture($field).slideDownCustom();
                return false;
            }
            this._get$inFuture($field).slideUpCustom();
        }

        // In max 2 weeks?
        if (this._isToCheckIfInMaxTwoWeeks($field)) {
            if (!this._isInMaxTwoWeeks($field.val())) {
                this.flagInvalid($field);
                this._get$inMaxTwoWeeks($field).slideDownCustom();
                return false;
            }
            this._get$inMaxTwoWeeks($field).slideUpCustom();
        }

        // Long enough?
        if (this._isToCheckIfLongEnough($field)) {
            if (!this._isLongEnough($field.val(), $field.data("min-length"))) {
                this.flagInvalid($field);
                this._get$longEnough($field).slideDownCustom();
                return false;
            }
            this._get$longEnough($field).slideUpCustom();
        }

        this.flagValid($field);

        return true;
    },

    _addBlurEvent: function ($field) {
        var _this = this;

        $field.blur(function () {
            _this._validateField($field, true);
        });
    },

    _addValueChangedEvent: function ($field) {
        var _this = this;

        $field.change(function () {
            _this._validateField($field);
        });
    },

    _addClickEvents: function ($field) {
        var _this = this;

        $field.find("a").bind("active-toggled", function () {
            _this._validateField($field);
        });
    }
});
