import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import { Table } from 'semantic-ui-react';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';
import { history } from 'app/store';
import { LinkRow } from 'components/shared';

const MemberInsuranceList = ({ memberInsurance: { list } }) => {
    const getMemberName = member =>
        member.firstName
            ? `${member.firstName} ${member.lastName || ''}`
            : `Member-${member.hid}`;

    const linkClickHandler = id => history.push(`/members/${id}`);

    const getTableRow = item => {
        const date = moment(item.birthDate);
        const formattedDate = date.isValid()
            ? date.format('DD MMMM YYYY')
            : '-';
        return (
            <LinkRow onClick={linkClickHandler.bind(this, item.hid)}>
                <Table.Cell>{getMemberName(item)}</Table.Cell>
                <Table.Cell>{formattedDate}</Table.Cell>
            </LinkRow>
        );
    };

    const getTableHeader = () => {
        return (
            <Table.Header>
                <Table.Row>
                    <Table.HeaderCell>Name</Table.HeaderCell>
                    <Table.HeaderCell>Insurance type</Table.HeaderCell>
                    <Table.HeaderCell>Insurance active from</Table.HeaderCell>
                    <Table.HeaderCell>Insurance status</Table.HeaderCell>
                </Table.Row>
            </Table.Header>
        );
    };

    return (
        <PaginatorList
            list={list}
            itemContent={item => getTableRow(item)}
            tableHeader={getTableHeader()}
            pageSize={25}
        />
    );
};

MemberInsuranceList.propTypes = {
    memberInsurance: PropTypes.object
};

export default MemberInsuranceList;
