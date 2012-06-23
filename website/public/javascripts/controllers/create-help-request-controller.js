CBR.Controllers.CreateHelpRequest = new Class({
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

        this._initValidation();

        jQuery("#submit-button").click(jQuery.proxy(this._doCreate, this));
        jQuery("form").submit(jQuery.proxy(this._doCreate, this));
    },

    _initValidation: function () {
        jQuery(".field-error").hide();

        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "title",
                "description",
                "expiry-date"
            ]
        });
    },

    _doCreate: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            var helpRequest = new CBR.Models.HelpRequest({
                title: jQuery("#title").val(),
                description: jQuery("#description").val(),
                expiryDate: jQuery("#expiry-date").val()
            });

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/help-requests",
                data: CBR.JsonUtil.stringifyModel(helpRequest),
                onSuccess: function (responseText, responseXML) {
                    location.replace("/help");
                },
                onFailure: function (xhr) {
                    if (xhr.status === 401)
                        location.replace("/login");
                    else
                        alert("AJAX fail :(");
                }
            }).post();
        }
    }
});
