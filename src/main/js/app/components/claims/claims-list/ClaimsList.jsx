import React from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { ItemContent } from '../../dashboard/Dashboard';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';

const ListItem = ({ item }) => (
    <Link to={`claims/${item.id}`}>
        <ItemContent>
            <div>{item.type.name}</div>
            <div>{item.registrationDate}</div>
        </ItemContent>
    </Link>
);

ListItem.propTypes = {
    item: PropTypes.object.isRequired
};

const ClaimsList = ({ claims }) => (
    <PaginatorList
        list={claims}
        itemContent={item => <ListItem item={item} />}
    />
);

ClaimsList.propTypes = {
    claims: PropTypes.array.isRequired
};

export default ClaimsList;
