


describe('User', function () {

	beforeEach(function() {
		this.currentUser = new APPEAR.Models.User({ firstName: "John", lastName: "Johnson", username: "john" });
	});
	
	it('Should have correct values', function () {
	
		// Assert
		expect( this.currentUser.firstName ).toEqual( "John" );
		expect( this.currentUser.lastName ).toEqual( "Johnson" );
		expect( this.currentUser.username ).toEqual( "john" );

	});

	afterEach(function() {});

});