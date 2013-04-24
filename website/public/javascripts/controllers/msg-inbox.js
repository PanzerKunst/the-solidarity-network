CBR.Controllers.MsgInbox = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().append(
            Mustache.render(
                jQuery("#content-template").html(),
                { inboxMessages: this._generateInboxMessagesForTemplate() }
            )
        );

        this.initElements();
        this._showInboxMessages();
        this._initEvents();
    },

    _getInboxMessages: function () {
        return this.options.inboxMessages;
    },

    initElements: function () {
        this.parent();

        this.$listContainer = jQuery("#list-container");
        this.$listTemplate = jQuery("#list-template");
    },

    _initEvents: function () {
        jQuery(".clickable").click(jQuery.proxy(this._navigateToMessage, this));
    },

    _showInboxMessages: function () {
        this.$listContainer.html(
            Mustache.render(
                this.$listTemplate.html(),
                { inboxMessages: this._formatDates(this._getInboxMessages()) }
            )
        );
    },

    _navigateToMessage: function (e) {
        e.preventDefault();

        var msgId = jQuery(e.currentTarget).data("id");

        if (msgId !== undefined)
            location.href = "/messages/" + msgId;
    },

    _formatDates: function (messages) {
        for (var i = 0; i < messages.length; i++) {
            var currentMessage = messages[i];

            currentMessage.creationDatetime = this.formatDate(currentMessage.creationDatetime);
        }

        return messages;
    },

    _generateInboxMessagesForTemplate: function () {
        var result = [];

        var maxChars = 75;

        for (var i = 0; i < this._getInboxMessages().length; i++) {
            var message = this._getInboxMessages()[i];
            if (message.title === null && message.text.length > maxChars) {
                message.text = message.text.substring(0, maxChars).trim() + "...";
            }
            result.push(message);
        }

        return result;
    }
});
