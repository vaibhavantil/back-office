import React from 'react';
import ChatPage from 'containers/messages-page/ChatPage';
import UsersPage from 'containers/messages-page/UsersPage';
import { Switch, Route } from 'react-router';
import PrivateRoute from './PrivateRoute';

const MessgesPageRouter = ({ store }) => (
    <Switch>
        <Route
            exact
            path="/messages"
            render={() => (
                <PrivateRoute
                    component={UsersPage}
                    path="/messages"
                    store={store}
                />
            )}
        />
        <Route
            path="/messages/:id"
            render={() => (
                <PrivateRoute
                    component={ChatPage}
                    path="/messages/:id"
                    store={store}
                />
            )}
        />
    </Switch>
);

export default MessgesPageRouter;
