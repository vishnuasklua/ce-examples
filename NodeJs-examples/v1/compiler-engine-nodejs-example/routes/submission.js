const express = require('express');
const router = express.Router();
const utils = require('../utils/util');

router.post('/', async function(req, res) {
    let error = null, response = null;
    if (req.body) {
        if (req.body.compilerId != null || req.body.source) {

            const requestBody = {
                compilerId: req.body.compilerId,
                source: req.body.source,
                input: req.body.input,
                timeLimit: req.body.timeLimit,
            };

            try {
                response = await utils.execute(requestBody);
            } catch (e) {
                if (e.response) {
                    error = utils.resolveResponseError(e.response);
                } else {
                    console.error(e);
                    error = "Something went wrong. Check console logs";
                }
            }
        } else {
            error = "CompilerId and source are compulsory for code submission/execution";
        }
    } else {
        error = "Empty POST body";
    }

    let data = {
        response: response,
        error: error
    }
    res.send(data);
});


module.exports = router;
