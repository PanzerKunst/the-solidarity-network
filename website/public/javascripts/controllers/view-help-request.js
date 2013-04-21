CBR.Controllers.ViewHelpRequest = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().append(
            Mustache.render(
                jQuery("#content-template").html(),
                {
                    helpRequest: this._generateHelpRequestForTemplate(),
                    helpResponses: this._generateHelpResponsesForTemplate()
                }
            )
        );

        this.initElements();
        this._initValidation();
        this._initEvents();
        this._initPills();
    },

    _getHelpRequest: function () {
        return this.options.helpRequest;
    },

    _getHelpResponses: function() {
        return this.options.helpResponses;
    },

    _getHelpRequester: function () {
        return this.options.helpRequest.requester;
    },

    initElements: function () {
        this.parent();

        this.$respondForm = jQuery("#respond-form");
        this.$respond = jQuery("#respond");

        this.$isSubscribingToFutureResponsesCheckbox = jQuery("#is-subscribing-to-future-responses");

        this.$expanded = jQuery(".expanded");

        this.$modals = jQuery(".modal");
        this.$confirmDeleteModal = jQuery("#confirm-delete-modal");
        this.$deletionImpossibleModal = jQuery("#deletion-impossible-modal");
    },

    _initValidation: function () {
        this.responseValidator = new CBR.Services.Validator({
            fieldIds: [
                "response-text"
            ]
        });
    },

    _initEvents: function () {
        jQuery("#delete").click(jQuery.proxy(this._showModal, this));
        jQuery("#cancel-delete").click(jQuery.proxy(this._hideModals, this));
        jQuery("#close-modal").click(jQuery.proxy(this._hideModals, this));
        jQuery("#confirm-delete").click(jQuery.proxy(this._doDeleteHelpRequest, this));

        this.$isSubscribingToFutureResponsesCheckbox.change(jQuery.proxy(this._changeSubscriptionToResponses, this));

        this.$respond.click(jQuery.proxy(this._toggleRespondForm, this));
        jQuery("#cancel-response").click(jQuery.proxy(this._collapseRespondForm, this));
        jQuery("#post-response").click(jQuery.proxy(this._doCreateResponse, this));
        this.$respondForm.submit(jQuery.proxy(this._doCreateResponse, this));
    },

    _initPills: function () {
        jQuery(".nav-pills a").click(jQuery.proxy(this.setActivePill, this));
    },

    _generateHelpRequestForTemplate: function () {
        var result = Object.clone(this._getHelpRequest());
        result.description = result.description.replace(/\n/g, "<br />");
        return result;
    },

    _generateHelpResponsesForTemplate: function () {
        var result = [];

        for (var i = 0; i < this._getHelpResponses().length; i++) {
            var helpResponse = Object.clone(this._getHelpResponses()[i]);
            helpResponse.creationDatetime = this.formatDatetime(helpResponse.creationDatetime);
            helpResponse.text = helpResponse.text.replace(/\n/g, "<br />");
            result.push(helpResponse);
        }

        return result;
    },

    _showModal: function (e) {
        e.preventDefault();

        if (this._getHelpResponses() !== undefined && this._getHelpResponses().length > 0)
            this.$deletionImpossibleModal.modal("show");
        else
            this.$confirmDeleteModal.modal("show");
    },

    _hideModals: function (e) {
        e.preventDefault();

        this.$modals.modal('hide');
    },

    _doDeleteHelpRequest: function(e) {
        e.preventDefault(e);

        // For the parsing by the backend to work
        this._getHelpRequest().requesterId = this._getHelpRequest().requester.id;

        // Jackson fails to parse "yyyy-MM-dd hh:mm:ss" for datetimes. "yyyy-MM-ddThh:mm:ss" works though.
        this._getHelpRequest().creationDatetime = this._getHelpRequest().creationDatetime.replace(/\s/, "T");

        var _this = this;

        new Request({
            urlEncoded: false,
            headers: { "Content-Type": "application/json" },
            emulation: false, // Otherwise PUT and DELETE requests are sent as POST
            url: "/api/help-requests",
            data: CBR.JsonUtil.stringifyModel(this._getHelpRequest()),
            onSuccess: function(responseText, responseXML) {
                location.replace("/help?from=helpRequestDeleted");
            },
            onFailure: function (xhr) {
                if (xhr.status === _this.httpStatusCode.unauthorized)
                    location.replace("/login");
                else
                    alert("AJAX fail :(");
            }
        }).DELETE();   // Lowercase triggers a jsHint error
    },

    _toggleRespondForm: function () {
        if (this.$respond.hasClass("expanded"))
            this._collapseRespondForm();
        else {
            this.$expanded.slideUpCustom();

            this.$respondForm.slideDownCustom();
            this.$respond.addClass("expanded");
        }
    },

    _collapseRespondForm: function () {
        this.$respondForm.slideUpCustom();
        this.$respond.removeClass("expanded");
    },

    _doCreateResponse: function (e) {
        e.preventDefault();

        if (this.responseValidator.isValid()) {
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

    _changeSubscriptionToResponses: function (e) {
        e.preventDefault();

        var subscription = new CBR.Models.SubscriptionToHelpResponses({
            requestId: this._getHelpRequest().id
        });

        var _this = this;

        var request = new Request({
            urlEncoded: false,
            headers: { "Content-Type": "application/json" },
            emulation: false, // Otherwise PUT and DELETE requests are sent as POST
            url: "/api/help-requests/subscribe-to-responses",
            data: CBR.JsonUtil.stringifyModel(subscription),
            onFailure: function (xhr) {
                if (xhr.status === _this.httpStatusCode.unauthorized)
                    location.replace("/login");
                else
                    alert("AJAX fail :(");
            }
        });

        if (this.$isSubscribingToFutureResponsesCheckbox.is(':checked'))
            request.post();
        else
            request.DELETE();   // Lowercase triggers a jsHint error
    }
});
