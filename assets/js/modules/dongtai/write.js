// 让textarea高度自适应
$.fn.autoHeight = function(max_height) {

	function autoHeight(elem) {
		if (elem.scrollHeight >= max_height) {
			return;
		}
		elem.style.height = 'auto';
		elem.scrollTop = 0; // 防抖动
		elem.style.height = elem.scrollHeight + 'px';
	}

	this.each(function() {
		autoHeight(this);
		$(this).on('keyup', function() {
			autoHeight(this);
		});
	});

}
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
		return this;
	}
}

jsMethod = new Method();
$(function() {

	$("#send").click(function() {
		var url = lt.getBasePath() + "dongtai/save";
		var data = {};
		jsMethod.setContent($("#content").val());
		jsMethod.setRange($("#range").attr("range"));
		data = jsMethod.getData();
		jsInterface.setParams(data.content, data.range);
	});

	new jBox('Modal', {
		attach: jQuery('#range'),
		content: jQuery("#modal-content"),
		closeButton: "none"
	});

	$(".select-range").click(function() {
		var range = $(this).attr("range");
		var conetnt = $(this).html();
		$('#range').attr("range", range).empty().append(conetnt).click();
	});
})