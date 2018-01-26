import React from 'react';
import { Switch, Route } from 'react-router';
import PrivateRoute from './PrivateRoute';
import ClaimsPage from 'containers/claims-page/ClaimsPage';
import ClaimDetails from 'containers/claim-details/ClaimDetails';

const ClaimsPageRoute = ({ store }) => (
    <Switch>
        <Route
            exact
            path="/claims"
            render={() => (
                <PrivateRoute
                    component={ClaimsPage}
                    path="/claims"
                    store={store}
                />
            )}
        />
        <Route
            exact
            path="/claims/:id"
            render={() => (
                <PrivateRoute
                    component={ClaimDetails}
                    path="/claims/:id"
                    store={store}
                />
            )}
        />
    </Switch>
);

export default ClaimsPageRoute;
