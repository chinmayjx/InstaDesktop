// 192.168.29.229

var http = require("http");
var fs = require('fs');
const { exec } = require("child_process");

http.createServer(function(request, response) {
    let file = fs.createWriteStream("abc.jpg");
    if (request.method == 'POST') {
        console.log('POST');
        request.pipe(file);

        request.on('end', function() {
            console.log("received");
            response.write("received");
            response.end();
            console.log(process.cwd());
            file.close();
            exec(`gsettings set org.gnome.desktop.background picture-uri '${process.cwd()}/abc.jpg'`);
        })
    } else {
        console.log("GET");
        response.write("GET");
        response.end();
    }
}).listen(8080);