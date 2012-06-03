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

        this.initValidation();

        jQuery("#register-button").click(jQuery.proxy(this.doRegister, this));
        jQuery("form").submit(jQuery.proxy(this.doRegister, this));
    },

    initValidation: function() {
        jQuery(".field-error").hide();
        jQuery("#username-taken").hide();

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

        this.checkIfUsernameIsAlreadyTakenOnBlur();
        this.checkIfEmailIsAlreadyRegisteredOnBlur();
    },

    doRegister: function (e) {
        e.preventDefault();

        if (this.validator.isValid() && this.isUsernameAvailable() && this.isEmailNotRegisteredYet()) {
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

    checkIfUsernameIsAlreadyTakenOnBlur: function() {
        var self = this;

        this.$usernameField.blur(function () {
            self.$usernameTakenParagraph.slideUp(200, "easeInQuad");

            new Request({
                urlEncoded: false,
                url: "/api/users?username=" + self.$usernameField.val(),
                onSuccess: function (responseText, responseXML) {
                    self.$usernameTakenParagraph.slideDown(200, "easeOutQuad");
                },
                onFailure: function (xhr) {
                    if (xhr.status === 404)
                        console.log("username available :)");
                    else
                        alert("AJAX fail :(");
                }
            }).get();

        });
    },

    checkIfEmailIsAlreadyRegisteredOnBlur: function() {
        var self = this;

        this.$emailField.blur(function () {
            self.$emailAlreadyRegisteredParagraph.slideUp(200, "easeInQuad");

            new Request({
                urlEncoded: false,
                url: "/api/users?email=" + self.$emailField.val().toLowerCase(),
                onSuccess: function (responseText, responseXML) {
                    self.$emailAlreadyRegisteredParagraph.slideDown(200, "easeOutQuad");
                },
                onFailure: function (xhr) {
                    if (xhr.status === 404)
                        console.log("email not registered yet :)");
                    else
                        alert("AJAX fail :(");
                }
            }).get();

        });
    },

    isUsernameAvailable: function() {
        var xhr = new Request({
            urlEncoded: false,
            async: false,
            url: "/api/users?username=" + this.$usernameField.val()
        }).get();

        return xhr.status === 404;
    },

    isEmailNotRegisteredYet: function() {
        var xhr = new Request({
            urlEncoded: false,
            async: false,
            url: "/api/users?email=" + this.$emailField.val().toLowerCase()
        }).get();

        return xhr.status === 404;
    }
});
