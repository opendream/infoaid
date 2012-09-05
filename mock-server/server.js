var http = require('http'),
    fs = require('fs'),
    url = require('url')
;

http.createServer(function (request, response) {
  response.writeHead(200, {
    'Content-Type': 'application/json; charset=utf-8'
  });

  var request_url = url.parse(request.url);

  fs.readFile('json' + request_url.pathname, function (err, data) {
    if (err) response.end(JSON.stringify({'status': false}));
    else response.end(data);
  });

}).listen(8080);

console.log('Server running at http://127.0.0.1:8080/');
