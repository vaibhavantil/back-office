'use strict';

import React from 'react';
import { Provider } from 'react-redux';
import { Router, Route, Switch, Redirect } from 'react-router';
import Routes from './routes';
import Store from './store';
import { history } from './store';
import Navigation from './components/shared/navigation';

const store = Store.configureStore();

const App = () => (
    <Provider store={store}>
        <Router history={history}>
            <React.Fragment>
                <Navigation history={history} store={store} />
                <Switch>
                    <Route path="/login/oauth" component={Routes.LoginPageRoute} />
                    <Route path="/login/process" component={Routes.LoginProcessPageRoute} />
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
                        path="/questions"
                        store={store}
                        component={Routes.QuestionsPageRoute}
                    />
                    <Route
                        path="/claims"
                        render={routeProps => (
                            <Routes.ClaimsPageRoute
                                {...routeProps}
                                store={store}
                            />
                        )}
                    />
                    <Route
                        path="/members"
                        render={routeProps => (
                            <Routes.UsersPageRoute
                                {...routeProps}
                                store={store}
                            />
                        )}
                    />
                    <Redirect from="*" to="/dashboard" />
                </Switch>
            </React.Fragment>
        </Router>
    </Provider>
);

export default App;
