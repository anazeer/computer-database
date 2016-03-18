$("#computerForm").validate(
		{
			rules: 
			{
				computerName: 
				{
					required: true,
					
				},
				introduced: 
				{
					dateValidation: true
				},
				discontinued:
				{
					dateValidation: true
				}
			},
			messages: 
			{
				computerName: 
				{
					required: "name is required"
				},
				introduced: 
				{
					required: "Please enter your email address."
				}
			},
			error: function(label) {
				$(this).addClass("error")
			}
		});
$.validator.addMethod("dateValidation", function(value, element) {
    return /(^$|^(1|2)[0-9]{3}-(0[1-9]|1[0-2])-(0[1-9]|(1|2)[0-9]|3[0-2])$)/.test(value);
}, "uncorrect date format (should be yyyy-mm-dd)");