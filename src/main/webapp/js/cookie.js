/**
 * Created with IntelliJ IDEA.
 * User: Lorenzo
 * Date: 15-5-10
 * Time: 下午7:35
 * To change this template use File | Settings | File Templates.
 */

function getCookie(c_name) {
    if(document.cookie.length>0) {
        var c_start = document.cookie.indexOf(c_name+"=");
            if(c_start!=-1) {
            c_start = c_start+ c_name.length + 1;
            var c_end = document.cookie.indexOf(";", c_start);
            if(c_end==-1) {
                c_end = document.cookie.length;
            }
            return unescape(document.cookie.substring(c_start, c_end));
        }
    }
    window.location.href="/login222.html";
}

function setCookie(c_name, value, expiretimes){
    var _date = new Date();
    _date.setDate(_date.getMinutes() + expiretimes);
    document.cookie = c_name + "=" +escape(value);
}

function deleteCookie(name){
    var date=new Date();
    date.setTime(date.getTime()-10000);
    document.cookie=name+"=''";
}

function validateCookie() {
    if(getCookie('userName')==null || getCookie('userName') == ''){
        window.location.href="/login222.html";
    };
}