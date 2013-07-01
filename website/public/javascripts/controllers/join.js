CBR.Controllers.Join = new Class({
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

        this.$languageSelect.val(this._getLanguageCode());

        this._initValidation();
        this._initEvents();

        this.$languageSelect.change(jQuery.proxy(this._changeLanguage, this));
    },

    _getLanguageCode: function () {
        return this.options.languageCode;
    },

    initElements: function () {
        this.parent();

        this.$usernameField = jQuery("#username");
        this.$emailField = jQuery("#email");
        this.$emailConfirmationField = jQuery("#email-confirmation");
        this.$languageSelect = jQuery("#language");

        this.$usernameTakenParagraph = jQuery("#username-taken");
        this.$emailAlreadyRegisteredParagraph = jQuery("#email-already-registered");
        this.$emailsDoNotMatchParagraph = jQuery("#emails-do-not-match");

        this.$submit = jQuery(".submit-wrapper > input");
        this.$submitProgress = this.$submit.siblings(".button-progress");
    },

    _initValidation: function () {
        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "first-name",
                "last-name",
                "email",
                "email-confirmation",
                "username",
                "password",
                "city",
                "country"
            ]
        });

        this.$usernameField.blur(jQuery.proxy(this._checkIfUsernameIsAlreadyTaken, this));
        this.$emailField.blur(jQuery.proxy(this._checkIfEmailIsAlreadyRegistered, this));
        this.$emailConfirmationField.blur(jQuery.proxy(this._checkIfEmailConfirmationMatches, this));
    },

    _initEvents: function () {
        jQuery("#check-username-availability").click(function(e) {
            e.preventDefault();
        });

        jQuery("form").submit(jQuery.proxy(this._doJoin, this));
    },

    _doJoin: function (e) {
        e.preventDefault();

        if (this.validator.isValid() &&
            this._isUsernameAvailable() &&
            this._isEmailNotRegisteredYet() &&
            this._isEmailConfirmationMatching()) {

            this.$submit.hide();
            this.$submitProgress.show();

            var user = new CBR.Models.User({
                username: this.$usernameField.val(),
                firstName: jQuery("#first-name").val(),
                lastName: jQuery("#last-name").val(),
                email: this.$emailField.val().toLowerCase(),
                password: jQuery("#password").val(),
                streetAddress: jQuery("#street-address").val(),
                postCode: jQuery("#post-code").val(),
                city: jQuery("#city").val(),
                countryId: jQuery("#country").val()
            });

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/users",
                data: CBR.JsonUtil.stringifyModel(user),
                onSuccess: function (responseText, responseXML) {
                    location.replace("/login?from=join&username=" + user.getUsername());
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).post();
        }
    },

    _checkIfUsernameIsAlreadyTaken: function () {
        this.$usernameTakenParagraph.slideUpCustom();

        if (this.$usernameField.val() !== "") {
            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/users/first?username=" + this.$usernameField.val(),
                onSuccess: function (responseText, responseXML) {
                    if (!_this.validator.isFlaggedInvalid(_this.$usernameField)) {
                        if (this.status === _this.httpStatusCode.noContent) {
                            _this.validator.flagValid(_this.$usernameField);
                        }
                        else {
                            _this.validator.flagInvalid(_this.$usernameField);
                            _this.$usernameTakenParagraph.slideDownCustom();
                        }
                    }
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).get();
        }
    },

    _checkIfEmailIsAlreadyRegistered: function () {
        this.$emailAlreadyRegisteredParagraph.slideUpCustom();

        if (this.$emailField.val() !== "") {
            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/users/first?email=" + this.$emailField.val().toLowerCase(),
                onSuccess: function (responseText, responseXML) {
                    if (this.status !== _this.httpStatusCode.noContent && !_this.validator.isFlaggedInvalid(_this.$emailField)) {
                        _this.validator.flagInvalid(_this.$emailField);
                        _this.$emailAlreadyRegisteredParagraph.slideDownCustom();
                    }
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).get();
        }
    },

    _checkIfEmailConfirmationMatches: function () {
        this.$emailsDoNotMatchParagraph.slideUpCustom();

        var email = this.$emailField.val();
        var emailConfirmation = this.$emailConfirmationField.val();

        if ((email !== "" || emailConfirmation !== "") &&
            email !== emailConfirmation &&
            !this.validator.isFlaggedInvalid(this.$emailConfirmationField)) {

            this.validator.flagInvalid(this.$emailConfirmationField);
            this.$emailsDoNotMatchParagraph.slideDownCustom();
        }
    },

    _isUsernameAvailable: function () {
        if (this.$usernameField.val() === "")
            return true;

        var xhr = new Request({
            urlEncoded: false,
            headers: { "Content-Type": "application/json" },
            async: false,
            url: "/api/users/first?username=" + this.$usernameField.val()
        }).get();

        var isAvailable = xhr.status === this.httpStatusCode.noContent;

        if (!isAvailable) {
            this.validator.flagInvalid(this.$usernameField);
            this.$usernameTakenParagraph.slideDownCustom();
        }

        return isAvailable;
    },

    _isEmailNotRegisteredYet: function () {
        if (this.$emailField.val() === "")
            return true;

        var xhr = new Request({
            urlEncoded: false,
            headers: { "Content-Type": "application/json" },
            async: false,
            url: "/api/users/first?email=" + this.$emailField.val().toLowerCase()
        }).get();

        var isNotRegistered = xhr.status === this.httpStatusCode.noContent;

        if (!isNotRegistered) {
            this.validator.flagInvalid(this.$emailField);
            this.$emailAlreadyRegisteredParagraph.slideDownCustom();
        }

        return isNotRegistered;
    },

    _isEmailConfirmationMatching: function () {
        var email = this.$emailField.val();
        var emailConfirmation = this.$emailConfirmationField.val();

        var isMatching = (email !== "" || emailConfirmation !== "") && email === emailConfirmation;

        if (!isMatching) {
            this.validator.flagInvalid(this.$emailConfirmationField);
            this.$emailsDoNotMatchParagraph.slideDownCustom();
        }

        return isMatching;
    },

    _changeLanguage: function (e) {
        e.preventDefault();

        var languageCode = e.currentTarget.value;

        location.href = "/join?lang=" + languageCode;
    }
});
