import React from 'react';
import moment from 'moment';
import PropTypes from 'prop-types';
import { Table } from 'semantic-ui-react';
import { LinkRow } from 'components/shared';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';
import { history } from 'app/store';

const linkClickHandler = id => history.replace(`/claims/${id}`);

export const TableRow = ({ item }) => {
    const date = moment(item.date);
    const formattedDate = date.isValid()
        ? date.format('HH:mm DD MMMM YYYY')
        : '-';
    return (
        <LinkRow onClick={linkClickHandler.bind(this, item.id)}>
            <Table.Cell>{formattedDate}</Table.Cell>
            <Table.Cell>{item.type}</Table.Cell>
            <Table.Cell>{item.state}</Table.Cell>
            <Table.Cell>{item.reserve}</Table.Cell>
        </LinkRow>
    );
};

export const TableHeader = () => (
    <Table.Header>
        <Table.Row>
            <Table.HeaderCell>Date</Table.HeaderCell>
            <Table.HeaderCell>Type</Table.HeaderCell>
            <Table.HeaderCell>Status</Table.HeaderCell>
            <Table.HeaderCell>Reserves</Table.HeaderCell>
        </Table.Row>
    </Table.Header>
);

TableRow.propTypes = {
    item: PropTypes.object.isRequired
};

const ClaimsList = ({ claims }) => (
    <PaginatorList
        list={claims}
        itemContent={item => <TableRow item={item} />}
        tableHeader={<TableHeader />}
        keyName="id"
    />
);

ClaimsList.propTypes = {
    claims: PropTypes.array.isRequired
};

export default ClaimsList;
