//评论
$(function(){
	$("#comment").bind("touchstart",function(){
		jsInterface.pingLun();
	});
})

// 点赞
$(function() {
	$("#bottom").on("touchstart", ".good", function() {
		var $this = $(this);
		var url = lt.getBasePath() + "dongtaigood/save";
		var data = {};
		data.id = $this.attr("data-id");
		$.ajax({
			type : "post",
			url : url,
			data : data,
			dataType : "json",
			success : function(data) {
				if (data.code == 200 && data.message == "add") {
					addActive($this);

				} else if (data.code == 200 && data.message == "cancel") {
					removeActive($this);
				} else {
					removeActive($this);
					lt.showMessage("哎呀，再试试吧");
				}
			},
			error : function() {
				removeActive($this);
				lt.showMessage("哎呀，出现了一点小错误");
			}
		});
	});

	function removeActive($this) {
		$this.removeClass("active");
	}

	function addActive($this) {
		$this.addClass("active");
	}
})