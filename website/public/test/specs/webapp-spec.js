


describe('WebApp', function () {

	beforeEach(function() {

		this.webApp1 = new APPEAR.Models.WebApp({ id: 1, name: "Sample", type: "templateWebapp" });
		this.webApp2 = new APPEAR.Models.WebApp({ id: 2, name: "Saved", type: "customizedWebapp" });
		this.webApp3 = new APPEAR.Models.WebApp({ id: 3, name: "Published", type: "customizedWebapp", status: "published" });
		this.webApp4 = new APPEAR.Models.WebApp({ id: 4, name: "TemplateToBePublished", type: "userTemplateWebapp", status: "published" });
		
	});
	
	it('Should have correct friendly type', function () {
		
		expect( this.webApp1.friendlyStatus() ).toEqual( "sample" );
		expect( this.webApp2.friendlyStatus() ).toEqual( "saved" );
		expect( this.webApp3.friendlyStatus() ).toEqual( "published" );
		expect( this.webApp4.friendlyStatus() ).toEqual( "sample" );
		
	});

	afterEach(function() {});

});