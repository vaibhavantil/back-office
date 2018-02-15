'use strict';

/* eslint-env browser */
/* global module */

import 'babel-polyfill';
import React from 'react';
import ReactDOM from 'react-dom';
import App from './app';

const appElement = document.getElementById('react');
appElement.style.height = '100%';

ReactDOM.render(<App />, appElement);

if (module.hot) {
    module.hot.accept();
}
