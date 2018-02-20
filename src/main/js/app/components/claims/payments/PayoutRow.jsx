import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import { Checkbox, Table } from 'semantic-ui-react';

const PayoutRow = ({ data }) => (
    <React.Fragment>
        <Table.Cell>{data.amount}</Table.Cell>
        <Table.Cell>{data.note}</Table.Cell>
        <Table.Cell>{moment.unix(data.date).format('DD MM YYYY')}</Table.Cell>
        <Table.Cell>
            <Checkbox checked={data.exg} disabled />
        </Table.Cell>
    </React.Fragment>
);

PayoutRow.propTypes = {
    data: PropTypes.object.isRequired
};

export default PayoutRow;
