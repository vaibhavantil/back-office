import React from 'react';
import PropTypes from 'prop-types';
import { Button, Header, Table } from 'semantic-ui-react';

const DetailsTab = ({ user }) => {
    const downloadClick = () => {
        //eslint-disable-next-line
        window.open(`/api/member/mandate/${user.hid}`);
    };

    return user ? (
        <Table selectable>
            <Table.Body>
                <Table.Row>
                    <Table.Cell>Insurance Mandate</Table.Cell>
                    <Table.Cell>
                        <Button onClick={downloadClick}>Download</Button>
                    </Table.Cell>
                </Table.Row>
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
};

DetailsTab.propTypes = {
    user: PropTypes.object
};

export default DetailsTab;
