$('textarea').autoHeight(70);

function Method() {
	var data = {};
	data.images = [];
	this.getData = function() {
		return data;
	}
	this.setImgs = function(imageUrl) {
		data.images.push(imageUrl);
		return this;
	}
	this.clearImgs = function() {
		data.images = [];
		return this;
	}
	this.setPosition = function(position) {
		data.position = position;
		return this;
	}
	this.setContent = function(content) {
		data.content = content;
		return this;
	}
	this.setRange = function(range) {
		data.range = range;
		return this;
	}
	this.showImgs = function() {
		var images = data.images;
		if (images == null) {
			return;
		}
		var content = "";
		$.each(images, function(i, v) {
			content += '<li><div style="background-image:url(\'' + v + '\')"></div></li>';
		});
		$(".figure-list").empty().append(content);
		new jBox("Notice", {
			content: "努力加载中...",
			position: {
				x: "center",
				y: "center"
			},
			autoClose: 2000
		});
		return this;
	}
}

jsMethod = new Method();
$(function() {

	$("#send").click(function() {
		var url = lt.getBasePath() + "dongtai/save";
		var data = {};
		jsMethod.setContent($("#content").val());
		jsMethod.setRange($("#range").attr("range"))
		data = jsMethod.getData();
		// console.log(JSON.stringify(data));
		var a = $.post(url, data, function(data) {
			if (!lt.isEmpty(data.message)) {
				new jBox("Notice", {
					content: data.message,
					position: {
						x: "center",
						y: "center"
					},
					autoClose: 2000
				});
			} else {
				new jBox("Notice", {
					content: "上传失败",
					position: {
						x: "center",
						y: "center"
					},
					autoClose: 2000
				});
			}
		});
	});

	new jBox('Modal', {
		attach: $('#range'),
		content: $("#modal-content"),
		closeButton: "none"
	});

	$(".select-range").click(function() {
		var range = $(this).attr("range");
		var conetnt = $(this).html();
		$('#range').attr("range", range).empty().append(conetnt).click();
	});
})