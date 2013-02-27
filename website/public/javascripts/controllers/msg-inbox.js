CBR.Controllers.MsgInbox = new Class({
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
    },

    _getMessages: function () {
        return this.options.messages;
    },

    initElements: function () {
        this.parent();

        this.$listContainer = jQuery("#list-container");
    },

    _initEvents: function () {
        jQuery(".inbox-item").click(jQuery.proxy(this._navigateToMessage, this));
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
    }
});
