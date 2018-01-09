'use strict';

import React from 'react';
import { Provider } from 'react-redux';
import { Router, Route, Switch, Redirect } from 'react-router';
import createBrowserHistory from 'history/createBrowserHistory';

import Routes from './routes';
import Store from './store';

export const history = createBrowserHistory();
const store = Store.configureStore();

export default class App extends React.Component {
    render() {
        return (
            <Provider store={store}>
                <Router history={history}>
                    <Switch>
                        <Route path="/login" component={Routes.LoginPageRoute} />
                        <Routes.PrivateRoute path="/assets" store={store} component={Routes.MainPageRoute} />
                        <Route path="/messages/:id" component={Routes.MessagesPageRoute} />
                        <Redirect from="*" to="/messages/21"/>
                    </Switch>
                </Router>
            </Provider>
        );
    }
}
