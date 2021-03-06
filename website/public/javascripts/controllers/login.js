CBR.Controllers.Login = new Class({
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
        this._initValidation();
        this._fillForm();
        this._initEvents();
    },

    _getUsername: function () {
        return this.options.username;
    },

    _getTo: function () {
        return this.options.to;
    },

    initElements: function () {
        this.parent();

        this.$usernameOrEmail = jQuery("#username-or-email");
        this.$authFailed = jQuery("#auth-failed");

        this.$submit = jQuery(".submit-wrapper > input");
        this.$submitProgress = this.$submit.siblings(".button-progress");
    },

    _initValidation: function () {
        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "username-or-email",
                "password"
            ]
        });
    },

    _fillForm: function () {
        if (this._getUsername() !== undefined) {
            this.$usernameOrEmail.val(this._getUsername());
            this.$usernameOrEmail.focus();
        }
    },

    _initEvents: function () {
        jQuery("form").submit(jQuery.proxy(this._doLogin, this));
    },

    _doLogin: function (e) {
        e.preventDefault();

        this.$authFailed.slideUpCustom();

        if (this.validator.isValid()) {
            this.$submit.hide();
            this.$submitProgress.show();

            var user = {
                password: jQuery("#password").val()
            };
            var usernameOrEmail = this.$usernameOrEmail.val();
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
                    if (this.status === _this.httpStatusCode.noContent) {
                        _this.$submitProgress.hide();
                        _this.$submit.show();
                        _this.$authFailed.slideDownCustom();
                    } else if (_this._getTo() === "messages") {
                        location.href = "/messages";
                    }
                    else {
                        location.href = "/home";
                    }
                },
                onFailure: function (xhr) {
                    alert("AJAX fail :(");
                }
            }).post();
        }
    }
});
