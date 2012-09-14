var http = require('http'),
    fs = require('fs'),
    url = require('url')
;

var universal_pattern = /^\/(\w+)\/(?:\w|-)+\/(\w+)/;

http.createServer(function (request, response) {
  response.writeHead(200, {
    'Content-Type': 'application/json; charset=utf-8'
  });

  var request_url = url.parse(request.url);

  var pathname = request_url.pathname;

  console.log('Request : ' + pathname);

  fs.readFile('json' + pathname + '.json', function (err, data) {
    if (err) {
      if (request_url.pathname.match(universal_pattern)) {
        pathname = request_url.pathname.replace(universal_pattern, '/$1/_all_/$2');

        console.log('==> Not found, fallback to : ' + pathname + '\n');

        fs.readFile('json' + pathname + '.json', function (errerr, datadata) {
          if (errerr) response.end(JSON.stringify({'status': false}));
          else response.end(datadata);
        });
      }
      else {
        response.end(JSON.stringify({'status': false}));
      }
    }
    else response.end(data);
  });

}).listen(8080);

console.log('Server running at http://127.0.0.1:8080/');
