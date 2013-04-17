CBR.Controllers.ViewMessage = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this._prepareDataForDisplay();

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

    _getMessage: function () {
        return this.options.message;
    },

    initElements: function () {
        this.parent();

        this.$replyForm = jQuery("#reply-form");
    },

    _initValidation: function () {
        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "text"
            ]
        });
    },

    _initEvents: function () {
        jQuery("#reply").click(jQuery.proxy(this._doCreateResponse, this));
        this.$replyForm.submit(jQuery.proxy(this._doCreateResponse, this));
    },

    _prepareDataForDisplay: function () {
        this._getMessage().creationDatetime = this.formatDatetime(this._getMessage().creationDatetime);
    },

    _doCreateResponse: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            var replyPrefix = "Re: ";
            var replyTitle = this._getMessage().title.startsWith(replyPrefix) ? this._getMessage().title : replyPrefix + this._getMessage().title;

            var reply = new CBR.Models.Message({
                toUserId: this._getMessage().fromUser.id,
                title: replyTitle,
                text: jQuery("#text").val(),
                replyToMessageId: this._getMessage().id
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
