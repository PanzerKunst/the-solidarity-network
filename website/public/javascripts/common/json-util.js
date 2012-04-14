CBR.JsonUtil = {};

CBR.JsonUtil.stringifyModel = function (obj) {
    var seen = [];

    return JSON.stringify(obj, function (key, val) {
        if (typeof val === "object") {
            if (seen.indexOf(val) >= 0)
                return undefined;
            seen.push(val);
        }
        return val;
    });
};
