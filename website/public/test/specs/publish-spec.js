

describe('Publish', function () {

    beforeEach(function() {

        this.server = sinon.fakeServer.create();

        this.model = APPEAR.Models.WebApp;

        APPEAR.currentUser = new APPEAR.Models.User({ firstName: "John", lastName: "Johnson", username: "john" });

        this.dutyPlanId = "dutyPlan";

        this.dutyPlanWebApp = new APPEAR.Models.WebApp({
            id: this.dutyPlanId,
            name: "Duty Plan",
            type: "userTemplateWebapp",
            username: APPEAR.currentUser.username,
            status: this.model.STATUS_TO_BE_PUBLISHED
        });


        this.trafficInfoId = "trafficInfo";

        this.trafficInfoWebApp = new APPEAR.Models.WebApp({
            id: this.trafficInfoId,
            name: "Traffic Info",
            type: "userTemplateWebapp",
            username: APPEAR.currentUser.username,
            status: this.model.STATUS_TO_BE_PUBLISHED
        });

        
        this.customizedId = "j98iuoj09j8";

        this.customizedWebApp = new APPEAR.Models.WebApp({
            id: this.customizedId,
            name: "CBR's Duty Plan",
            type: "customizedWebapp",
            username: APPEAR.currentUser.username,
            status: this.model.STATUS_TO_BE_PUBLISHED
        });

    });

    afterEach(function() {
        this.server.restore();
    });

});