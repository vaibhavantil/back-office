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
                        <Route
                            path="/login"
                            component={Routes.LoginPageRoute}
                        />
                        <Routes.PrivateRoute
                            path="/assets"
                            store={store}
                            component={Routes.AssetsPageRoute}
                        />
                        <Routes.PrivateRoute
                            path="/dashboard"
                            store={store}
                            component={Routes.DashboardPageRoute}
                        />
                         <Routes.PrivateRoute
                            path="/claims"
                            store={store}
                            component={Routes.ClaimsPageRoute}
                        />
                        <Route
                            path="/users"
                            render={routeProps => (
                                <Routes.UsersPageRoute
                                    {...routeProps}
                                    store={store}
                                />
                            )}
                        />
                        <Redirect from="*" to="/dashboard" />
                    </Switch>
                </Router>
            </Provider>
        );
    }
}
