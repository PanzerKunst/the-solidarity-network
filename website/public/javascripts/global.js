// create the base namespace
var CBR = CBR || {};

// create additional namespace
CBR.Models = CBR.Models || {};
CBR.Controllers = CBR.Controllers || {};
CBR.Services = CBR.Services || {};

// Because jsHint doesn't know Mustache
var Mustache = Mustache || {};

// Because jsHint doesn't know AjaxUpload
var qq = qq || {};

CBR.isEmptyObject = function (obj) {
    for (var prop in obj) {
        if (obj.hasOwnProperty(prop))
            return false;
    }

    return true;
};

if (typeof String.prototype.startsWith !== 'function') {
    String.prototype.startsWith = function (str) {
        return this.slice(0, str.length) === str;
    };
}