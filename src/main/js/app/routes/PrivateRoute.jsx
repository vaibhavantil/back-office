import React from 'react';
import { Route, Redirect } from 'react-router';
import { checkAssetAuthorization } from '../lib/checkAuth';

const PrivateRoute = ({ component: Component, store, ...rest }) => {
    return (
        <Route
            {...rest}
            render={props =>
                checkAssetAuthorization(store) ? (
                    <Component {...props} />
                ) : (
                    <Redirect
                        to={{
                            pathname: '/login',
                            state: { from: props.location }
                        }}
                    />
                )
            }
        />
    );
};

export default PrivateRoute;
