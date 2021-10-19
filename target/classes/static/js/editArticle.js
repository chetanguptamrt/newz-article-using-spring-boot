$(document).ready(function() {
	$("#editArticleForm").on("submit", function(event){
		event.preventDefault();
        var formData = $(this).serialize();
		$.ajax({
			url: "/admin/updateArticle",
			method: "post",
			data: formData,
			success: function(data){
				if(data==="done") {
					$("#status").show();
                    $("#status").removeClass("text-danger");
                    $("#status").addClass("text-success");
                    $("#status").html("Article update successfully.");
				} else if (data==="noDetail") {
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Please provide required details.");
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