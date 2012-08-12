CBR.Controllers.Login = new Class({
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
        this._initValidation();
        this._initEvents();
    },

    _initElements: function() {
        this.$authFailed = jQuery("#auth-failed");
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

    _initEvents: function() {
        jQuery("#login-button").click(jQuery.proxy(this._doLogin, this));
        jQuery("form").submit(jQuery.proxy(this._doLogin, this));
    },

    _doLogin: function (e) {
        e.preventDefault();

        this.$authFailed.slideUp(200, "easeInQuad");

        if (this.validator.isValid()) {
            var user = {
                password: jQuery("#password").val()
            };
            var usernameOrEmail = jQuery("#username-or-email").val();
            if (usernameOrEmail.indexOf("@") > 0)
                user.email = usernameOrEmail.toLowerCase();
            else
                user.username = usernameOrEmail;

            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/authenticate",
                data: CBR.JsonUtil.stringifyModel(user),
                onSuccess: function (responseText, responseXML) {
                    if (this.status === _this.httpStatusCode.noContent)
                        _this.$authFailed.slideDown(200, "easeOutQuad");
                    else
                        location.href = "/home";
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).post();
        }
    }
});
