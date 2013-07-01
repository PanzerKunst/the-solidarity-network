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
                    helpReplies: this._generateHelpRepliesForTemplate(),
                    isSubscribedToReplies: this._getIsSubscribedToReplies()
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

    _getHelpReplies: function() {
        return this.options.helpReplies;
    },

    _getIsSubscribedToReplies: function () {
        return this.options.isSubscribedToReplies;
    },

    initElements: function () {
        this.parent();

        this.$replyForm = jQuery("#reply-form");
        this.$reply = jQuery("#reply");

        this.$isSubscribingToFutureRepliesCheckbox = jQuery("#is-subscribing-to-future-replies");

        this.$expanded = jQuery(".expanded");

        this.$postReply = jQuery("#post-reply");
        this.$postReplyProgress = this.$postReply.siblings(".button-progress");

        this.$modals = jQuery(".modal");
        this.$confirmDeleteModal = jQuery("#confirm-delete-modal");
        this.$deletionImpossibleModal = jQuery("#deletion-impossible-modal");

        this.$confirmDelete = jQuery("#confirm-delete");
        this.$confirmDeleteProgress = this.$confirmDelete.siblings(".button-progress");
    },

    _initValidation: function () {
        this.replyValidator = new CBR.Services.Validator({
            fieldIds: [
                "reply-text"
            ]
        });
    },

    _initEvents: function () {
        jQuery("#delete").click(jQuery.proxy(this._showModal, this));
        jQuery("#cancel-delete").click(jQuery.proxy(this._hideModals, this));
        jQuery("#close-modal").click(jQuery.proxy(this._hideModals, this));
        this.$confirmDelete.click(jQuery.proxy(this._doDeleteHelpRequest, this));

        this.$isSubscribingToFutureRepliesCheckbox.change(jQuery.proxy(this._changeSubscriptionToReplies, this));

        this.$reply.click(jQuery.proxy(this._toggleReplyForm, this));
        jQuery("#cancel-reply").click(jQuery.proxy(this._collapseReplyForm, this));
        this.$postReply.click(jQuery.proxy(this._doCreateReply, this));
        this.$replyForm.submit(jQuery.proxy(this._doCreateReply, this));
    },

    _initPills: function () {
        jQuery(".nav-pills a").click(jQuery.proxy(this.setActivePill, this));
    },

    _generateHelpRequestForTemplate: function () {
        var result = Object.clone(this._getHelpRequest());
        result.description = result.description.replace(/\n/g, "<br />");
        return result;
    },

    _generateHelpRepliesForTemplate: function () {
        var result = [];

        for (var i = 0; i < this._getHelpReplies().length; i++) {
            var helpReply = Object.clone(this._getHelpReplies()[i]);
            helpReply.creationDatetime = this.formatDatetime(helpReply.creationDatetime);
            helpReply.text = helpReply.text.replace(/\n/g, "<br />");
            result.push(helpReply);
        }

        return result;
    },

    _showModal: function (e) {
        e.preventDefault();

        if (this._getHelpReplies() !== undefined && this._getHelpReplies().length > 0)
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

        this.$confirmDelete.hide();
        this.$confirmDeleteProgress.show();

        // For the parsing by the backend to work
        this._getHelpRequest().requesterId = this._getHelpRequest().requester.id;

        // The object cannot have extra properties
        if (this._getHelpRequest().requester !== undefined) {
            delete this._getHelpRequest().requester;
        }

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

    _toggleReplyForm: function () {
        if (this.$reply.hasClass("expanded"))
            this._collapseReplyForm();
        else {
            this.$expanded.slideUpCustom();

            this.$replyForm.slideDownCustom();
            this.$reply.addClass("expanded");
        }
    },

    _collapseReplyForm: function () {
        this.$replyForm.slideUpCustom();
        this.$reply.removeClass("expanded");
    },

    _doCreateReply: function (e) {
        e.preventDefault();

        if (this.replyValidator.isValid()) {
            this.$postReply.hide();
            this.$postReplyProgress.show();

            var helpReply = new CBR.Models.HelpReply({
                requestId: jQuery("#help-request").data("id"),
                text: jQuery("#reply-text").val()
            });

            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/help-replies",
                data: CBR.JsonUtil.stringifyModel(helpReply),
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

    _changeSubscriptionToReplies: function (e) {
        e.preventDefault();

        var subscription = new CBR.Models.SubscriptionToHelpReplies({
            requestId: this._getHelpRequest().id
        });

        var _this = this;

        var request = new Request({
            urlEncoded: false,
            headers: { "Content-Type": "application/json" },
            emulation: false, // Otherwise PUT and DELETE requests are sent as POST
            url: "/api/help-requests/subscribe-to-replies",
            data: CBR.JsonUtil.stringifyModel(subscription),
            onFailure: function (xhr) {
                if (xhr.status === _this.httpStatusCode.unauthorized)
                    location.replace("/login");
                else
                    alert("AJAX fail :(");
            }
        });

        if (this.$isSubscribingToFutureRepliesCheckbox.is(':checked'))
            request.post();
        else
            request.DELETE();   // Lowercase triggers a jsHint error
    }
});
