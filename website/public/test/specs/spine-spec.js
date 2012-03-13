

describe('Spine', function () {

	// Create a test model
	APPEAR.TestModel = Spine.Model.sub();
	APPEAR.TestModel.configure("TestModel", "id", "name", "value");
	APPEAR.TestModel.extend( Spine.Model.Ajax );

	beforeEach(function() {
		
		this.server = sinon.fakeServer.create();

		// create data & a model
		this.testName = "Test", this.testValue = 1;
		this.testModel = new APPEAR.TestModel({ name: this.testName, value: this.testValue });
	  
	});
	
	
	it('Should create a model', function () {
	
		// Assert
		expect( this.testModel.name ).toEqual( this.testName );
		expect( this.testModel.value ).toEqual( this.testValue );

		// NOTE:  No unique id is generated on creation

	});

	it('Should fetch & refresh data', function () {

		this.server.respondWith( "GET", "/testmodels",
			[ 200, {"Content-Type": "application/json"}, '{ "id": 1, "name": "Test2", "value": "2" }' ]
		);
		
		// setup refresh event
		var eventSpy = sinon.spy();
		APPEAR.TestModel.bind("refresh", eventSpy );
		
		// fetch data
		APPEAR.TestModel.fetch();

		// check if request was made 
		expect( this.server.requests.length ).toEqual(1);
		expect( this.server.requests[0].method ).toEqual("GET");
  		expect( this.server.requests[0].url ).toEqual("/testmodels");

  		// server repsonse
  		this.server.respond();

  		// make sure refresh event was called
  		expect( eventSpy ).toHaveBeenCalledOnce();
		
  		// check response
  		expect( APPEAR.TestModel.count() ).toEqual( 1 );
  		expect( APPEAR.TestModel.find( 1 ).name ).toEqual( "Test2" );
  		
	});

	it('Should save & store data', function () {

		var eventSpy = sinon.spy();

		// Bind event & save
		APPEAR.TestModel.bind("save", eventSpy );
		
		this.testModel.save();
		
		// Assert
		expect( eventSpy ).toHaveBeenCalledOnce();
		expect( eventSpy ).toHaveBeenCalledWith( this.testModel );
	});


	afterEach(function() {
	  this.server.restore();
	});

});