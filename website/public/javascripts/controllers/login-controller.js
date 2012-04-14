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

        jQuery("#login-button").click(jQuery.proxy(this.doLogin, this));
        jQuery("form").submit(jQuery.proxy(this.doLogin, this));
    },

    doLogin: function (e) {
        e.preventDefault();

        var user = {
            password: jQuery("#password").val()
        };
        var usernameOrEmail = jQuery("#username-or-email").val();
        if (usernameOrEmail.indexOf("@") > 0)
            user.email = usernameOrEmail;
        else
            user.username = usernameOrEmail;

        new Request({
            urlEncoded: false,
            url: "/api/authenticate",
            data: JSON.stringify(user),
            onSuccess: function (responseText, responseXML) {
                location.replace("/home");
            },
            onFailure: function (xhr) {
                alert("AJAX fail :(");
            }
        }).post();
    }
});
