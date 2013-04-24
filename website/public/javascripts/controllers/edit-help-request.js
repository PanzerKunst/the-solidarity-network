CBR.Controllers.EditHelpRequest = new Class({
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

    _getHelpRequest: function () {
        return this.options.helpRequest;
    },

    initElements: function () {
        this.parent();

        this.$titleField = jQuery("#title");
        this.$descriptionField = jQuery("#description");
        this.$expiryDateField = jQuery("#expiry-date");
    },

    _initValidation: function () {
        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "title",
                "description",
                "expiry-date"
            ]
        });
    },

    _fillForm: function () {
        this.$titleField.val(this._getHelpRequest().title);
        this.$descriptionField.val(this._getHelpRequest().description);
        this.$expiryDateField.val(this._getHelpRequest().expiryDate);
    },

    _initEvents: function () {
        jQuery("form").submit(jQuery.proxy(this._doSave, this));
    },

    _doSave: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            this._getHelpRequest().title = this.$titleField.val();
            this._getHelpRequest().description = this.$descriptionField.val();
            this._getHelpRequest().expiryDate = this.$expiryDateField.val();

            // For the parsing by the backend to work
            this._getHelpRequest().requesterId = this._getHelpRequest().requester.id;

            // Jackson fails to parse "yyyy-MM-dd hh:mm:ss" for datetimes. "yyyy-MM-ddThh:mm:ss" works though.
            this._getHelpRequest().creationDatetime = this._getHelpRequest().creationDatetime.replace(/\s/, "T");

            var _this = this;

            new Request({
                urlEncoded: false,
                emulation: false, // Otherwise PUT and DELETE requests are sent as POST
                headers: { "Content-Type": "application/json" },
                url: "/api/help-requests",
                data: CBR.JsonUtil.stringifyModel(this._getHelpRequest()),
                onSuccess: function (responseText, responseXML) {
                    location.href = "/help-requests/" + _this._getHelpRequest().id;
                },
                onFailure: function (xhr) {
                    if (xhr.status === _this.httpStatusCode.unauthorized)
                        location.replace("/login");
                    else
                        alert("AJAX fail :(");
                }
            }).put();
        }
    }
});
