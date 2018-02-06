import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { Icon } from 'semantic-ui-react';

const BackLink = ({ path }) => (
    <Link to={`/${path || ''}`}>
        <Icon name="arrow left" /> Back
    </Link>
);

BackLink.propTypes = {
    path: PropTypes.string
};

export default BackLink;
