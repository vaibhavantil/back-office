import React from 'react';
import PropTypes from 'prop-types';
import { Button, Header, Table } from 'semantic-ui-react';
import TableFields from 'components/shared/table-fields/TableFields';

const InsuranceTab = ({ insurance: { data }, messages: { user } }) => {
    const downloadClick = () => {
        //eslint-disable-next-line
        window.open(`/api/member/mandate/${user.hid}`);
    };
    return data ? (
        <Table selectable>
            <Table.Body>
                <Table.Row>
                    <Table.Cell>Insurance Mandate</Table.Cell>
                    <Table.Cell>
                        <Button onClick={downloadClick}>Download</Button>
                    </Table.Cell>
                </Table.Row>
                <TableFields fields={data} />
            </Table.Body>
        </Table>
    ) : (
        <Header>No insurance info </Header>
    );
};

InsuranceTab.propTypes = {
    insurance: PropTypes.object.isRequired,
    messages: PropTypes.object.isRequired
};

export default InsuranceTab;
