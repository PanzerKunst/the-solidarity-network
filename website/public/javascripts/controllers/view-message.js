CBR.Controllers.ViewMessage = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().append(
            Mustache.render(
                jQuery("#content-template").html(),
                {
                    message: this._generateMessageForTemplate(),
                    replies: this._generateRepliesForTemplate()
                }
            )
        );

        this.initElements();
        this._initValidation();
        this._initEvents();

        this._scrollToReply();
    },

    _getMessage: function () {
        return this.options.message;
    },

    _getReplies: function () {
        return this.options.replies;
    },

    _getLoggedInUser: function () {
        return this.options.loggedInUser;
    },

    initElements: function () {
        this.parent();

        this.$replyForm = jQuery("form");
    },

    _initValidation: function () {
        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "text"
            ]
        });
    },

    _initEvents: function () {
        this.$replyForm.submit(jQuery.proxy(this._doCreateReply, this));
    },

    _scrollToReply: function () {
        var replyIdFromUrl = this._getReplyIdFromUrl();

        if (replyIdFromUrl !== -1) {
            setTimeout(function () {    // We need to wait for images to be added to the DOM (at least on Chrome)
                jQuery("html, body").animate({
                    scrollTop: jQuery('article[data-reply-id="' + replyIdFromUrl + '"]').offset().top
                }, 0);
            }, 300);
        }
    },

    _getReplyIdFromUrl: function () {
        var messageIdUrlIndex = document.URL.lastIndexOf("/") + 1;
        var messageIdInUrl = parseInt(document.URL.substring(messageIdUrlIndex), 10);

        return messageIdInUrl !== this._getMessage().id ? messageIdInUrl : -1;
    },

    _generateMessageForTemplate: function () {
        var result = Object.clone(this._getMessage());

        result.creationDatetime = this.formatDatetime(this._getMessage().creationDatetime);
        result.text = result.text.replace(/\n/g, "<br />");

        return result;
    },

    _generateRepliesForTemplate: function () {
        var result = [];

        for (var i = 0; i < this._getReplies().length; i++) {
            var reply = Object.clone(this._getReplies()[i]);
            reply.creationDatetime = this.formatDatetime(reply.creationDatetime);
            reply.text = reply.text.replace(/\n/g, "<br />");
            result.push(reply);
        }

        return result;
    },

    _doCreateReply: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            var originalTitle = this._getMessage().title !== null ? this._getMessage().title : "";
            var replyPrefix = "Re: ";
            var replyTitle = originalTitle.startsWith(replyPrefix) ? originalTitle : replyPrefix + originalTitle;

            var toUserId = this._getMessage().fromUser.id;
            if (toUserId === this._getLoggedInUser().id) {
                toUserId = this._getMessage().toUser.id;
            }

            var reply = new CBR.Models.Message({
                toUserId: toUserId,
                title: replyTitle,
                text: jQuery("#text").val(),
                replyToMessageId: this._getMessage().replyToMessageId ? this._getMessage().replyToMessageId : this._getMessage().id
            });

            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/messages",
                data: CBR.JsonUtil.stringifyModel(reply),
                onSuccess: function (responseText, responseXML) {
                    location.href = "/messages?from=reply";
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
