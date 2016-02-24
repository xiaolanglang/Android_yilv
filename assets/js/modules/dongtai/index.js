//主要负责下拉加载数据
;
(function() {
	var templateHtml = jQuery('#template')[0].innerHTML;
	jQuery.template('templateHtml', templateHtml);

	var url = lt.getBasePath() + "dongtai/list";
	jQuery.post(url, null, function(data) {
		createHtml(data);
	});

	function createHtml(data) {

		jQuery.tmpl("templateHtml", data.list, {
			getTime: function() {
				var dateFrom = new Date(this.data.createTime);
				var dateTo = new Date();

				var diff = dateTo.valueOf() - dateFrom.valueOf();

				var diff_day = parseInt(diff / (1000 * 60 * 60 * 24));
				var time = this.data.createTime.split(" ")[1]
				if (diff_day != 0) {
					diff_day += "天前 " + time;
				} else {
					diff_day = "今天 " + time;
				}
				return diff_day;
			}
		}).appendTo('#wrapper');

		Zepto(".imglazy").lazyload();
	}

})()

// 主要负责点击图片效果，图片延迟加载
$(function() {
	$("#wrapper").on("touchstart", ".good", function() {
		$(this).toggleClass("activity");
	});
})