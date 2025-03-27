<%@page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en" >
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>首页</title>
</head>
<body>
<h1>Hello World!</h1>
<script>
    let $path = "<%= request.getContextPath()%>";
    var param = location.href.split('?')[1]
    console.log('param', param)
    // location.href = $path + "/assets/views/home.html?" + param;
    // window.open($path + "/assets/views/index.html?"+ param, '_self')
</script>
</body>
</html>
