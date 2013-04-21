CBR.Controllers.Profile = new Class({
    Extends: CBR.Controllers.TemplateController,

    initialize: function (options) {
        this.parent(options);
    },

    run: function () {
        this.getEl().append(
            Mustache.render(
                jQuery("#content-template").html(),
                {
                    user: this._generateUserForTemplate(),
                    references: this._generateReferencesForTemplate()
                }
            )
        );

        this.initElements();
        this._initValidation();
        this._initEvents();
        this._initPills();

        this._initAction();
    },

    _getUser: function() {
        return this.options.user;
    },

    _getReferences: function() {
        return this.options.references;
    },

    initElements: function() {
        this.parent();

        this.$referenceForm = jQuery("#reference-form");
        this.$writeReference = jQuery("#write-reference");
        this.$referenceInput = jQuery("#reference-text");

        this.$expanded = jQuery(".expanded");
    },

    _initValidation: function () {
        this.referenceValidator = new CBR.Services.Validator({
            fieldIds: [
                "helped-or-was-helped",
                "grade",
                "reference-text"
            ]
        });
    },

    _initEvents: function () {
        this.$writeReference.click(jQuery.proxy(this._toggleReferenceForm, this));
        jQuery("#cancel-reference").click(jQuery.proxy(this._collapseReferenceForm, this));
        jQuery("#post-reference").click(jQuery.proxy(this._doCreateReference, this));
        this.$referenceForm.submit(jQuery.proxy(this._doCreateReference, this));
    },

    _initPills: function () {
        jQuery(".nav-pills a").click(jQuery.proxy(this.setActivePill, this));
    },

    _initAction: function() {
        if (this.getParameterByName("action") === "writeReference") {
            this._toggleReferenceForm();
            this.$referenceInput.focus();
        }
    },

    _generateUserForTemplate: function() {
        var result = Object.clone(this._getUser());
        if (result.description) {
            result.description = result.description.replace(/\n/g, "<br />");
        }
        return result;
    },

    _generateReferencesForTemplate: function() {
        var result = [];

        for (var i=0; i<this._getReferences().length; i++) {
            var reference = Object.clone(this._getReferences()[i]);
            reference.text = reference.text.replace(/\n/g, "<br />");
            result.push(reference);
        }

        return result;
    },

    _toggleReferenceForm: function () {
        if (this.$writeReference.hasClass("expanded"))
            this._collapseReferenceForm();
        else {
            this.$expanded.slideUpCustom();

            this.$referenceForm.slideDownCustom();
            this.$writeReference.addClass("expanded");
        }
    },

    _collapseReferenceForm: function () {
        this.$referenceForm.slideUpCustom();
        this.$writeReference.removeClass("expanded");
    },

    _doCreateReference: function (e) {
        e.preventDefault();

        if (this.referenceValidator.isValid()) {
            var reference = new CBR.Models.Reference({
                toUserId: this._getUser().id,
                wasHelped: jQuery("#was-helped").hasClass("active"),
                ratingId: jQuery("#grade > li.active").data("grade-id"),
                text: this.$referenceInput.val()
            });

            var _this = this;

            new Request({
                urlEncoded: false,
                headers: { "Content-Type": "application/json" },
                url: "/api/references",
                data: CBR.JsonUtil.stringifyModel(reference),
                onSuccess: function (responseText, responseXML) {
                    location.replace("/users/" + _this._getUser().username);
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
