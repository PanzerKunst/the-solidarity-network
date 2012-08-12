CBR.Controllers.Join = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().append(
            Mustache.to_html(
                jQuery("#content-template").html(),
                this.options
            )
        );

        this._initElements();

        this.$languageSelect.val(this._getLanguageCode());

        this._initValidation();
        this._initEvents();

        this.$languageSelect.change(jQuery.proxy(this._changeLanguage, this));
    },

    _getLanguageCode: function () {
        return this.options.languageCode;
    },

    _initElements: function () {
        this.$usernameField = jQuery("#username");
        this.$emailField = jQuery("#email");
        this.$emailConfirmationField = jQuery("#email-confirmation");
        this.$languageSelect = jQuery("#language");

        this.$usernameTakenParagraph = jQuery("#username-taken");
        this.$emailAlreadyRegisteredParagraph = jQuery("#email-already-registered");
        this.$emailsDoNotMatchParagraph = jQuery("#emails-do-not-match");
    },

    _initValidation: function () {
        jQuery(".field-error").hide();
        this.$usernameTakenParagraph.hide();
        this.$emailAlreadyRegisteredParagraph.hide();

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
        jQuery("#join-button").click(jQuery.proxy(this._doJoin, this));
        jQuery("form").submit(jQuery.proxy(this._doJoin, this));
    },

    _doJoin: function (e) {
        e.preventDefault();

        if (this.validator.isValid() &&
            this._isUsernameAvailable() &&
            this._isEmailNotRegisteredYet() &&
            this._isEmailConfirmationMatching()) {
            var user = new CBR.Models.User({
                username: this.$usernameField.val(),
                firstName: jQuery("#first-name").val(),
                lastName: jQuery("#last-name").val(),
                email: this.$emailField.val().toLowerCase(),
                password: jQuery("#password").val(),
                streetAddress: jQuery("#street-address").val(),
                postCode: jQuery("#post-code").val(),
                city: jQuery("#city").val(),
                countryId: jQuery('#country').val()
            });

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/users",
                data: CBR.JsonUtil.stringifyModel(user),
                onSuccess: function (responseText, responseXML) {
                    location.replace("/login");
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).post();
        }
    },

    _checkIfUsernameIsAlreadyTaken: function (e) {
        e.preventDefault();

        this.$usernameTakenParagraph.slideUp(200, "easeInQuad");

        if (this.$usernameField.val() !== "") {
            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/users/first?username=" + this.$usernameField.val(),
                onSuccess: function (responseText, responseXML) {
                    if (this.status !== _this.httpStatusCode.noContent && !_this.validator.isFlaggedInvalid(_this.$usernameField)) {
                        _this.validator.flagInvalid(this.$usernameField);
                        _this.$usernameTakenParagraph.slideDown(200, "easeOutQuad");
                    }
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).get();
        }
    },

    _checkIfEmailIsAlreadyRegistered: function (e) {
        e.preventDefault();

        this.$emailAlreadyRegisteredParagraph.slideUp(200, "easeInQuad");

        if (this.$emailField.val() !== "") {
            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/users/first?email=" + this.$emailField.val().toLowerCase(),
                onSuccess: function (responseText, responseXML) {
                    if (this.status !== _this.httpStatusCode.noContent && !_this.validator.isFlaggedInvalid(_this.$emailField)) {
                        _this.validator.flagInvalid(this.$emailField);
                        _this.$emailAlreadyRegisteredParagraph.slideDown(200, "easeOutQuad");
                    }
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).get();
        }
    },

    _checkIfEmailConfirmationMatches: function (e) {
        e.preventDefault();

        this.$emailsDoNotMatchParagraph.slideUp(200, "easeInQuad");

        var email = this.$emailField.val();
        var emailConfirmation = this.$emailConfirmationField.val();

        if ((email !== "" || emailConfirmation !== "") &&
            email !== emailConfirmation &&
            !this.validator.isFlaggedInvalid(this.$emailConfirmationField)) {

            this.validator.flagInvalid(this.$emailConfirmationField);
            this.$emailsDoNotMatchParagraph.slideDown(200, "easeOutQuad");
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
            this.$usernameTakenParagraph.slideDown(200, "easeOutQuad");
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
            this.$emailAlreadyRegisteredParagraph.slideDown(200, "easeOutQuad");
        }

        return isNotRegistered;
    },

    _isEmailConfirmationMatching: function () {
        var email = this.$emailField.val();
        var emailConfirmation = this.$emailConfirmationField.val();

        var isMatching = (email !== "" || emailConfirmation !== "") && email === emailConfirmation;

        if (!isMatching) {
            this.validator.flagInvalid(this.$emailConfirmationField);
            this.$emailsDoNotMatchParagraph.slideDown(200, "easeOutQuad");
        }

        return isMatching;
    },

    _changeLanguage: function (e) {
        e.preventDefault();

        var languageCode = e.currentTarget.value;

        location.href = "/join?lang=" + languageCode;
    }
});
