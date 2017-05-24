/**
 * 获取定义的ID规则获取系统中的ID值
 *
 * @param keyid string 定义的key主键
 *
 * @returns 系统中命名的key值
 */
function getkey(keyid) {
    var keyvalue;
    $.ajax({
        url: '../../key/getkey',
        type: "POST",
        dataType: "text",
        contentType: "application/json; charset=utf-8",
        data:keyid,
        timeout:3000,
        async: false,
        success: function (data) {
            keyvalue = data;
        },
        error: function() {
            alert("error");
        },
        complete: function(XMLHttpRequest, status) {
            if(status == 'timeout') {
                alert("超时");
            }
        }
    });
    return keyvalue;
}

/**
 * 后台获取UUID
 */
function getUuid() {
    var keyvalue;
    $.ajax({
        url: '../../key/getuuid',
        type: "POST",
        dataType: "text",
        contentType: "application/json; charset=utf-8",
        timeout:3000,
        async: false,
        success: function (data) {
            alert(data);
            keyvalue = data;
        },
        error: function() {
            alert("error");
        },
        complete: function(XMLHttpRequest, status) {
            if(status == 'timeout') {
                alert("超时");
            }
        }
    });
    return keyvalue;
}

/**
 * 后台获取32位UUID
 */
function getUu32id() {
    var keyvalue;
    $.ajax({
        url: '../../key/getuu32id',
        type: "POST",
        dataType: "text",
        contentType: "application/json; charset=utf-8",
        timeout:3000,
        async: false,
        success: function (data) {
            alert(data);
            keyvalue = data;
        },
        error: function() {
            alert("error");
        },
        complete: function(XMLHttpRequest, status) {
            if(status == 'timeout') {
                alert("超时");
            }
        }
    });
    return keyvalue;
}

/**
 * 获取固定规则的主键ID
 */
function getPKUUID() {
    var keyvalue;
    $.ajax({
        url: '../../key/getPKUUID',
        type: "POST",
        dataType: "text",
        contentType: "application/json; charset=utf-8",
        timeout:3000,
        async: false,
        success: function (data) {
            alert(data);
            keyvalue = data;
        },
        error: function() {
            alert("error");
        },
        complete: function(XMLHttpRequest, status) {
            if(status == 'timeout') {
                alert("超时");
            }
        }
    });
    return keyvalue;
}