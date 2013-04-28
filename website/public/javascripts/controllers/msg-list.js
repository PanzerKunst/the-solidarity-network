CBR.Controllers.MsgList = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().append(
            Mustache.render(
                jQuery("#content-template").html(),
                { messages: this._generateMessagesForTemplate() }
            )
        );

        this.initElements();
        this._initEvents();
    },

    _getMessages: function () {
        return this.options.messages;
    },

    _generateMessagesForTemplate: function () {
        var result = [];

        var maxChars = 75;

        for (var i = 0; i < this._getMessages().length; i++) {
            var message = this._getMessages()[i];

            // Formatting date
            message.creationDatetime = this.formatDate(message.creationDatetime);

            // Formatting text
            if (message.title === null && message.text.length > maxChars) {
                message.text = message.text.substring(0, maxChars).trim() + "...";
            }

            result.push(message);
        }

        return result;
    },

    _initEvents: function () {
        jQuery(".clickable").click(jQuery.proxy(this._navigateToMessage, this));
    },

    _navigateToMessage: function (e) {
        e.preventDefault();

        var msgId = jQuery(e.currentTarget).data("id");

        if (msgId !== undefined)
            location.href = "/messages/" + msgId;
    }
});
