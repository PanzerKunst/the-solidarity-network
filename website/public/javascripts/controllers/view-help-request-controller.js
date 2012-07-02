CBR.Controllers.ViewHelpRequest = new Class({
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

        this.$repondForm = jQuery("#respond-form");

        jQuery("#respond").click(jQuery.proxy(this._expandRespondForm, this));
        jQuery("#post-response-button").click(jQuery.proxy(this._doCreateResponse, this));
        this.$repondForm.submit(jQuery.proxy(this._doCreateResponse, this));
    },

    _initValidation: function () {
        jQuery(".field-error").hide();

        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "response-text"
            ]
        });
    },

    _expandRespondForm: function(e) {
        e.preventDefault();

        this.$repondForm.slideDown(200, "easeOutQuad");
    },

    _doCreateResponse: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            var helpResponse = new CBR.Models.HelpResponse({
                requestId: jQuery("#help-request").data("id"),
                text: jQuery("#response-text").val()
            });

            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/help-responses",
                data: CBR.JsonUtil.stringifyModel(helpResponse),
                onSuccess: function (responseText, responseXML) {
                    location.reload();
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
