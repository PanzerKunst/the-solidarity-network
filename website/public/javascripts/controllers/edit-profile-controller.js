CBR.Controllers.EditProfile = new Class({
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
        this._fillForm();
        this._initPictureUpload();
        this._initEvents();

        this.$languageSelect.change(jQuery.proxy(this._changeLanguage, this));
    },

    _getLanguageCode: function () {
        return this.options.languageCode;
    },

    _getUser: function () {
        return this.options.user;
    },

    initElements: function () {
        this.parent();

        this.$indicationParagraph = jQuery(".indication");

        this.$tabLis = jQuery(".nav-tabs > li");
        this.$showProfileInfo = this.$tabLis.children("#show-profile-info");
        this.$showAccountInfo = this.$tabLis.children("#show-account-info");

        this.$profileInfoSection = jQuery("#profile-info");
        this.$accountInfoSection = jQuery("#account-info");

        this.$firstNameField = jQuery("#first-name");
        this.$lastNameField = jQuery("#last-name");
        this.$streetAddressField = jQuery("#street-address");
        this.$postCodeField = jQuery("#post-code");
        this.$cityField = jQuery("#city");
        this.$countryField = jQuery("#country");

        this.$changeProfilePic = jQuery("#change-profile-pic");
        this.$profilePic = jQuery("#profile-pic");
        this.$wrongExtensionParagraph = jQuery("#wrong-extension");
        this.$uploadFailedParagraph = jQuery("#upload-failed");

        this.$descriptionField = jQuery("#description");

        this.$emailField = jQuery("#email");
        this.$emailConfirmationWrapper = jQuery("#email-confirmation-field");
        this.$emailConfirmationField = jQuery("#email-confirmation");
        this.$passwordField = jQuery("#password");

        this.$languageSelect = jQuery("#language");

        this.$emailAlreadyRegisteredParagraph = jQuery("#email-already-registered");
        this.$emailsDoNotMatchParagraph = jQuery("#emails-do-not-match");
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

        this.$emailField.blur(jQuery.proxy(this._checkIfEmailIsNotYetRegisteredByAnotherUser, this));
        this.$emailConfirmationField.blur(jQuery.proxy(this._checkIfEmailConfirmationMatches, this));
    },

    _fillForm: function () {
        this.$firstNameField.val(this._getUser().firstName);
        this.$lastNameField.val(this._getUser().lastName);
        this.$streetAddressField.val(this._getUser().streetAddress);
        this.$postCodeField.val(this._getUser().postCode);
        this.$cityField.val(this._getUser().city);
        this.$countryField.val(this._getUser().country.id);

        this.$descriptionField.val(this._getUser().description);

        this.$emailField.val(this._getUser().email);
    },

    _initPictureUpload: function () {
        var _this = this;

        var options = {
            action: this.$changeProfilePic.data("action"),
            name: 'image',
            accept: "image/*",
            autoSubmit: true,
            onSubmit: function (file, extension) {
                _this.$wrongExtensionParagraph.slideUpCustom();
                _this.$uploadFailedParagraph.slideUpCustom();

                // Make sure its is one of the allowed file extensions
                var lowerCaseExtension = extension.toLowerCase();
                if (lowerCaseExtension !== "png" &&
                    lowerCaseExtension !== "jpg" &&
                    lowerCaseExtension !== "jpeg") {

                    _this.$wrongExtensionParagraph.slideDownCustom();
                    return false;
                }
            },
            onComplete: function (file, response) {
                if (file !== "")
                    _this.$profilePic.attr("src", "/files/profile-pic/" + _this._getUser().id + "?isTemp=true&time=" + new Date().getTime());
                else
                    _this.$uploadFailedParagraph.slideDownCustom();
            }
        };

        var ajaxUpload = new qq.AjaxUpload(this.$changeProfilePic, options);
    },

    _initEvents: function () {
        this.$showProfileInfo.click(jQuery.proxy(this._activateProfileInfoSection, this));
        this.$showAccountInfo.click(jQuery.proxy(this._activateAccountInfoSection, this));

        this.$emailField.keyup(jQuery.proxy(this._toggleEmailConfirmationField, this));

        jQuery("#save").click(jQuery.proxy(this._doSave, this));
        jQuery("form").submit(jQuery.proxy(this._doSave, this));
    },

    _activateProfileInfoSection: function () {
        this.$indicationParagraph.hide();

        this.$tabLis.removeClass("active");
        this.$showProfileInfo.parent().addClass("active");

        this.$profileInfoSection.show();
        this.$accountInfoSection.hide();
    },

    _activateAccountInfoSection: function () {
        this.$indicationParagraph.hide();

        this.$tabLis.removeClass("active");
        this.$showAccountInfo.parent().addClass("active");

        this.$accountInfoSection.show();
        this.$profileInfoSection.hide();
    },

    _toggleEmailConfirmationField: function (e) {
        if (CBR.Services.Keyboard.isPressedKeyText(e)) {
            if (this.$emailField.val().toLowerCase() === this._getUser().email && this.$emailConfirmationWrapper.is(":visible"))
                this.$emailConfirmationWrapper.slideUpCustom();

            else if (this.$emailField.val().toLowerCase() !== this._getUser().email && !this.$emailConfirmationWrapper.is(":visible"))
                this.$emailConfirmationWrapper.slideDownCustom();
        }
    },

    _doSave: function (e) {
        e.preventDefault();

        var _this = this;

        if (this._isFormValid() &&
            this._isEmailNotYetRegisteredByAnotherUser() &&
            this._isEmailConfirmationMatching()) {
            var user = new CBR.Models.User({
                firstName: this.$firstNameField.val(),
                lastName: this.$lastNameField.val(),
                streetAddress: jQuery("#street-address").val(),
                postCode: jQuery("#post-code").val(),
                city: jQuery("#city").val(),
                countryId: jQuery('#country').val(),
                description: jQuery('#description').val(),
                email: this.$emailField.val().toLowerCase()
            });

            var newPassword = this.$passwordField.val();
            if (newPassword !== "")
                user.setPassword(newPassword);

            new Request({
                urlEncoded: false,
                emulation: false, // Otherwise PUT and DELETE requests are sent as POST
                headers: { "Content-Type": "application/json" },
                url: "/api/users",
                data: CBR.JsonUtil.stringifyModel(user),
                onSuccess: function (responseText, responseXML) {
                    jQuery(window).scrollTop(0);
                    _this.$emailConfirmationWrapper.slideUpCustom();
                    _this.$indicationParagraph.slideDownCustom();
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).put();
        }
    },

    _isFormValid: function () {
        var isValid = this.validator.isValid();

        if (!isValid) {
            if (this.validator.isFlaggedInvalid(this.$firstNameField) ||
                this.validator.isFlaggedInvalid(this.$lastNameField) ||
                this.validator.isFlaggedInvalid(this.$cityField) ||
                this.validator.isFlaggedInvalid(this.$countryField))

                this._activateProfileInfoSection();

            else
                this._activateAccountInfoSection();
        }

        return isValid;
    },

    _checkIfEmailIsNotYetRegisteredByAnotherUser: function () {
        this.$emailAlreadyRegisteredParagraph.slideUpCustom();

        if (this.$emailField.val() !== "") {
            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/users/first?email=" + this.$emailField.val().toLowerCase() + "&notId=" + this._getUser().id,
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

    _isEmailNotYetRegisteredByAnotherUser: function () {
        if (this.$emailField.val() === "")
            return true;

        var xhr = new Request({
            urlEncoded: false,
            headers: { "Content-Type": "application/json" },
            async: false,
            url: "/api/users/first?email=" + this.$emailField.val().toLowerCase() + "&notId=" + this._getUser().id
        }).get();

        var isNotRegistered = xhr.status === this.httpStatusCode.noContent;

        if (!isNotRegistered && !this.validator.isFlaggedInvalid(this.$emailField)) {
            this._activateAccountInfoSection();

            this.validator.flagInvalid(this.$emailField);
            this.$emailAlreadyRegisteredParagraph.slideDownCustom();
        }

        return isNotRegistered;
    },

    _isEmailConfirmationMatching: function () {
        var email = this.$emailField.val();
        var emailConfirmation = this.$emailConfirmationField.val();

        var isMatching = this.$emailConfirmationWrapper.css("display") === "none" || email === emailConfirmation;

        if (!isMatching && !this.validator.isFlaggedInvalid(this.$emailConfirmationField)) {
            this._activateAccountInfoSection();

            this.validator.flagInvalid(this.$emailConfirmationField);
            this.$emailsDoNotMatchParagraph.slideDownCustom();
        }

        return isMatching;
    },

    _changeLanguage: function (e) {
        e.preventDefault();

        var languageCode = e.currentTarget.value;

        location.href = "/my-profile/edit?lang=" + languageCode;
    }
});
