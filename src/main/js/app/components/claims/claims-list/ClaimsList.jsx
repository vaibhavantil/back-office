import React from 'react';
import PaginatorList from 'components/common/paginator-list/PaginatorList';

const ListItem = ({ item }) => <div>{item}</div>;

const ClaimsList = ({ claims }) => (
    <PaginatorList
        list={claims}
        itemContent={item => <ListItem item={item} />}
    />
);

export default ClaimsList;
