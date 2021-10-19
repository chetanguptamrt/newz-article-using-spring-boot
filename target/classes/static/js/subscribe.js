$(document).ready(function() {
	$("#subscriberForm").on("submit", function(event){
		event.preventDefault();
        var formData = $(this).serialize();
		$.ajax({
			url: "/subscribe",
			method: "POST",
			data: formData,
			success: function(data){
				if(data==="done") {
					$("#email").val("");
				} else{
					alert("Something went wrong.");
				}
			},
			error: function(){
				alert("Something went wrong! Please try again later");
			}
		});
	});
});
function search(){
	var search = $("#search").val();
	if(search===""){
		$("#display-search").hide();
	} else {
		$.ajax({
			url: "/search-article?q="+search,
			method: "get",
			success: function(data) {
				var htmlData = "";
				for(var d = 0; d<data.length; d++){
					htmlData+="<a class='text-dark border' style='text-decoration:none; display: block;' href='/article/"+data[d].id+"' >"+data[d].articleName+"</a>";
				}
				if(data.length==0){
					$("#display-search").html("Not found");
				} else {
					$("#display-search").html(htmlData);	
				}
			}
		});
		$("#display-search").show();
	}
}