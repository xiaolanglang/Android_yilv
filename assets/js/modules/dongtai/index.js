$(function() {
	var showImage = true;
	var imageNum;
	window.onresize = function() {
		controlImageView();
	}
	var swiper = null;
	document.getElementById("swiperView").addEventListener('touchmove',
			function(event) {
				event.preventDefault();
			}, false);

	function controlImageView() {
		if (showImage) {
			showImageView();
		} else {
			closeImageView();
		}
	}

	function showImageView() {
		var height = document.documentElement.clientHeight + "px";
		$(".swiper-container").css("height", height);
		$("body").css("height", height).addClass("swipe-body");
		$("#swiperView").css("display", "inherit");
		var swiper = new Swiper('.swiper-container', {
			pagination : '.swiper-pagination',
			paginationClickable : false,
			spaceBetween : 30,
			initialSlide : imageNum
		});
		showImage = false;
	}

	function closeImageView() {
		$("body").css("height", "100%").removeClass("swipe-body");
		var content = '<div class="swiper-container"><div class="swiper-wrapper"></div><div class="swiper-pagination"></div></div>';
		$("#swiperView").empty().append(content);
		swiper = null;
		showImage = true;
	}

	$(".imglazy").lazyload();

	$(".figure-list")
			.on(
					"click",
					"li",
					function(obj) {
						var $this = $(this);
						imageNum = $this.index(); // 点击了第几个
						var content = "";
						$this
								.parent()
								.find("li div")
								.each(
										function(i, v) {
											var img = $(this).attr(
													"data-original");
											content += '<div class="swiper-slide" style="background-image: url(\''
													+ img + '\')"></div>';
										});

						$(".swiper-wrapper").append(content);
						jsInterface.imageFullPage("true");
						showImage = true;
					});

	$("#swiperView").click(function() {
		$("#swiperView").css("display", "none");
		jsInterface.imageFullPage("false");
	});

})