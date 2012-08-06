CBR.Controllers.EditProfile = new Class({
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
        this._fillForm();
        this._initEvents();

        this.$languageSelect.change(jQuery.proxy(this._changeLanguage, this));
    },

    _getLanguageCode: function () {
        return this.options.languageCode;
    },

    _getUser: function() {
        return this.options.user;
    },

    _initElements: function() {
        this.$profileInfoSection = jQuery("#profile-info");
        this.$accountInfoSection = jQuery("#account-info");

        this.$firstNameField = jQuery("#first-name");
        this.$lastNameField = jQuery("#last-name");
        this.$streetAddressField = jQuery("#street-address");
        this.$postCodeField = jQuery("#post-code");
        this.$cityField = jQuery("#city");
        this.$countryField = jQuery("#country");

        this.$descriptionField = jQuery("#description");

        this.$emailField = jQuery("#email");
        this.$emailConfirmationField = jQuery("#email-confirmation");
        this.$passwordField = jQuery("#password");

        this.$languageSelect = jQuery("#language");

        this.$emailsDoNotMatchParagraph = jQuery("#emails-do-not-match");
    },

    _initValidation: function () {
        jQuery(".field-error").hide();

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

        this.$emailConfirmationField.blur(jQuery.proxy(this._checkIfEmailConfirmationMatches, this));
    },

    _fillForm: function() {
        this.$firstNameField.val(this._getUser().firstName);
        this.$lastNameField.val(this._getUser().lastName);
        this.$streetAddressField.val(this._getUser().streetAddress);
        this.$postCodeField.val(this._getUser().postCode);
        this.$cityField.val(this._getUser().city);
        this.$countryField.val(this._getUser().country.id);

        this.$descriptionField.val(this._getUser().description);

        this.$emailField.val(this._getUser().email);
    },

    _initEvents: function() {
        jQuery("#show-profile-info").click(jQuery.proxy(this._activateProfileInfoSection, this));
        jQuery("#show-account-info").click(jQuery.proxy(this._activateAccountInfoSection, this));
        jQuery("#save-button").click(jQuery.proxy(this._doSave, this));
        jQuery("form").submit(jQuery.proxy(this._doSave, this));
    },

    _activateProfileInfoSection: function (e) {
        e.preventDefault();

        this.$profileInfoSection.show();
        this.$accountInfoSection.hide();
    },

    _activateAccountInfoSection: function (e) {
        e.preventDefault();

        this.$accountInfoSection.show();
        this.$profileInfoSection.hide();
    },

    _doSave: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
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

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/users",
                data: CBR.JsonUtil.stringifyModel(user),
                onSuccess: function (responseText, responseXML) {
                    alert("saved!");
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).put();
        }
    },

    _checkIfEmailConfirmationMatches: function (e) {
        e.preventDefault();

        this.$emailsDoNotMatchParagraph.slideUp(200, "easeInQuad");

        var email = this.$emailField.val();
        var emailConfirmation = this.$emailConfirmationField.val();

        if ((email !== "" || emailConfirmation !== "") && email !== emailConfirmation) {
            var $wrapper = this.$emailsDoNotMatchParagraph.parent();
            $wrapper.removeClass("valid");
            $wrapper.addClass("invalid");

            this.$emailsDoNotMatchParagraph.slideDown(200, "easeOutQuad");
        }
    },

    _changeLanguage: function (e) {
        e.preventDefault();

        var languageCode = e.currentTarget.value;

        location.href = "/join?lang=" + languageCode;
    }
});
