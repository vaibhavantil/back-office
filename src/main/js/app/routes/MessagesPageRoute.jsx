import React from 'react';
import MessgesPage from 'containers/messages-page/MessgesPage';
import MessgesSearchPage from 'containers/messages-page/MessagesSearch';
import { Switch, Route } from 'react-router';
import PrivateRoute from './PrivateRoute';

const MessgesPageRouter = ({ store }) => (
    <Switch>
        <Route
            exact
            path="/messages"
            render={() => (
                <PrivateRoute
                    component={MessgesSearchPage}
                    path="/messages"
                    store={store}
                />
            )}
        />
        <Route
            path="/messages/:id"
            render={() => (
                <PrivateRoute
                    component={MessgesPage}
                    path="/messages/:id"
                    store={store}
                />
            )}
        />
    </Switch>
);

export default MessgesPageRouter;
