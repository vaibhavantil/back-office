import React from 'react';
import PropTypes from 'prop-types';
import { Table, Header } from 'semantic-ui-react';

const DetailsTab = ({ user }) =>
    user ? (
        <Table selectable>
            <Table.Body>
                {Object.keys(user).map((field, id) => (
                    <Table.Row key={id}>
                        <Table.Cell>{field}</Table.Cell>
                        <Table.Cell>{user[field]}</Table.Cell>
                    </Table.Row>
                ))}
            </Table.Body>
        </Table>
    ) : (
        <Header>No member info </Header>
    );

DetailsTab.propTypes = {
    user: PropTypes.object
};

export default DetailsTab;
