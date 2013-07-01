CBR.Controllers.CreateHelpRequest = new Class({
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
        this._initEvents();
    },

    initElements: function() {
        this.parent();

        this.$expiryDateField = jQuery("#expiry-date");

        this.$submit = jQuery(".submit-wrapper > input");
        this.$submitProgress = this.$submit.siblings(".button-progress");

        jQuery(".tooltip-link").tooltip();

        this._initExpiryDate();
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

    _initEvents: function() {
        jQuery("form").submit(jQuery.proxy(this._doCreate, this));
    },

    _initExpiryDate: function() {
        var inTwoWeeks = new Date();
        inTwoWeeks.setDate(inTwoWeeks.getDate() + 15);

        var yearToDisplay = inTwoWeeks.getFullYear();
        var monthToDisplay = (inTwoWeeks.getMonth() + 1).toString().pad(2, "0", "left");
        var dayToDisplay = inTwoWeeks.getDate().toString().pad(2, "0", "left");

        this.$expiryDateField.val(yearToDisplay + "-" + monthToDisplay + "-" + dayToDisplay);
    },

    _doCreate: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            this.$submit.hide();
            this.$submitProgress.show();

            var helpRequest = new CBR.Models.HelpRequest({
                title: jQuery("#title").val(),
                description: jQuery("#description").val(),
                expiryDate: this.$expiryDateField.val()
            });

            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/help-requests",
                data: CBR.JsonUtil.stringifyModel(helpRequest),
                onSuccess: function (responseText, responseXML) {
                    location.href = "/help-requests/" + responseText;
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
