CBR.Controllers.Register = new Class({
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

        this.$usernameField = jQuery("#username");
        this.$emailField = jQuery("#email");
        this.$languageSelect = jQuery("#language");
        this.$usernameTakenParagraph = jQuery("#username-taken");
        this.$emailAlreadyRegisteredParagraph = jQuery("#email-already-registered");

        this.$languageSelect.val(this._getLanguageCode());

        this._initValidation();

        jQuery("#register-button").click(jQuery.proxy(this._doRegister, this));
        jQuery("form").submit(jQuery.proxy(this._doRegister, this));

        this.$languageSelect.change(jQuery.proxy(this._changeLanguage, this));
    },

    _getLanguageCode: function () {
        return this.options.languageCode;
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
    },

    _doRegister: function (e) {
        e.preventDefault();

        if (this.validator.isValid() && this._isUsernameAvailable() && this._isEmailNotRegisteredYet()) {
            var user = new CBR.Models.User({
                username: this.$usernameField.val(),
                lastName: jQuery("#last-name").val(),
                firstName: jQuery("#first-name").val(),
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

                    var $wrapper = _this.$usernameField.parent();
                    $wrapper.removeClass("valid");
                    $wrapper.addClass("invalid");

                    _this.$usernameTakenParagraph.slideDown(200, "easeOutQuad");
                },
                onFailure: function (xhr) {
                    if (xhr.status === 404)
                        console.log("username available :)");
                    else
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

                    var $wrapper = _this.$emailAlreadyRegisteredParagraph.parent();
                    $wrapper.removeClass("valid");
                    $wrapper.addClass("invalid");

                    _this.$emailAlreadyRegisteredParagraph.slideDown(200, "easeOutQuad");
                },
                onFailure: function (xhr) {
                    if (xhr.status === 404)
                        console.log("email not registered yet :)");
                    else
                        alert("AJAX fail :(");
                }
            }).get();
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

        return xhr.status === 404;
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

        return xhr.status === 404;
    },

    _changeLanguage: function(e) {
        e.preventDefault();

        var languageCode = e.currentTarget.value;

        location.replace("/register?lang=" + languageCode);
    }
});
