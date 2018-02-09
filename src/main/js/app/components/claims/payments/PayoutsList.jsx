import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import { Checkbox, Table } from 'semantic-ui-react';

const RowCells = ({ data }) => (
    <React.Fragment>
        <Table.Cell>{data.amount}</Table.Cell>
        <Table.Cell>{data.note}</Table.Cell>
        <Table.Cell>{moment.unix(data.date).format('DD MM YYYY')}</Table.Cell>
        <Table.Cell>
            <Checkbox checked={data.exg} disabled />
        </Table.Cell>
    </React.Fragment>
);

RowCells.propTypes = {
    data: PropTypes.object.isRequired
};

const PayoutsList = ({ list }) => (
    <React.Fragment>
        <h2>Payouts List:</h2>
        {list.length ? (
            <Table>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell>Amount</Table.HeaderCell>
                        <Table.HeaderCell>Note</Table.HeaderCell>
                        <Table.HeaderCell>Date</Table.HeaderCell>
                        <Table.HeaderCell>Exg</Table.HeaderCell>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {list.map((item, id) => (
                        <Table.Row key={item.id || id}>
                            <RowCells data={item} />
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
        ) : (
            <span>----</span>
        )}
    </React.Fragment>
);

PayoutsList.propTypes = {
    list: PropTypes.array.isRequired
};

export default PayoutsList;
