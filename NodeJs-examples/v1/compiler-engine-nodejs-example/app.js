const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');

const indexRouter = require('./routes/index');
const submissionRouter = require('./routes/submission')

app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.set('view engine', 'html');

app.use('/', indexRouter);
app.use('/submit', submissionRouter);

console.log('Server started at http://localhost:3000');

module.exports = app;
