const http = require('http')

// compiler access_token
const API_TOKEN = '<API_TOKEN>';

// HTTP params/options
const options = {
  hostname: 'compiler-engine.com',
  port: 80,
  path: `/api/v1/compilers`,
  method: 'GET',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + API_TOKEN
  }
}

// GET submission status 
const req = http.request(options, res => {
  console.log(`statusCode: ${res.statusCode}`);

  res.on('data', d => {    
    let de = JSON.parse(d.toString());
      
    // for displaying to the console
    console.log('GET Response: \n');
    console.log(JSON.stringify(de, null, 2));
  });
});

req.on('error', error => {
  console.error(error)
});

req.end();
