import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import { Table } from 'semantic-ui-react';
import PaginatorList from 'components/shared/paginator-list/PaginatorList';
import { history } from 'app/store';
import { LinkRow } from 'components/shared';

export default class MemberInsuranceList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            column: null,
            direction: null
        };
    }

    getMemberName = member =>
        member.memberFirstName
            ? `${member.memberFirstName} ${member.memberLastName || ''}`
            : `${member.memberId ? 'Member-' + member.memberId : 'No id'}`;

    linkClickHandler = id => {
        history.push(`/members/${id}`, { to: 'insurance' });
    };

    getTableRow = item => {
        const date = moment(item.insuranceActiveFrom);
        const formattedDate = date.isValid()
            ? date.format('DD MMMM YYYY')
            : '-';
        return (
            <LinkRow onClick={this.linkClickHandler.bind(this, item.memberId)}>
                <Table.Cell>{this.getMemberName(item)}</Table.Cell>
                <Table.Cell>{item.insuranceType}</Table.Cell>
                <Table.Cell>{formattedDate}</Table.Cell>
                <Table.Cell>{item.insuranceStatus}</Table.Cell>
                <Table.Cell>
                    {item.cancellationEmailSent
                        ? item.cancellationEmailSent.toString()
                        : '-'}
                </Table.Cell>
                <Table.Cell>
                    {item.certificateUploaded
                        ? item.certificateUploaded.toString()
                        : '-'}
                </Table.Cell>
                <Table.Cell>
                    {item.personsInHouseHold
                        ? item.personsInHouseHold.toString()
                        : '-'}
                </Table.Cell>
            </LinkRow>
        );
    };

    sortTable = clickedColumn => {
        const { column, direction } = this.state;

        if (column !== clickedColumn) {
            this.setState({
                column: clickedColumn,
                direction: 'ascending'
            });
            this.props.sortMemberInsList(clickedColumn, false);
            return;
        }

        this.setState(
            {
                direction:
                    direction === 'ascending' ? 'descending' : 'ascending'
            },
            () => {
                this.props.sortMemberInsList(
                    clickedColumn,
                    this.state.direction === 'descending'
                );
            }
        );
    };

    getTableHeader = () => {
        const { column, direction } = this.state;
        return (
            <Table.Header>
                <Table.Row>
                    <Table.HeaderCell
                        width={3}
                        sorted={column === 'name' ? direction : null}
                        onClick={this.sortTable.bind(this, 'name')}
                    >
                        Name
                    </Table.HeaderCell>
                    <Table.HeaderCell
                        width={2}
                        sorted={column === 'insuranceType' ? direction : null}
                        onClick={this.sortTable.bind(this, 'insuranceType')}
                    >
                        Insurance type
                    </Table.HeaderCell>
                    <Table.HeaderCell
                        width={2}
                        sorted={column === 'date' ? direction : null}
                        onClick={this.sortTable.bind(this, 'date')}
                    >
                        Insurance active from
                    </Table.HeaderCell>
                    <Table.HeaderCell
                        width={2}
                        sorted={column === 'insuranceStatus' ? direction : null}
                        onClick={this.sortTable.bind(this, 'insuranceStatus')}
                    >
                        Insurance status
                    </Table.HeaderCell>
                    <Table.HeaderCell
                        width={2}
                        sorted={
                            column === 'cancellationEmailSent'
                                ? direction
                                : null
                        }
                        onClick={this.sortTable.bind(
                            this,
                            'cancellationEmailSent'
                        )}
                    >
                        Cancellation email sent
                    </Table.HeaderCell>
                    <Table.HeaderCell
                        width={2}
                        sorted={
                            column === 'certificateUploaded'
                                ? direction
                                : null
                        }
                        onClick={this.sortTable.bind(
                            this,
                            'certificateUploaded'
                        )}
                    >
                        Certificate uploaded
                    </Table.HeaderCell>
                    <Table.HeaderCell
                        width={2}
                        sorted={
                            column === 'personsInHouseHold' ? direction : null
                        }
                        onClick={this.sortTable.bind(
                            this,
                            'personsInHouseHold'
                        )}
                    >
                        Persons in house hold
                    </Table.HeaderCell>
                </Table.Row>
            </Table.Header>
        );
    };
    render() {
        const {
            memberInsurance: { list }
        } = this.props;

        return (
            <PaginatorList
                list={list}
                itemContent={item => this.getTableRow(item)}
                tableHeader={this.getTableHeader()}
                pageSize={20}
                isSortable={true}
                keyName="productId"
            />
        );
    }
}

MemberInsuranceList.propTypes = {
    memberInsurance: PropTypes.object.isRequired,
    sortMemberInsList: PropTypes.func.isRequired
};
