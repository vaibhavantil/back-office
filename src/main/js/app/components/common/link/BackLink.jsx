import React from 'react';
import { Link } from 'react-router-dom';
import { Icon } from 'semantic-ui-react';

const BackLink = ({ path }) => (
    <Link to={`/${path || ''}`}>
        <Icon name="arrow left" /> Back
    </Link>
);

export default BackLink;
