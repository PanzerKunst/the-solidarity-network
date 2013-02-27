CBR.Controllers.CreateMessage = new Class({
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

    initElements: function () {
        this.parent();

        this.$recipientField = jQuery("#recipient");
        this._initSelect(this.$recipientField);
    },

    _initSelect: function ($input) {
        $input.select2({
            placeholder: "Search",
            minimumInputLength: 2,
            ajax: {
                url: "http://localhost:9000/api/users",
                dataType: 'json',
                data: function (term, page) {
                    return {
                        query: term
                    };
                },
                results: function (data, page) {
                    return { results: data };
                }
            },
            formatResult: this._formatDropdownItem,
            formatSelection: this._formatDropdownItem,
            escapeMarkup: function (m) {
                return m; // we do not want to escape markup since we are displaying html in results
            }
        });
    },

    _formatDropdownItem: function(user) {
        return user.firstName + " " + user.lastName + " &lt;" + user.username + "&gt;";
    },

    _initValidation: function () {
        this.validator = new CBR.Services.Validator({
            fieldIds: [
                "recipient",
                "message"
            ]
        });
    },

    _initEvents: function () {
        jQuery("#submit").click(jQuery.proxy(this._doCreate, this));
        jQuery("form").submit(jQuery.proxy(this._doCreate, this));
    },

    _doCreate: function (e) {
        e.preventDefault();

        if (this.validator.isValid()) {
            var message = new CBR.Models.Message({
                toUser: { id: this.$recipientField.val() },
                title: jQuery("#title").val(),
                content: jQuery("#message").val()
            });

            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/messages",
                data: CBR.JsonUtil.stringifyModel(message),
                onSuccess: function (responseText, responseXML) {
                    location.href = "/messages?from=new";
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
