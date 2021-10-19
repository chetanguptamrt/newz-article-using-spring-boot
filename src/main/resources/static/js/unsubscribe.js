$(document).ready(function() {
	$("#unsubcribeForm").on("submit", function(event){
		event.preventDefault();
        var formData = $(this).serialize();
		$.ajax({
			url: "/deleteSubscriberByEmail",
			method: "post",
			data: formData,
			success: function(data){
				if(data==="done") {
					$("#status").show();
                    $("#status").removeClass("text-danger");
                    $("#status").addClass("text-success");
                    $("#status").html("Unsubscribe email successfully.");
                    $("#email").val("");
				} else{
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Something went wrong.");
				}
			},
			error: function(){
				alert("Something went wrong! Please try again later");
			}
		});
	});
});