import React from 'react';
import PropTypes from 'prop-types';
import { Header, Table } from 'semantic-ui-react';
import TableFields from 'components/shared/table-fields/TableFields';

const DetailsTab = ({ messages: { member } }) => {
    return member ? (
        <Table selectable>
            <Table.Body>
                <TableFields fields={member} />
            </Table.Body>
        </Table>
    ) : (
        <Header>No member info</Header>
    );
};

DetailsTab.propTypes = {
    messages: PropTypes.object
};

export default DetailsTab;
