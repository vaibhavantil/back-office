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
            path="/members"
            render={() => (
                <PrivateRoute
                    component={UsersPage}
                    path="/members"
                    store={store}
                />
            )}
        />
        <Route
            path="/members/:id"
            render={() => (
                <PrivateRoute
                    component={ChatPage}
                    path="/members/:id"
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
