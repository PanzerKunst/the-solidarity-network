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
    },

    _getUser: function() {
        return this.options.user;
    },

    _getReferences: function() {
        return this.options.references;
    },

    _generateUserForTemplate: function() {
        var result = Object.clone(this._getUser());
        result.description = result.description.replace(/\n/g, "<br />");
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
    }
});
