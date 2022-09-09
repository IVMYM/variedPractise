<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <link rel="stylesheet" type="text/css" href="/webuploader/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/webuploader/webuploader.css">
    <link type="text/css" href="/webuploader/bootstrap-theme.css">
    <script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="webuploader/webuploader.min.js"></script>
    <script type="text/javascript" src="webuploader/bootstrap.min.js"></script>

    <style>
        .rg_layout{
            width: 500px;
            height: 120px;
            border: 8px solid #EEEEEE;
            background-color: #d9edf7;
            /*让div水平居中*/
            margin: auto;
            padding: auto;

        }

        .rg_left{
            /*border: 1px solid red;*/
            float: left;
            margin: 15px;
        }
        .rg_right{
            /*border: 1px solid red;*/
            float: right;
            margin: 15px;
        }
    </style>

    <script >
        var page = {
            init:function () {
                $("#ctlBtn_syn").click($.proxy(this.upload,this));
            },
            upload:function () {
                var guid = Math.random().toString().substring(2,8)

                var file = $("#file_syn")[0].files[0]; //文件对象
                if (file){
                    $("#ctlBtn_syn").attr({"disabled":"disabled"});
                    $("#output").text("文件正在上传...");

                    var name = file.name,        //文件名
                        size = file.size;        //总大小

                    var shardSize = 20 * 1024 * 1024,    //以25MB为一个分片
                        shardCount = Math.ceil(size / shardSize);  //总片数

                    for(var i = 0;i < shardCount;++i){
                        //计算每一片的起始与结束位置
                        var start = i * shardSize,
                            end = Math.min(size, start + shardSize);
                        //构造一个表单，FormData是HTML5新增的
                        var form = new FormData();
                        form.append("file", file.slice(start,end));  //slice方法用于切出文件的一部分
                        form.append("name", name);
                        form.append("chunks", shardCount);  //总片数
                        form.append("chunk", i);        //当前是第几片
                        form.append("guid", guid);        //当前是第几片

                        //Ajax提交
                        $.ajax({
                            url: "../fileUpload/fileSyn",
                            type: "POST",
                            data: form,
                            async: true,        //异步
                            processData: false,  //jquery不要对form进行处理
                            contentType: false,  //指定为false才能形成正确的Content-Type
                            success: function(data){
                                //成功后的事件
                                finished(data)
                            }
                        });
                    }
                }else {
                    alert("请选择要上传的文件")
                }
            }
        }



        $(function () {
            // 利用h5实现分片上传, ajax实现异步
            page.init();

            var guid
            var uploader = WebUploader.create({
                // swf文件路径
                swf : 'webuploader/Uploader.swf',
                // 文件接收服务端。
                // server : 'http://localhost:8080/fileUploaderServlet',
                server : "/fileUpload/file",
                // 选择文件的按钮。可选。
                // 内部根据当前运行是创建，可能是input元素，也可能是flash.
                pick : {id:'#picker'},
                chunked: true,  //分片处理
                chunkSize: 50 * 1024 * 1024, //每片10M
                threads:1,//上传并发数。允许同时最大上传进程数。
                formData:{'guid':guid},
                // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
                resize : false

            });
            // 当有文件被添加进队列的时候
            uploader.on('fileQueued', function(file) {
                $("#thelist").empty();
                $("#thelist").append('<h4 id="info" class="info">' + file.name + '</h4>');
            });

            // 当文件开始上传的时候
            uploader.on('uploadStart', function() {
                guid = uploader.options.formData.guid = Math.random().toString().substring(2,8);

                $("#info").text("文件正在上传....")
            });

            //
            uploader.on('uploadFinished',function () {
                $("#info").empty();
                $("#thelist").append('<h4 id="info" class="info">' + '文件上传完成' + '</h4>');
            });

            $("#ctlBtn").on ('click',function () {
                if ($(this).hasClass('disabled')){

                    alert("disabled");
                    return false;
                }

                uploader.upload();
            })
        });
        function finished(data) {
            if (data.code=="1"&& data.msg=="success"){
                $("#ctlBtn_syn").removeAttr("disabled");
                $("#output").text("文件上传完成;fileId="+data.data);
                var file = document.getElementById('file_syn');
                file.value = '';
            }
        }
    </script>

</head>
<body>

<div id="uploader" class="rg_layout">
    <div class="rg_left">
        <div id="picker">选择文件</div>
        <div id="thelist" class="uploader-list"></div>
    </div>

    <div class="rg_right">
        <button id="ctlBtn" class="btn btn-default">开始上传</button>
    </div>
</div>
<div id="uploader_syn" class="rg_layout">
    <div class="rg_left">
        <input type="file" id="file_syn">
        <br>
        <span id="output"></span>
    </div>

    <div class="rg_right">
        <button id="ctlBtn_syn">开始上传</button>
    </div>
</div>

</body>
</html>

