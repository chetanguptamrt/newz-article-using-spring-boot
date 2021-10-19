function removeArticle(id,total) {
	$.ajax({
		url: "/admin/deleteArticle?id="+id,
		method: "post",
		success: function(data){
			if(data==="done") {
				if(total%10==1){
					location.href="/admin/showArticle"
				}
				location.reload();	
			} else{
				alert("Something went wrong.");
			}
		},
		error: function(){
			alert("Something went wrong! Please try again later");
		}
	});
}