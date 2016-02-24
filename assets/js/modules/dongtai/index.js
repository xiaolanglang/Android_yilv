var LoadMethod = function(fn) {
	var url = lt.getBasePath() + "dongtai/list";
	var $loading = $("#loading");
	var pageNum = 1;

	function createHtml(data) {
		if (data == null || data.list == null || data.list.length <= 0) {
			$loading.find(".load2").text("没有更多");
			$loading.addClass("failed");
			return;
		}

		jQuery.tmpl("templateHtml", data.list, {
			getTime : function() {
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
		
		pageNum = data.pageNum;
		Zepto(".imglazy").lazyload();
		$("#loading").show();
		if (fn != null) {
			fn();
		}

	}

	this.load = function(_pageNum) {
		$loading.removeClass("failed");
		jQuery.post(url, {
			pageNum : _pageNum
		}, function(data) {
			createHtml(data);
		}).error(function() {
			$loading.addClass("failed");
		});
	}

	this.getPageNum = function() {
		return pageNum;
	}
}

// 主要负责下拉加载数据和页面初始化加载数据
;
(function() {
	var templateHtml = jQuery('#template')[0].innerHTML;
	jQuery.template('templateHtml', templateHtml);

	// 滚动加载开始
	var $loading = $("#loading");
	var windowHeight = $(window).height();
	var $body = $("body");
	var $wrapper = $("#wrapper");
	var threshold = 300;
	var isloading = false;
	$(document).bind("scroll", function() {
		var contentH = $wrapper.get(0).scrollHeight; // 内容高度
		var scrollTop = $body.scrollTop();// 滚动高度
		if (windowHeight + scrollTop + threshold >= contentH && isloading) {
			isloading = false;

			load.load(load.getPageNum() + 1);
		}
	});
	// 滚动加载结束

	var load = new LoadMethod(function() {
		isloading = true;
	});

	load.load(1); // 页面刚开始加载的时候

	// 加载出错的时候，点击重试按钮
	$("#loading").on("touchstart", ".load2", function() {
		load.load(load.getPageNum() + 1);
	});

})()

// 点赞
$(function() {
	$("#wrapper").on("touchstart", ".good", function() {
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
		var num = $this.find(".number").text();
		num = num - 1;
		$this.find(".number").text(num);
	}

	function addActive($this) {
		$this.addClass("active");
		var num = $this.find(".number").text();
		num = num / 1 + 1;
		$this.find(".number").text(num);
	}
})
