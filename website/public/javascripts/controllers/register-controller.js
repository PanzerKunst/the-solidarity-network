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

        jQuery("#register-anchor").click(this.doRegister);
    },

    doRegister: function (e) {
        e.preventDefault();

        var user = new CBR.User({
            username: jQuery("#username").val(),
            lastName: jQuery("#last-name").val(),
            firstName: jQuery("#first-name").val(),
            email: jQuery("#email").val(),
            password: jQuery("#password").val(),
            streetAddress: jQuery("#street-address").val(),
            postCode: jQuery("#post-code").val(),
            city: jQuery("#city").val(),
            countryId: jQuery('#country').val()
        });

        new Request({
            urlEncoded: false,
            url: "/api/user",
            data: CBR.JsonUtil.stringifyModel(user),
            onSuccess: function (responseText, responseXML) {
                console.log("Registration successful :)");
            },
            onFailure: function (xhr) {
                alert("AJAX fail :(");
            }
        }).post();
    }
});
