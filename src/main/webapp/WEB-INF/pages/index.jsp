<html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<head>
    <title>Department Blog Analyser</title>
    <link type="text/css" rel="stylesheet" href="http://cdn.staticfile.org/semantic-ui/0.16.1/css/semantic.min.css">

    <script type="text/javascript" src="http://cdn.staticfile.org/jquery/2.1.1-rc2/jquery.min.js"></script>
    <script type="text/javascript" src="http://cdn.staticfile.org/jquery/2.1.1-rc2/jquery.min.map"></script>
    <script type="text/javascript" src="http://cdn.staticfile.org/semantic-ui/0.16.1/javascript/semantic.min.js"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/index.js'/>"></script>
</head>
<body>

<input id="searchUrl" type="hidden" value="<c:url value='/search'/>">

<div class="ui transparent inverted main menu">
    <div class="container">
                <div class="title item">
            <b>部门博客查询</b>
        </div>
    </div>
</div>

<div class="ui grid">
    <div class="one wide column"></div>

    <div class="fifteen wide column">
        <div id="searchForm" class="ui form">
            <div class="two fields">
                <div class="field">
                    <div class="ui icon input">
                        <input id="searchKeyword" type="text" name="searchKeyword" placeholder="请输入查询关键字...">
                        <i class="search icon"></i>
                    </div>
                </div>

                <div class="field">
                    <div id="searchBtn" class="ui blue submit button">查询</div>
                </div>
            </div>
            <div class="ui error message"></div>
        </div>

        <div class="ui divider"></div>

        <div class="ui dimmer">
            <div class="ui large text loader">查询中...</div>
        </div>

        <table class="ui table segment">
            <thead>
            <tr>
                <th>标题</th>
                <th>网站</th>
                <th>作者</th>
                <th>分组</th>
                <th>访问量</th>
                <th>评论数</th>
                <th>发布时间</th>
            </tr>
            </thead>
            <tbody id="result">
            </tbody>
        </table>
    </div>
</div>

<div class="ui divider"></div>

<div class="ui inverted segment center aligned">
    <div class="ui inverted horizontal relaxed divided tiny list">
        <div class="item">Super-powered by XX ©2014-2016</div>
    </div>
</div>

</body>
</html>