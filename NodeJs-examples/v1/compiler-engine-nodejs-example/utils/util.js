const axios = require("axios");
const qs = require('qs');

const ACCESS_TOKEN = '<API_TOKEN>';

const BASE_URL = 'https://www.compiler-engine.com/api/v1/submissions';

/**
 *
 * @param requestBody POST body
 * @returns {Promise<{error: string}|string>}
 */
async function execute(requestBody) {
    if (!requestBody.source || !requestBody.compilerId) {
        return {error: 'source not present'};
    }

    let output = "";
    try {

        // submit code for execution
        const compileId = await getCompileId(requestBody);
        console.log(compileId);

        // Get Status
        let executing = true;
        let data = null;
        for (let i = 0; i < 7; i++) {
            if (executing) {
                // wait for 2s before checking submission status
                await sleep(2000);
                [executing, data] = await getExecutionStatus(compileId);
            }
        }

        // get the submission output
        output = handleSubmissionExecuted(data);
    } catch (e) {
        throw e;
    }
    return output;
}

/**
 *  Return the compile-id after submitting code for execution
 *
 * @param requestBody Submission POST body
 * @returns {Promise<string>}
 */
async function getCompileId(requestBody) {
    const options = {
        method: 'POST',
        headers: {
            'content-type': 'application/x-www-form-urlencoded',
            Authorization: 'Bearer ' + ACCESS_TOKEN
        },
        data: qs.stringify(requestBody),
        url: BASE_URL
    };

    // Make the API call
    let response;
    try {
        response = await axios(options);
    } catch (e) {
        throw e;
    }

    let compileId = "-1";
    if (response && response.status === 200) {
        compileId = response.data.id;
    }

    console.log(`CompileId - ${compileId}`);

    return compileId;
}

/**
 * sleep/wait for certain period of time
 *
 * @param {number} milliseconds
 * @returns {Promise<null>}
 */
const sleep = (milliseconds) => {
    return new Promise(resolve => setTimeout(resolve, milliseconds))
}

/**
 * Get the status of submission
 *
 * @param {string} compileId id returned after submitting code for execution
 * @returns {Promise<(boolean|any)[]|boolean[]>}
 */
async function getExecutionStatus(compileId) {
    const statusUrl = `${BASE_URL}/${compileId}`
    const options = {
        method: 'GET',
        headers: {
            Authorization: 'Bearer ' + ACCESS_TOKEN
        },
        url: statusUrl
    };

    // Make the API call
    let response;
    try {
        response = await axios(options);
    } catch (e) {
        throw e;
    }

    if (response && response.status === 200) {
        if (response.data) {
            const executing = response.data.executing;
            if (executing === "true" || executing === true) {
                return [true, null];
            } else {
                return [false, response.data];
            }
        }
    }
    return [true, null];
}

/**
 * Resolving response error received from API request
 *
 * @param e error received from compiler-engine API request
 * @returns {string|*}
 */
function resolveResponseError(e) {
    if (e.status) {
        if (e.status >= 400 && e.status < 500) {
            if (e.data) {
                return e.data.message;
            }
        }
    } else {
        return "Internal Error. Please check console";
    }
}

/**
 * Get stream data of a submitted code
 *
 * @param url
 * @returns {Promise<string|any>}
 */
async function getStreamOutput(url) {
    const options = {
        method: 'GET',
        headers: {
            Authorization: 'Bearer ' + ACCESS_TOKEN
        },
        url: url
    };
    let response;
    try {
        response = await axios(options);
    } catch (e) {
        throw e;
    }

    if (response && response.status === 200) {
        return response.data;
    }
    return "";
}

async function getAcceptedResponse(res) {
    let url = res.result.streams.output.url;
    return await getStreamOutput(url);
}

async function getCompilationErrorResponse(res) {
    let url = res.result.streams.cmpinfo.url;
    return await getStreamOutput(url);
}

async function getErrorResponse(res) {
    let url = res.result.streams.error.url;
    return await getStreamOutput(url);
}

/**
 * Function for handling a submission according to it's status
 *
 * @param submissionStatusResponse
 * @returns {Promise<string>} return stream url
 */
async function handleSubmissionExecuted (submissionStatusResponse){
    let statusCode = submissionStatusResponse.result.status.code;
    switch(statusCode) {
        case 15:
            return await getAcceptedResponse(submissionStatusResponse);
        case 11:
            return await getCompilationErrorResponse(submissionStatusResponse);
        case 12:
        case 13:
        case 17:
            return await getErrorResponse(submissionStatusResponse);
        case 20:
            break;
        default:
    }
}

module.exports = {
    execute: execute,
    resolveResponseError: resolveResponseError
};
