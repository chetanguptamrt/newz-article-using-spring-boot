function deleteAllSubscriber(){
	$.ajax({
		url: "/admin/deleteAllSubscriber",
		method: "post",
		success: function(data){
			if(data==="done"){
				location.reload();
			} else {
				alert("Something went wrong!");
			}
		},
		error: function(){
			alert("Something went wrong!");
		}
	});
}