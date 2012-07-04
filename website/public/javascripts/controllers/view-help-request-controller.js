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

        this.$respondForm = jQuery("#respond-form");
        this.$respond = jQuery("#respond");

        this.$referenceForm = jQuery("#reference-form");
        this.$writeReference = jQuery("#write-reference");

        this.$respond.click(jQuery.proxy(this._toggleRespondForm, this));
        jQuery("#cancel-response-button").click(jQuery.proxy(this._collapseRespondForm, this));
        jQuery("#post-response-button").click(jQuery.proxy(this._doCreateResponse, this));
        this.$respondForm.submit(jQuery.proxy(this._doCreateResponse, this));

        this.$writeReference.click(jQuery.proxy(this._toggleReferenceForm, this));
        jQuery("#cancel-reference-button").click(jQuery.proxy(this._collapseReferenceForm, this));
        jQuery("#post-reference-button").click(jQuery.proxy(this._doCreateReference, this));
        this.$referenceForm.submit(jQuery.proxy(this._doCreateReference, this));
    },

    _initValidation: function () {
        jQuery(".field-error").hide();

        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "response-text"
            ]
        });
    },

    _toggleRespondForm: function(e) {
        e.preventDefault();

        if (this.$respond.hasClass("expanded"))
            this._collapseRespondForm(e);
        else {
            this.$respondForm.slideDown(200, "easeOutQuad");
            this.$respond.addClass("expanded");
        }
    },

    _collapseRespondForm: function(e) {
        e.preventDefault();

        this.$respondForm.slideUp(200, "easeInQuad");
        this.$respond.removeClass("expanded");
    },

    _toggleReferenceForm: function(e) {
        e.preventDefault();

        if (this.$writeReference.hasClass("expanded"))
            this._collapseReferenceForm(e);
        else {
            this.$referenceForm.slideDown(200, "easeOutQuad");
            this.$writeReference.addClass("expanded");
        }
    },

    _collapseReferenceForm: function(e) {
        e.preventDefault();

        this.$referenceForm.slideUp(200, "easeInQuad");
        this.$writeReference.removeClass("expanded");
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
    },

    _doCreateReference: function(e) {
        e.preventDefault();
    }
});
