const http = require('http')

// compiler access_token
const API_TOKEN = '<API_TOKEN>';

// compile ID obtained from submitting for API code compilation
const compileID = '<Compile_ID>';

// the stream of data which is needed. Eg - output, error, source, etc.
const stream = 'output';

// HTTP params/options
const options = {
  hostname: 'compiler-engine.com',
  port: 80,
  path: `/api/v1/submissions/${compileID}/${stream}`,
  method: 'GET',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + API_TOKEN,
  }
}

// GET submission status 
const req = http.request(options, res => {
  console.log(`statusCode: ${res.statusCode}`);

  res.on('data', d => {      
    // for displaying to the console
    console.log('GET Response: \n');
    console.log(d.toString());
  });
});

req.on('error', error => {
  console.error(error)
});

req.end();
