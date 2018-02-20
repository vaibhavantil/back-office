'use strict';

/* eslint-env browser */
/* global module, Raven, process */

import 'babel-polyfill';
import React from 'react';
import ReactDOM from 'react-dom';
import App from './app';

if (window.Raven && process.env.NODE_ENV === 'production') {
    Raven.config('https://62dae85cd81a446a9878e3be1409f871@sentry.io/291079').install();
}

const appElement = document.getElementById('react');
appElement.style.height = '100%';

ReactDOM.render(<App />, appElement);

if (module.hot) {
    module.hot.accept();
}
