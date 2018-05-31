<%--
  Created by IntelliJ IDEA.
  User: huyuqi
  Date: 2018/5/15
  Time: 20:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>使用servlet进行文件的上传和下载操作安全漏洞演示</title>
</head>
<body>
    <h2>上传文件大小超过限制</h2>
    <div>
        <p>文件大小限制为2M，超过2M上传失败</p>
        <form action="${pageContext.request.contextPath}/servlet/uploadCheckSize" method="post" enctype="multipart/form-data">
            文件：<input type="file" name="file"><br><br>
            <input type="submit" value="开始上传">
        </form>
    </div>
    <br><br><hr>
    <h2>上传文件类型校验绕过</h2>
    <div>
        <p>
            仅支持jpg类型，可以进行文件类型绕过,绕过后可上传其他文件或者获取webshell,不再通过前台校验文件后缀，前台校验可以被绕过
        </p>
        <form action="${pageContext.request.contextPath}/servlet/uploadCheckType" method="post" enctype="multipart/form-data">
            文件：<input type="file" name="file"><br><br>
            <input type="submit" value="开始上传">
        </form>
    </div>
    <br><br><hr>
    <h2>上传文件存在跨目录问题</h2>
    <div>
        <p>
            上传文件没有存储在后台专门保存文件的目录中,文件名存在跨目录问题，可通过抓包工具修改请求
        </p>
        <form action="${pageContext.request.contextPath}/servlet/uploadCrossDir" method="post" enctype="multipart/form-data">
            文件：<input type="file" name="file"><br><br>
            <input type="submit" value="开始上传">
        </form>
    </div>
    <br><br><hr>
    <h2>上传高压缩比文件</h2>
    <div>
        <p>
            上传压缩文件需要校验解压后文件大小。解压后大小限制为10M
        </p>
        <form action="${pageContext.request.contextPath}/servlet/uploadUnzip" method="post" enctype="multipart/form-data">
            文件：<input type="file" name="file"><br><br>
            <input type="submit" value="开始上传">
        </form>
    </div>

</body>
</html>
