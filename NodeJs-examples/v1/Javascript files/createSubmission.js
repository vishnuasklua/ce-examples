const http = require('http')

// compiler access_token
const API_TOKEN = "<API_TOKEN>";

/**
 * @description This contains the Body for a API POST code submission
 * @param {String} compilerId: This is the ID for language selection/compiler's ID
 *                             Check http://www.compiler-engine.com/supported-languages for more
 * @param {String} source: This is the source code for compilation
 * @param {String} input: This is the input for the source code
 * @param {Number} timeLimit: time limit for source code execution. Default: 5s
 */
const requestBody = {
  compilerId: '116',
  source: "print(input())",
  input: "Testing a simple input/output in python3",
  timeLimit: 4
}

const data = JSON.stringify(requestBody)

// Http options/params
const options = {
  hostname: 'compiler-engine.com',
  port: 80,
  path: '/api/v1/submissions',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': data.length,
    'Authorization': 'Bearer ' + API_TOKEN
  }
}

// send request and print the result
const req = http.request(options, res => {
  console.log(`\nstatusCode: ${res.statusCode}`)

  res.on('data', d => {
    // print the POST response
    console.log(`POST response: \n`);
    const output = JSON.parse(d.toString());
    console.log(JSON.stringify(output, null, 2));
  })
})

req.on('error', error => {
  console.error(error)
})

req.write(data)
req.end()