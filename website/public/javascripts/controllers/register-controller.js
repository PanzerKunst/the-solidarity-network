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
                    console.log("Registration successful :)");
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).post();
        }
    },

    _checkIfUsernameIsAlreadyTakenOnBlur: function () {
        var self = this;

        this.$usernameField.blur(function () {
            self.$usernameTakenParagraph.slideUp(200, "easeInQuad");

            if (self.$usernameField.val() !== "") {
                new Request({
                    urlEncoded: false,
                    url: "/api/users?username=" + self.$usernameField.val(),
                    onSuccess: function (responseText, responseXML) {

                        var $wrapper = self.$usernameField.parent();
                        $wrapper.removeClass("valid");
                        $wrapper.addClass("invalid");

                        self.$usernameTakenParagraph.slideDown(200, "easeOutQuad");
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
        var self = this;

        this.$emailField.blur(function () {
            self.$emailAlreadyRegisteredParagraph.slideUp(200, "easeInQuad");

            if (self.$emailField.val() !== "") {
                new Request({
                    urlEncoded: false,
                    url: "/api/users?email=" + self.$emailField.val().toLowerCase(),
                    onSuccess: function (responseText, responseXML) {

                        var $wrapper = self.$emailAlreadyRegisteredParagraph.parent();
                        $wrapper.removeClass("valid");
                        $wrapper.addClass("invalid");

                        self.$emailAlreadyRegisteredParagraph.slideDown(200, "easeOutQuad");
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
            url: "/api/users?username=" + this.$usernameField.val()
        }).get();

        return xhr.status === 404;
    },

    _isEmailNotRegisteredYet: function () {
        if (this.$emailField.val() === "")
            return true;

        var xhr = new Request({
            urlEncoded: false,
            async: false,
            url: "/api/users?email=" + this.$emailField.val().toLowerCase()
        }).get();

        return xhr.status === 404;
    }
});
