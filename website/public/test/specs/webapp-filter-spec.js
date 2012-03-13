

describe('WebApp Filters', function () {

	beforeEach(function() {

		APPEAR.currentUser = new APPEAR.Models.User({ firstName: "John", lastName: "Johnson", username: "john" });
        
		this.webApp1 = new APPEAR.Models.WebApp({
			id: 1,
			name: "Test1",
			type: "templateWebapp", 
			descriptorCodes: [ "travel-and-transport", "field-service" ]
		});

		this.webApp2 = new APPEAR.Models.WebApp({
			id: 2,
			name: "Test2",
			type: "customizedWebapp", 
			descriptorCodes: [ "travel-and-transport", "utilities" ],
			username: "john"
		});

		this.webApp3 = new APPEAR.Models.WebApp({ 
			id: 3, 
			name: "TemplateToBePublished", 
			type: "userTemplateWebapp", 
			status: "toBePublished",
			username: "john"
		});

		APPEAR.Models.WebAppFilter.addWebAppToFilters( this.webApp1 );
		APPEAR.Models.WebAppFilter.addWebAppToFilters( this.webApp2 );
		APPEAR.Models.WebAppFilter.addWebAppToFilters( this.webApp3 );
			
	});
	
	it('Should give correct key counts', function () {
		
		expect( APPEAR.Models.WebAppFilter.countByKey("all") ).toEqual( 3 );
		expect( APPEAR.Models.WebAppFilter.countByKey("travel-and-transport") ).toEqual( 2 );
		expect( APPEAR.Models.WebAppFilter.countByKey("field-service") ).toEqual( 1 );
		expect( APPEAR.Models.WebAppFilter.countByKey("saved") ).toEqual( 1 );
		expect( APPEAR.Models.WebAppFilter.countByKey("non-existing-key") ).toEqual( 0 );

	});

	it('Should filter correctly by active key', function () {
		
		// check first when all filter is active
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp1 ) ).toBeTruthy();
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp2 ) ).toBeTruthy();
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp3 ) ).toBeTruthy();

		APPEAR.Models.WebAppFilter.activeFilterKey = "field-service";
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp1 ) ).toBeTruthy();
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp2 ) ).toBeFalsy();
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp3 ) ).toBeFalsy();

		APPEAR.Models.WebAppFilter.activeFilterKey = "saved";
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp1 ) ).toBeFalsy();
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp2 ) ).toBeTruthy();
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp3 ) ).toBeFalsy();

		APPEAR.Models.WebAppFilter.activeFilterKey = "non-existing-key";
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp1 ) ).toBeFalsy();
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp2 ) ).toBeFalsy();
		expect( APPEAR.Models.WebAppFilter.isWebAppActive( this.webApp3 ) ).toBeFalsy();


	});

	afterEach(function() {});

});