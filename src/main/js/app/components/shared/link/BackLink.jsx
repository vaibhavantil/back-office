import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { Icon } from 'semantic-ui-react';
import { history } from 'app/store';

const clickHandler = () => {
    history.goBack();
};

const BackLink = ({ path }) => (
    <React.Fragment>
        {path ? (
            <Link to={`/${path}`}>
                <Icon name="arrow left" /> Back
            </Link>
        ) : (
            <a href="#" onClick={clickHandler}>
                <Icon name="arrow left" /> Back
            </a>
        )}
    </React.Fragment>
);

BackLink.propTypes = {
    path: PropTypes.string
};

export default BackLink;
