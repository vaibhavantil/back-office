import React from 'react';
import PropTypes from 'prop-types';
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

PrivateRoute.propTypes = {
    component: PropTypes.element.isRequired,
    store: PropTypes.object.isRequired,
    location: PropTypes.string.isRequired
};

export default PrivateRoute;
