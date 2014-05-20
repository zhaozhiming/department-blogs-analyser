$(document).ready(function () {

    var rules = {
        searchKeyword: {
            identifier: 'searchKeyword',
            rules: [
                {
                    type: 'empty',
                    prompt: '请输入查询关键字'
                }
            ]
        }
    };

    var setting = {
        onSuccess: function () {
            var searchUrl = $("#searchUrl").val();
            var queryData = {
                searchKeyword: $("#searchKeyword").val()
            };

            $.ajax({
                url: searchUrl,
                type: "POST",
                data: queryData,
                beforeSend: function () {
                    $(".ui.dimmer").addClass("active");
                }
            }).done(function (data) {
                    $(".ui.dimmer").removeClass("active");

                    showResult(data);

                    $("tr").hover(function () {
                        $(this).toggleClass("positive");
                    });

                    var searchKeywords = $("#searchKeyword").val().split(/\s+/);
                    $('body td').highlight(searchKeywords);
                });
        }
    };

    $('#searchForm').form(rules, setting);

    function showResult(data) {
        var result = jQuery.parseJSON(data);
        var resultContent = "";

        for (var i = 0; i < result.length; i++) {
            console.log(result[i]);
            resultContent += "<tr>";
            resultContent += "<td>" + result[i].jarName + "</td>";
            resultContent += "<td>" + result[i].version + "</td>";
            resultContent += "<td><a class='item' title='点击查看源码' target='_blank' href='";
            resultContent += $("#showUrl").val() + "?sourceFilePath=" + result[i].sourceFilePath;
            resultContent += "&jarFilePath=" + result[i].jarFilePath + "'>" + result[i].sourceFilePath + "</a></td>";
            resultContent += "</tr>";
        }

        $('#result').html(resultContent);
    }
});


