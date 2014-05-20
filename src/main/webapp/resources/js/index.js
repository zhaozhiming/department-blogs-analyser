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
            resultContent += "<td><a href='" + result[i].link + "'>" + result[i].title + "</a></td>";
            resultContent += "<td>" + result[i].website + "</td>";
            resultContent += "<td>" + result[i].author.name + "</td>";
            resultContent += "<td>" + result[i].author.groupName + "</td>";
            resultContent += "<td>" + result[i].view + "</td>";
            resultContent += "<td>" + result[i].comment + "</td>";
            resultContent += "<td>" + result[i].time + "</td>";
            resultContent += "</tr>";
        }

        $('#result').html(resultContent);
    }
});


