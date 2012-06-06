CBR.Controllers.Login = new Class({
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

        this._initValidation();

        jQuery("#login-button").click(jQuery.proxy(this._doLogin, this));
        jQuery("form").submit(jQuery.proxy(this._doLogin, this));
    },

    _initValidation: function () {
        jQuery(".field-error").hide();

        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "username-or-email",
                "password"
            ]
        });
    },

    _doLogin: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            var user = {
                password: jQuery("#password").val()
            };
            var usernameOrEmail = jQuery("#username-or-email").val();
            if (usernameOrEmail.indexOf("@") > 0)
                user.email = usernameOrEmail.toLowerCase();
            else
                user.username = usernameOrEmail;

            new Request({
                urlEncoded: false,
                url: "/api/authenticate",
                data: CBR.JsonUtil.stringifyModel(user),
                onSuccess: function (responseText, responseXML) {
                    location.replace("/home");
                },
                onFailure: function (xhr) {
                    jQuery("#auth-failed").show();
                }
            }).post();
        }
    }
});
