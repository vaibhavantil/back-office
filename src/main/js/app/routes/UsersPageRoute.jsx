import React from 'react';
import PropTypes from 'prop-types';
import ChatPage from 'containers/messages-page/ChatPage';
import UsersPage from 'containers/users-page/UsersPage';
import { Switch, Route } from 'react-router';
import PrivateRoute from './PrivateRoute';

const MessagesPageRouter = ({ store }) => (
    <Switch>
        <Route
            exact
            path="/users"
            render={() => (
                <PrivateRoute
                    component={UsersPage}
                    path="/users"
                    store={store}
                />
            )}
        />
        <Route
            path="/users/:id"
            render={() => (
                <PrivateRoute
                    component={ChatPage}
                    path="/users/:id"
                    store={store}
                />
            )}
        />
    </Switch>
);

MessagesPageRouter.propTypes = {
    store: PropTypes.object.isRequired
}

export default MessagesPageRouter;
