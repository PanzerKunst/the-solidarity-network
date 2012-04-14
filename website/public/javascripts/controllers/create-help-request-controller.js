CBR.Controllers.CreateHelpRequest = new Class({
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

        this.initValidation();

        jQuery("#submit-button").click(jQuery.proxy(this.doCreate, this));
        jQuery("form").submit(jQuery.proxy(this.doCreate, this));
    },

    initValidation: function () {
        jQuery(".field-error").hide();

        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "description",
                "expiry-date"
            ]
        });
    },

    doCreate: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            var helpRequest = new CBR.HelpRequest({
                description: jQuery("#description").val(),
                expiryDate: jQuery("#expiry-date").val()
            });

            new Request({
                urlEncoded: false,
                url: "/api/help-requests",
                data: CBR.JsonUtil.stringifyModel(helpRequest),
                onSuccess: function (responseText, responseXML) {
                    console.log("Request saved :)");
                },
                onFailure: function (xhr) {
                    if (xhr.status === 401)
                        alert("Seems like you are not logged-in. Please log in again and retry, thanks!");
                    else
                        alert("AJAX fail :(");
                }
            }).post();
        }
    }
});
