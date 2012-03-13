

describe('Signup', function () {

	beforeEach(function() {

		this.signupModel = new APPEAR.Models.Signup({
			name: "Test",
			email: "test@test.com",
			organisation: "Appear",
			title: "Test title",
			interestReason: "None",
			deviceCount: "5",
			deploymentDate: "Bla bla",
			isDeviceTypeAndroidSmartphone: false,
			isDeviceTypeAndroidTablet: false,
			isDeviceTypeIPhone: false,
			isDeviceTypeIPad: false,
			isDeviceTypeWEH: false,
			isDeviceTypeWindowsPhone7: false,
			otherDeviceTypes: false
		});

	});

	it('Should perform successful signup', function () {
		
		var validationSaveSpy = sinon.spy();
		APPEAR.Models.Signup.bind("save", validationSaveSpy );

		this.signupModel.save();

		expect( validationSaveSpy ).toHaveBeenCalledOnce();

	});
	

	it('Should validate empty model', function () {
		
		var validationErrorSpy = sinon.spy();
		APPEAR.Models.Signup.bind("error", validationErrorSpy );

		var signupModel = new APPEAR.Models.Signup({});
		signupModel.save();

		expect( validationErrorSpy ).toHaveBeenCalledOnce();

		// check the validation
		var validationErrors = validationErrorSpy.args[0][1];

		// name is empty
		expect( validationErrors[0].field ).toEqual( "name" );
		expect( validationErrors[0].type ).toEqual( "empty" );	

		// email is empty
		expect( validationErrors[1].field ).toEqual( "email" );
		expect( validationErrors[1].type ).toEqual( "empty" );	

		// email is invalid
		expect( validationErrors[2].field ).toEqual( "email" );
		expect( validationErrors[2].type ).toEqual( "invalid" );	

	}); 
	
	afterEach(function() {});

});