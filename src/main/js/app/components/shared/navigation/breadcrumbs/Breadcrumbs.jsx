import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Breadcrumb } from 'semantic-ui-react';
import { Link } from 'react-router-dom';

const BreadcrumbsContainer = styled.div`
    display: flex;
    margin: 20px;
`;

const Breadcrumbs = () => {
    // eslint-disable-next-line no-undef
    const pathname = window.location.pathname;
    const paths = pathname.split('/').map((path, i, arr) => {
        if (i === 0) {
            return {
                key: i,
                content: <Link to={'/dashboard'}>dashborad</Link>,
                active: true
            };
        }

        if (i === arr.length - 1) {
            return {
                key: i,
                content: path.toLowerCase(),
                active: false
            };
        }

        return {
            key: i,
            content: (
                <Link to={`${arr.slice(0, i + 1).join('/')}`}>
                    {path.toLowerCase()}
                </Link>
            ),
            active: true
        };
    });
    return (
        <BreadcrumbsContainer>
            <Breadcrumb sections={paths} />
        </BreadcrumbsContainer>
    );
};

Breadcrumbs.propTypes = {
    pathname: PropTypes.string
};

export default Breadcrumbs;
