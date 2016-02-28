$(function() {
	$("#form1").attr("action", lt.getBasePath() + "login");
	$("#form2").attr("action", lt.getBasePath() + "register");

	function showMessage(str) {
		new jBox("Notice", {
			content: str,
			position: {
				x: "center",
				y: "center"
			},
			autoClose: 2000
		});
	}

	$("#form1").submit(function() {
		var $submit = $("#submit");
		$submit.attr("disabled", "disabled").css("background-color", "#ccc");
		var a = $(this).ajaxSubmit({
			success: function(data) {
				if (data.code == 200) {
					jsInterface.setCookie(data.message);
					window.opener = null;
					window.open('', '_self');
					window.close();
				} else {
					$submit.removeAttr("disabled").removeAttr("style");
					showMessage(data.message);
				}
			},
			complete: function(xhr, status) {
				if (status == "error") {
					$submit.removeAttr("disabled").removeAttr("style");
					showMessage("登录失败，稍后再试");
				}
			}
		});
		return false;
	});

	$("#dongtai").click(function() {
		showMessage("敬请期待");
	});

	$("#add").click(function() {
		$("#form1").hide();
		$("#top").hide();
		$("#form2").show();
	});

	$("#back").click(function() {
		$("#form1").show();
		$("#top").show();
		$("#form2").hide();
	});

	$("#form2").submit(function() {
		var $register = $("#form2 #register");
		var username = $("#form2 #username").val();
		var phone = $("#form2 #phone").val();
		var password = $("#form2 #password").val();
		var confirmpassword = $("#confirmpassword").val();
		var nickname = $("#nickname").val();
		// ------------用户名
		if (username == null || username == "") {
			$("#form2 #username").addClass("error");
			showMessage("用户名不能为空");
			return false;
		} else {
			$("#form2 #username").removeClass("error");
		}
		// ------------密码
		if (password == "") {
			$("#form2 #password").addClass("error");
			showMessage("输入密码");
			return false;
		} else {
			$("#form2 #password").removeClass("error");
		}
		if (password != confirmpassword) {
			$("#confirmpassword").addClass("error");
			showMessage("两次输入的密码不一样");
			return false;
		} else {
			$("#confirmpassword").removeClass("error");
		}
		// ------------手机
		if (!(/^1[3|4|5|7|8]\d{9}$/.test(phone))) {
			$("#form2 #phone").addClass("error");
			showMessage("请输入正确的手机号码");
			return false;
		} else {
			$("#form2 #phone").removeClass("error");
		}
		// ------------昵称
		if (nickname == null || nickname == "") {
			$("#form2 #nickname").addClass("error");
			showMessage("昵称不能为空");
			return false;
		} else {
			$("#form2 #nickname").removeClass("error");
		}

		$register.attr("disabled", "disabled").css("background-color", "#ccc");

		$(this).ajaxSubmit({
			success: function(data) {
				if (data.code == 200) {
					window.opener = null;
					window.open('', '_self');
					window.close();
				} else {
					showMessage(data.message);
					$register.removeAttr("disabled").removeAttr("style");
				}
			},
			complete: function(xhr, status) {
				if (status == "error") {
					showMessage("登录失败，稍后再试");
					$register.removeAttr("disabled").removeAttr("style");
				}
			}
		});
		return false;
	});
})