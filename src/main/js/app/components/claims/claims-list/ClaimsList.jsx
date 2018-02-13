import React from 'react';
import moment from 'moment';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { List } from 'semantic-ui-react';
import { ItemContent } from 'components/shared';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';

const ListItem = ({ item }) => (
    <Link to={`claims/${item.id}`}>
        <ItemContent>
            <List.Header>{item.id}</List.Header>
            <List.Description>
                {moment.unix(item.timestamp).format('DD MM YYYY')}
            </List.Description>
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
