
/*
APPEAR.Validator = Spine.Class.sub();

APPEAR.Validator.extend({

	perform: function( model ) {
		
		var errors = [];

		for( var rule in )


	},

	validateRule: function() {
		

	},

	validateField: function( field, model ) {
		

	}
	
});


APPEAR.Validator.Signup = APPEAR.Validator.sub({
	require: ["name"],
	email: ["email"],
	customFunc: function() {
		
	}
});



describe('Validate', function () {

	beforeEach(function() {
		
		// create empty model
		this.model = new APPEAR.Models.Signup();

	});
	
	it('Should perform validation', function() {

		// execute validations
		var errors = APPEAR.Validator.Signup.perform( model );
		
		// check	
		expect( errors[0] ).toEqual( true );
	});

	it('Should validate field', function() {

		// execute validations
		var errors = APPEAR.Validator.Signup.perform( model );
		
		// check	
		expect( errors[0] ).toEqual( true );
	});

	afterEach(function() {});

});

*/