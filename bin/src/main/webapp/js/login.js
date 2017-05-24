
function validaSubmit() {
	if ($('#username').val() == '') {
		alert("用户名不能为空。");
		return false;
	}
	if ($('#password').val() == '') {
		alert("密码不能为空。");
		return false;
	}
	return true;
}
$(document).keyup(function (e) {//捕获文档对象的按键弹起事件  
    if (e.keyCode == 13) {//按键信息对象以参数的形式传递进来了  
    	login();
    }  
}); 
function login() {
	if (!validaSubmit()) {
		return;
	}
	$.ajax({
		type : "POST",
		url : "rbac/cmUser?method=login",
		data : "uname=" + $('#username').val() + "&pwd="
				+ hex_md5($('#password').val()).toUpperCase(),
		dataType : "json",
		contentType : "application/json; charset=utf-8",
		success : function(data) {
			if (data["valid"] === 1) {
				window.location = "main.html";
			} else if (data["valid"] === 2) {
				alert("您使用的用户已失效，请联系系统管理员");
				$('#password').val('');
				return;
			} else if (data["valid"] === 3) {
				alert("此用户没有任何权限，请联系系统管理员");
				window.location = "login.html";
			} else {
				alert("您输入的用户名或密码有误。")
				$('#password').val('');
				return;
			}
		},
		error : function() {
			alert("无法登陆，请重新输入。")
			$('#password').val('');
			return;
		}
	})
}

