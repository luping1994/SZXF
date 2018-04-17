var floor = 1;

if ($("#openType").val() == "alarm") {
    $("#btnAlarm").trigger("click");
}


function openConfirmDialog(d) {
//    console.log(d.href);
    sendCommand(d)

    // var htmlStr = '';
    // htmlStr += '<input type="hidden" id="channel_id" value="0">';
    // htmlStr += '<div class="weui-mask"></div>';
    // htmlStr += '<div class="weui-dialog__hd"><strong class="weui-dialog__title">弹窗标题</strong></div>';
    // htmlStr += '<div class="weui-dialog__bd">';
    // htmlStr += d.title + '</div>';
    // htmlStr += '</div>';
    // htmlStr += '<div class="weui-dialog__ft">';
    // htmlStr += '<a id="qvxiao" href="javascript:void (0);" class="weui-dialog__btn weui-dialog__btn_default">取消</a>';
    // htmlStr += '<a id="queding" href="javascript:void (0);" class="weui-dialog__btn weui-dialog__btn_primary">确定</a>';
    // htmlStr += '</div></div>';

    // var tips = d.status ? "是否关闭" : "是否打开";
    //
    // $("#title")
    //     .text('关闭' + d.title);
    // $("#alertDialog")
    //     .show()
    //     .data("data", d)
    // $("#queding")
    //     .click(function () {
    //         sendCommand(d)
    //         $("#alertDialog").hide();
    //
    //     });
    // $("#qvxiao")
    //     .click(function () {
    //         $("#alertDialog").hide();
    //     });
}

function sendCommand(d) {
//    console.log('channel_id为：' + d.channel_id);
    if (d.din == null || d.din == "") {
        d.din = "-1"
    }
    if (d.datapoint == null || d.datapoint == "") {
        d.datapoint = "-1"
    }
    control.switchChannel(d.din + "," + d.number + "," + d.status + "," + d.title);
}

//加载容器属性和元件
// initContainer();
// setInterval("refreshContainer()", 2000);


var tokens;
var house_ids;
var timer = null

function init(token, house_id) {
    tokens = token;
    house_ids = house_id;
    floor = house_id;
    //加载容器属性和元件
    initContainerByToken(tokens, floor);
    if (timer != null) {
        clearInterval(timer);
    }
    timer = setInterval("refreshContainerByToken(tokens,house_ids)", 8000);
}


function refreshContainerByToken(token, house_id) {
    $.ajax({
        url: 'http://stsz119.suntrans-cloud.com/api/v1/plan',
        type: 'GET',
        data: {'area_id': house_id},
        dataType: "json",
        success: function (json) {
            // setPlan1Data(json);
            var bgImage = "";
            if (floor == 4) {
                bgImage = "img/floor1.png";
            } else if (floor == 5) {
                bgImage = "img/floor2.png";

            } else if (floor == 8) {
                bgImage = "img/floor2.png";

            } else {
                bgImage = "img/floor2.png";

            }
            var con = json.data.container;
            if (con) {
                var width = 1385;
                var height = 625;
                var scale = $("body").width() / width;
                $("div.full-wrapper").css("height", height * scale);
                $("svg.designer").css("transform", "scale(" + scale + ")");
                $("svg.designer").css("width", width);
                $("svg.designer").css("height", height);
                $("svg.designer").css("background-color", "transparent");
                $("svg.designer").css("background-image", "url(" + bgImage + ")");
                $("svg.designer").empty();
                $("body").css("background-color", "#feece6");
            }

            json.data.elements.map(createElement);
            createScrollBarElements();
            createTips();
        }
    });
}




function createScrollBarElements() {
    floor1ScrollBar.map(createScrollbar);


}

//初始化容器
function initContainerByToken(token, house_id) {
    $.ajax({
        url: 'http://stsz119.suntrans-cloud.com/api/v1/plan',
        type: 'GET',
        data: {'area_id': house_id},

        dataType: "json",
        success: function (json) {
            // setPlan1Data(json);
            var bgImage = "";
            if (floor == '4') {
                bgImage = "img/floor1.png";
            } else if (floor == '5') {
                bgImage = "img/floor2.png";

            } else if (floor == '8') {
                bgImage = "img/floor2.png";

            } else {
                bgImage = "img/floor2.png";

            }
            var con = json.data.container;
            if (con) {
                var width = 1385;
                var height = 625;
                scale = $("body").width() / width;
                $("div.full-wrapper").css("height", height * scale);
                $("svg.designer").css("transform", "scale(" + scale + ")");
                $("svg.designer").css("width", width);
                $("svg.designer").css("height", height);
                $("svg.designer").css("background-color", "transparent");
                $("svg.designer").css("background-image", "url(" + bgImage + ")");
                $("svg.designer").empty();
                $("body").css("background-color", "#feece6");
            }
            // json.signals.map(function(signal){
            //  if(signal){
            // 	 signalMap[''+signal.id]=signal;
            //  }
            // });

            json.data.elements.map(createElement);
            createScrollBarElements();
            createTips();

        }
    });
}

//创建元素
function createElement(ele) {
    // if (ele.type == "image") {
    createImage(ele);
    // }
}

//创建一张图片
function createImage(data) {
    var imageGroup = d3.select("#floorPlanSvg")
        .append("g")
        .data([data]);

    if (data.status) {
        if (data.vtype == 1) {
            imageGroup.classed("ele", true)
        }
    }


    imageGroup.append("image")
        .classed("control-image", true)
        .attr("x", function (d) {
            return d.x
        })
        .attr("y", function (d) {
            return d.y
        })
        .attr("width", function (d) {
            if (d.vtype == 1) {
                return 32

            } else {
                return 32

            }
        })
        .attr("height", function (d) {
            if (d.vtype == 1) {


                return 32
            } else {
                return 32
            }
        })
        .attr("xlink:href", function (d) {
            if (d.status) {
                if (d.vtype == 1)
                    return 'img/light_on2.png';
                else
                    return 'img/socket_on2.png';

            } else {
                if (d.vtype == 1)
                    return 'img/light_off2.png';
                else
                    return 'img/socket_off2.png';
            }
        });
    imageGroup.on("click", openConfirmDialog);

}

function createScrollBarElements() {

    if (floor == 4) {
        floor1ScrollBar.map(createScrollbar);
    } else if (floor == 5) {
        floor2ScrollBar.map(createScrollbar);

    } else if (floor == 8) {

    } else {

    }

}

/**
 * 创建一条流动条控件
 * @param data 绑定数据
 * @return
 */
function createScrollbar(data) {
    var signal = true;
    var scrollbarGroup = d3.select("#floorPlanSvg")
        .append("g")
        .data([data]);

    scrollbarGroup.append("line")
        .classed("scrollbar-line", true)
        .attr("x1", data.x1)
        .attr("y1", data.y1)
        .attr("x2", data.x2)
        .attr("y2", data.y2)
        .style({'stroke': data.stroke, 'stroke-width': data.strokeWidth, 'stroke-dasharray': data.strokeDasharray});

    scrollbarGroup.append("polygon")
        .classed("scrollbar-border", true)
        .attr("stroke", data.fill)
        .attr("points", calcScrollbarPoints(data))
        .style("opacity", data.showBg);
    if (signal) {
        // var runScript = "(function(){var X=" + signal.value + ";return " + data.content + ";}())";
        try {
            // var result = eval(runScript);
            // if (result) {
            scrollbarGroup.select("line.scrollbar-line")
                .style("stroke-dashoffset", 0)
                .transition()
                .duration(data.radius)
                .ease("linear")
                .style("stroke-dashoffset", data.strokeLinecap);
            // }
        } catch (e) {
            console.log("流动条公式计算异常" + runScript);
        }
    }

    /**
     * 计算边框的坐标
     * @param data
     * @return
     */
    function calcScrollbarPoints(data) {
        var absX = data.width - data.x;
        var absY = data.height - data.y;
        var angle = Math.atan(absY / absX);
        var r = data.strokeWidth / 2 + 2;
        var offsetX = Math.sin(angle) * r;
        var offsetY = Math.cos(angle) * r;
        if (isNaN(offsetX) || isNaN(offsetY)) {
            return "";
        }
        var point1 = (data.x + offsetX) + "," + (data.y - offsetY);
        var point2 = (data.width + offsetX) + "," + (data.height - offsetY);
        var point3 = (data.width - offsetX) + "," + (data.height + offsetY);
        var point4 = (data.x - offsetX) + "," + (data.y + offsetY);
        return point1 + " " + point2 + " " + point3 + " " + point4;
    }

    /**
     * 计算流动条间隔
     * @param data
     * @return
     */
    function calcScrollbarDasharray(data) {
        return data.strokeWidth + " " + data.strokeDasharray;
    }
}

function createTips() {
    var imageGroup = d3.select("#floorPlanSvg")
        .append("g");

    imageGroup.append("image")
        .classed("control-image", true)
        .attr("x", function (d) {
            return 837
        })
        .attr("y", function (d) {
            return 565
        })
        .attr("width", function (d) {
           return 406
        })
        .attr("height", function (d) {
            return 48
        })
        .attr("xlink:href", function (d) {

            return 'img/tips.png'
        });
}


