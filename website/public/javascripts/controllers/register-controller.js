CBR.Controllers.Register = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().html(
            Mustache.to_html(
                jQuery("#content-template").html(),
                this.options
            )
        );

        this.$usernameField = jQuery("#username");
        this.$emailField = jQuery("#email");
        this.$usernameTakenParagraph = jQuery("#username-taken");
        this.$emailAlreadyRegisteredParagraph = jQuery("#email-already-registered");

        this._initValidation();

        jQuery("#register-button").click(jQuery.proxy(this._doRegister, this));
        jQuery("form").submit(jQuery.proxy(this._doRegister, this));
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

        this._checkIfUsernameIsAlreadyTakenOnBlur();
        this._checkIfEmailIsAlreadyRegisteredOnBlur();
    },

    _doRegister: function (e) {
        e.preventDefault();

        if (this.validator.isValid() && this._isUsernameAvailable() && this._isEmailNotRegisteredYet()) {
            var user = new CBR.User({
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

    _checkIfUsernameIsAlreadyTakenOnBlur: function () {
        var _this = this;

        this.$usernameField.blur(function () {
            _this.$usernameTakenParagraph.slideUp(200, "easeInQuad");

            if (_this.$usernameField.val() !== "") {
                new Request({
                    urlEncoded: false,
                    url: "/api/users/first?username=" + _this.$usernameField.val(),
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
        });
    },

    _checkIfEmailIsAlreadyRegisteredOnBlur: function () {
        var _this = this;

        this.$emailField.blur(function () {
            _this.$emailAlreadyRegisteredParagraph.slideUp(200, "easeInQuad");

            if (_this.$emailField.val() !== "") {
                new Request({
                    urlEncoded: false,
                    url: "/api/users/first?email=" + _this.$emailField.val().toLowerCase(),
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
        });
    },

    _isUsernameAvailable: function () {
        if (this.$usernameField.val() === "")
            return true;

        var xhr = new Request({
            urlEncoded: false,
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
            async: false,
            url: "/api/users/first?email=" + this.$emailField.val().toLowerCase()
        }).get();

        return xhr.status === 404;
    }
});
