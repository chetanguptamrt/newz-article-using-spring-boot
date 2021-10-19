$(document).ready(function() {
	$("#addArticleForm").on("submit", function(event){
		event.preventDefault();
        var form = $("#addArticleForm")[0];
        var formData = new FormData(form);
		$.ajax({
			url: "/admin/saveArticle",
			method: "post",
			data: formData,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
			success: function(data){
				if(data==="done") {
					$("#status").show();
                    $("#status").removeClass("text-danger");
                    $("#status").addClass("text-success");
                    $("#status").html("Article add successfully.");
                    $("#articleName").val("");
                    $("#articleAuthor").val("");
                    $("#tags").val("");
                    tinymce.activeEditor.setContent('');
                    $("#active").prop('checked',false);
                    $("#image").val("");
				} else if (data==="fileError") {
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Image doesn't upload, try again later.");
				} else if (data==="invalidFile") {
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Image format invald, Please provie only jpg, jpeg, png file.");
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