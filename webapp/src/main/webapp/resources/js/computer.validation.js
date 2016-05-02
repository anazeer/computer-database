$(document).ready(function() {
	
	jQuery.validator.addMethod("nameEmpty", function(value, element) {
        var str = value.trim();
		if (str.isEmpty())
			return false;
		return true;
	});
	
	jQuery.validator.addMethod("nameSize", function(value, element) {
        var str = value.trim();
		if (str.length > 80)
			return false;
		return true;
	});
	
	jQuery.validator.addMethod("nameContent", function(value, element) {
        var str = value.trim();
		return /^[\\p{L}0-9\.\'\+\-\/\s\(\)]*$/.test(value)
	});

	jQuery.validator.addMethod("dateFormat", function(value, element) {
		if (value == null)
			return true;
		var str = value.trim();
		if (str.isEmpty())
			return true;
		if (value == "0000-00-00")
			return false;
		return /^\d\d\d\d?-\d\d-\d\d/.test(value);
	});

	jQuery.validator.addMethod("dateOld", function(value, element) {
		if (value == null)
			return true;
		if (value == "")
			return true;
		if (value == "0000-00-00")
			return false;
		return new Date(value) > new Date("1970-01-01");
	});

	jQuery.validator.addMethod("dateTemporal", function(value, element, params) {
		if ($(params).val() == "")
			return true;
		if ($(params).val() == null)
			return true;
		if (value == null)
			return true;
		if (value == "")
			return true;
		return new Date(value) > new Date($(params).val());
	});

	$("#computerForm").validate({
		rules : {
			name : {
				required : true,
				nameEmpty : true,
				nameSize : true,
				nameContent : true
			},
			introduced : {
				required : false,
				dateFormat : true,
				dateBefore1970 : true,
			},
			discontinued : {
				required : false,
				dateFormat : true,
				dateOld : true,
				dateTemporal : "#introduced",
			}
		},
		messages: {
			name : {
				nameEmpty: translate['nameEmpty'],
				nameSize: translate['nameSize'],
				nameContent : translate['nameContent'],
			},
			introduced : {
				dateFormat : translate['dateFormat'],
				dateOld : translate['dateOld']
			},
			discontinued : {
				dateFormat : translate['dateFormat'],
				dateOld : translate['dateOld'],
				dateTemporal : translate['dateTemporal']
			}
		}
	});
});