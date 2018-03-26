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
    if(d.din==null||d.din==""){
        d.din ="-1"
    }
      if(d.datapoint==null||d.datapoint==""){
            d.datapoint ="-1"
        }
    control.switchChannel(d.channel_id + "," + d.title + "," + d.status + "," + d.datapoint + "," + d.din);
}

//加载容器属性和元件
// initContainer();
// setInterval("refreshContainer()", 2000);


var tokens;
var house_ids;

function init(token, house_id) {
    tokens = token;
    house_ids = house_id;
    floor = house_id;
    //加载容器属性和元件
    initContainerByToken(tokens, floor);
    setInterval("refreshContainerByToken(tokens,house_ids)", 8000);
}


function refreshContainerByToken(token, house_id) {
    $.ajax({
        url: 'http://dcw.suntrans-cloud.com/api/v1/plan',
        type: 'GET',
        data: {'house_id': house_id},
        dataType: "json",
        success: function (json) {
            // setPlan1Data(json);
            var bgImage = "";
            if (floor == 1) {
                bgImage = "img/floor1.png";
            }else if (floor==2){
                bgImage = "img/floor2.png";

            }else if (floor == 3){
                bgImage = "img/floor3.png";

            }else {
                bgImage = "img/floor4.png";

            }
            var con = json.data.container;
            if (con) {
                var width = 1309;
                var height = 693;
               var  scale = $("body").width() / width;
                $("div.full-wrapper").css("height", height * scale);
                $("svg.designer").css("transform", "scale(" + scale + ")");
                $("svg.designer").css("width", width);
                $("svg.designer").css("height", height);
                $("svg.designer").css("background-color", "#000000");
                $("svg.designer").css("background-image", "url(" + bgImage+ ")");
                $("svg.designer").empty();
                $("body").css("background-color", "#000000");
            }

            json.data.elements.map(createElement);
        }
    });
}

//初始化容器
function initContainerByToken(token, house_id) {
    $.ajax({
        url: 'http://dcw.suntrans-cloud.com/api/v1/plan',
        type: 'GET',
        data: {'house_id': house_id},

        dataType: "json",
        success: function (json) {
            // setPlan1Data(json);
            var bgImage = "";
            if (floor == '1') {
                bgImage = "img/floor1.png";
            }else if (floor=='2'){
                bgImage = "img/floor2.png";

            }else if (floor == '3'){
                bgImage = "img/floor3.png";

            }else {
                bgImage = "img/floor4.png";

            }
            var con = json.data.container;
            if (con) {
                var width = 1309;
                var height = 693;
                scale = $("body").width() / width;
                $("div.full-wrapper").css("height", height * scale);
                $("svg.designer").css("transform", "scale(" + scale + ")");
                $("svg.designer").css("width", width);
                $("svg.designer").css("height", height);
                $("svg.designer").css("background-color", "#000000");
                $("svg.designer").css("background-image", "url(" + bgImage+ ")");
                $("svg.designer").empty();
                $("body").css("background-color", "#000000");
            }
            // json.signals.map(function(signal){
            //  if(signal){
            // 	 signalMap[''+signal.id]=signal;
            //  }
            // });

            json.data.elements.map(createElement);
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
            return d.x - 552
        })
        .attr("y", function (d) {
            return d.y - 106
        })
        .attr("width", function (d) {
            if (d.vtype == 1) {
                return 60

            } else {
                return 30

            }
        })
        .attr("height", function (d) {
            if (d.vtype == 1) {


                return 60
            } else {
                return 30
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


