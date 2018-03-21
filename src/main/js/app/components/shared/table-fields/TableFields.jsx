import React from 'react';
import PropTypes from 'prop-types';
import { Table } from 'semantic-ui-react';
import { getFieldName, getFieldValue } from 'app/lib/helpers';

const TableFields = ({ fields }) => (
    <React.Fragment>
        {Object.keys(fields).map((field, id) => (
            <Table.Row key={id}>
                <Table.Cell>{getFieldName(field)}</Table.Cell>
                <Table.Cell>{getFieldValue(fields[field])}</Table.Cell>
            </Table.Row>
        ))}
    </React.Fragment>
);

TableFields.propTypes = {
    fields: PropTypes.object.isRequired
};

export default TableFields;
