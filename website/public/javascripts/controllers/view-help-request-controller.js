CBR.Controllers.ViewHelpRequest = new Class({
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

        this._initElements();
        this._initValidation();
        this._initEvents();
        this._initPills();
    },

    _getHelpRequest: function() {
        return this.options.helpRequest;
    },

    _getHelpRequester: function() {
        return this.options.helpRequest.requester;
    },

    _initElements: function () {
        this.$respondForm = jQuery("#respond-form");
        this.$respond = jQuery("#respond");

        this.$isSubscribingToFutureResponsesCheckbox = jQuery("#is-subscribing-to-future-responses");

        this.$referenceForm = jQuery("#reference-form");
        this.$writeReference = jQuery("#write-reference");

        this.$expanded = jQuery(".expanded");
    },

    _initValidation: function () {
        jQuery(".field-error").hide();

        this.responseValidator = new CBR.Services.Validator({
            fieldIds: [
                "response-text"
            ]
        });

        this.referenceValidator = new CBR.Services.Validator({
            fieldIds: [
                "helped-or-was-helped",
                "grade",
                "reference-text"
            ]
        });
    },

    _initEvents: function () {
        this.$isSubscribingToFutureResponsesCheckbox.change(jQuery.proxy(this._changeSubscriptionToResponses, this));

        this.$respond.click(jQuery.proxy(this._toggleRespondForm, this));
        jQuery("#cancel-response-button").click(jQuery.proxy(this._collapseRespondForm, this));
        jQuery("#post-response-button").click(jQuery.proxy(this._doCreateResponse, this));
        this.$respondForm.submit(jQuery.proxy(this._doCreateResponse, this));

        this.$writeReference.click(jQuery.proxy(this._toggleReferenceForm, this));
        jQuery("#cancel-reference-button").click(jQuery.proxy(this._collapseReferenceForm, this));
        jQuery("#post-reference-button").click(jQuery.proxy(this._doCreateReference, this));
        this.$referenceForm.submit(jQuery.proxy(this._doCreateReference, this));
    },

    _initPills: function() {
        jQuery(".pills a").click(jQuery.proxy(this.setActivePill, this));
    },

    _prepareDataForDisplay: function () {
        for (var i = 0; i < this.options.helpResponses.length; i++) {
            var currentHelpResponse = this.options.helpResponses[i];
            var currentCreationDatetime = currentHelpResponse.creationDatetime;

            var year = currentCreationDatetime.substring(0, 4);
            var month = currentCreationDatetime.substring(5, 7);
            var day = currentCreationDatetime.substring(8, 10);
            var hour = currentCreationDatetime.substring(11, 13);
            var minute = currentCreationDatetime.substring(14, 16);

            currentCreationDatetime = day + "/" + month;

            if (year !== new Date().getFullYear().toString())
                currentCreationDatetime += "/" + year;

            currentCreationDatetime += " " + hour + ":" + minute;

            currentHelpResponse.creationDatetime = currentCreationDatetime;
        }
    },

    _toggleRespondForm: function () {
        if (this.$respond.hasClass("expanded"))
            this._collapseRespondForm();
        else {
            this.$expanded.slideUpAnimated({
                complete: function () {
                    jQuery(this).hide();
                }
            });

            this.$respondForm.show(0, function () {
                jQuery(this).slideDownAnimated();
            });
            this.$respond.addClass("expanded");
        }
    },

    _collapseRespondForm: function () {
        this.$respondForm.slideUpAnimated({
            complete: function () {
                jQuery(this).hide();
            }
        });
        this.$respond.removeClass("expanded");
    },

    _toggleReferenceForm: function () {
        if (this.$writeReference.hasClass("expanded"))
            this._collapseReferenceForm();
        else {
            this.$expanded.slideUpAnimated({
                complete: function () {
                    jQuery(this).hide();
                }
            });

            this.$referenceForm.show(0, function () {
                jQuery(this).slideDownAnimated();
            });
            this.$writeReference.addClass("expanded");
        }
    },

    _collapseReferenceForm: function () {
        this.$referenceForm.slideUpAnimated({
            complete: function () {
                jQuery(this).hide();
            }
        });
        this.$writeReference.removeClass("expanded");
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

    _doCreateReference: function (e) {
        e.preventDefault();

        if (this.referenceValidator.isValid()) {
            var reference = new CBR.Models.Reference({
                toUserId: this._getHelpRequester().id,
                wasHelped: jQuery("#was-helped").hasClass("active"),
                ratingId: jQuery("#grade > li.active").data("grade-id"),
                text: jQuery("#reference-text").val()
            });

            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/references",
                data: CBR.JsonUtil.stringifyModel(reference),
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

    _changeSubscriptionToResponses: function(e) {
        e.preventDefault();

        var subscription = new CBR.Models.SubscriptionToHelpResponses({
            requestId: this._getHelpRequest().id
        });

        var _this = this;

        var request = new Request({
            urlEncoded: false,
            headers: { "Content-Type": "application/json" },
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
            request.delete();
    }
});
