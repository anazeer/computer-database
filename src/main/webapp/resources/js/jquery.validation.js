$("#myemailform").validate(
      {
        rules: 
        {
          fullname: 
          {
            required: true
          },
          email: 
          {
            required: true,
            email: true
          }
        },
        messages: 
        {
          fullname: 
          {
            required: "Please enter your name"
          },
          email: 
          {
            required: "Please enter your email address."
          }
        }
      });   