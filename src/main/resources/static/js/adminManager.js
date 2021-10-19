$(document).ready(function() {
	$("#addAdminForm").on("submit", function(event){
		event.preventDefault();
        var formData = $(this).serialize();
		$.ajax({
			url: "/admin/addAdmin",
			method: "post",
			data: formData,
			success: function(data){
				if(data==="done") {
					location.reload();
				} else if (data==="alreadyUser"){
					$("#status2").show();
                    $("#status2").removeClass("text-success");
                    $("#status2").addClass("text-danger");
                    $("#status2").html("User already exists");
				} else {
					alert("Something went wrong.");
				}
			},
			error: function(){
				alert("Something went wrong! Please try again later");
			}
		});
	});
	
	$("#changePasswordForm").on("submit", function(event){
		event.preventDefault();
        var formData = $(this).serialize();
		$.ajax({
			url: "/admin/changeAdminPassword",
			method: "post",
			data: formData,
			success: function(data){
				if(data==="done") {
					$("#status").show();
                    $("#status").removeClass("text-danger");
                    $("#status").addClass("text-success");
                    $("#status").html("Change Password successfully.");
                    $("#opassword").val("");
                    $("#npassword").val("");
				} else if (data==="notMatched") {
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Old Password does not matched");
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

function deleteAdmin(id){
	$.ajax({
		url: "/admin/deleteAdmin?id="+id,
		method: "post",
		success: function(data){
			if(data==="done") {
				location.reload();
			} else {
				alert("Something went wrong.");
			}
		},
		error: function(){
			alert("Something went wrong! Please try again later");
		}
	});
}