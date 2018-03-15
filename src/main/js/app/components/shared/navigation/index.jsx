import React from 'react';
import PropTypes from 'prop-types';
import HorizontalMenu from './horizontal-menu/HorizontalMenu';
import Breadcrumbs from './breadcrumbs/Breadcrumbs';

const Navigation = ({ history, store }) =>
    history.location.pathname !== '/dashboard' ? (
        <React.Fragment>
            <HorizontalMenu history={history} />
            <Breadcrumbs history={history} state={store.getState()} />
        </React.Fragment>
    ) : null;

Navigation.propTypes = {
    history: PropTypes.object.isRequired,
    store: PropTypes.object
};

export default Navigation;
