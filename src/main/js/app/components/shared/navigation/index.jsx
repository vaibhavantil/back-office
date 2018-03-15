import React from 'react';
import PropTypes from 'prop-types';
import HorizontalMenu from './horizontal-menu/HorizontalMenu';
import Breadcrumbs from './breadcrumbs/Breadcrumbs';

const Navigation = ({ history }) =>
    history.location.pathname !== '/dashboard' ? (
        <React.Fragment>
            <HorizontalMenu history={history} />
            <Breadcrumbs history={history} />
        </React.Fragment>
    ) : null;

Navigation.propTypes = {
    history: PropTypes.object.isRequired
};

export default Navigation;
