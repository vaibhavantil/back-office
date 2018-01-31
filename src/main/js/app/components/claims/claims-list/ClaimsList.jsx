import React from 'react';
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
)

const ClaimsList = ({ claims }) => (
    <PaginatorList
        list={claims}
        itemContent={item => <ListItem item={item} />}
    />
);

export default ClaimsList;
