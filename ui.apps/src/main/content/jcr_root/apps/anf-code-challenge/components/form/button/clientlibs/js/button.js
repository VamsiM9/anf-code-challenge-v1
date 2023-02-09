(function () {
    'use strict';
    window.onload = () => {
		console.log('ready');
        $('#custom-submit').click(function(e){
        e.preventDefault();
        let firstName = $('input[name=firstName]').val();
        let lastName = $('input[name=lastName]').val();
        let age = $('input[name=age]').val();

        $.ajax({
        type: 'GET',
        url: '/bin/saveUserDetails',
        data: {'firstName': firstName,
               'lastName': lastName,
               'age': age},
        success: function(data) {
			$('#custom-error').html("");
            console.log('success');
        },
        error: function(data){
            $('#custom-error').html("you are not eligible");
        }

    });
    });
    };
})();