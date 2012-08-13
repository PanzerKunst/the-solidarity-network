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
        this._initEvents();
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

    _initEvents: function() {
        jQuery("#submit-button").click(jQuery.proxy(this._doCreate, this));
        jQuery("form").submit(jQuery.proxy(this._doCreate, this));
    },

    _doCreate: function () {
        if (this.validator.isValid()) {
            var helpRequest = new CBR.Models.HelpRequest({
                title: jQuery("#title").val(),
                description: jQuery("#description").val(),
                expiryDate: jQuery("#expiry-date").val()
            });

            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/help-requests",
                data: CBR.JsonUtil.stringifyModel(helpRequest),
                onSuccess: function (responseText, responseXML) {
                    location.href = "/help";
                },
                onFailure: function (xhr) {
                    if (xhr.status === _this.httpStatusCode.unauthorized)
                        location.replace("/login");
                    else
                        alert("AJAX fail :(");
                }
            }).post();
        }
    }
});
