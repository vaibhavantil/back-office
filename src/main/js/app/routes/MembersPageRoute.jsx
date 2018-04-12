import React from 'react';
import PropTypes from 'prop-types';
import ChatPage from 'containers/chat-page/ChatPage';
import MembersPage from 'containers/members-page/MembersPage';
import { Switch, Route } from 'react-router';
import PrivateRoute from './PrivateRoute';

const MessagesPageRouter = ({ store }) => (
    <Switch>
        <Route
            exact
            path="/members"
            render={() => (
                <PrivateRoute
                    component={MembersPage}
                    path="/members"
                    store={store}
                />
            )}
        />
        <Route
            path="/members/:id/:msgId?"
            render={() => (
                <PrivateRoute
                    component={ChatPage}
                    path="/members/:id/:msgId?"
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
