'use strict';

import React from 'react';
import { Provider } from 'react-redux';
// import { ConnectedRouter } from 'react-router-redux';
import { Router, Route, Switch, Redirect } from 'react-router';
import Routes from './routes';
import Store from './store';
import { history } from './store';

const store = Store.configureStore();

const App = () => (
    <Provider store={store}>
        <Router history={history}>
            <Switch>
                <Route path="/login" component={Routes.LoginPageRoute} />
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
                <Route
                    path="/claims"
                    render={routeProps => (
                        <Routes.ClaimsPageRoute {...routeProps} store={store} />
                    )}
                />
                <Route
                    path="/users"
                    render={routeProps => (
                        <Routes.UsersPageRoute {...routeProps} store={store} />
                    )}
                />
                <Redirect from="*" to="/dashboard" />
            </Switch>
        </Router>
    </Provider>
);

export default App;
