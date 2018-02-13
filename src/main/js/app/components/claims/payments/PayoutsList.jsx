import React from 'react';
import PropTypes from 'prop-types';
import { Table, Header } from 'semantic-ui-react';
import { getSum } from 'app/lib/helpers';
import PayoutRow from './PayoutRow';
import PaymentCreator from './PaymentCreator';

const PayoutsList = ({
    list,
    updatePayment,
    createPayment,
    removePayment,
    id
}) => (
    <React.Fragment>
        <Header as="h2" floated="left">
            Payouts
        </Header>
        <Header as="h2" floated="right">
            <PaymentCreator createPayment={createPayment} claimId={id} />
        </Header>
        {list.length ? (
            <Table>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell>Amount</Table.HeaderCell>
                        <Table.HeaderCell>Note</Table.HeaderCell>
                        <Table.HeaderCell>Date</Table.HeaderCell>
                        <Table.HeaderCell>Ex Gratia</Table.HeaderCell>
                        <Table.HeaderCell> </Table.HeaderCell>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {list.map(item => (
                        <Table.Row key={item.id}>
                            <PayoutRow
                                data={item}
                                updatePayment={updatePayment}
                                id={id}
                                removePayment={removePayment}
                            />
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
        ) : (
            <span>----</span>
        )}

        <span>Total payed out: {list.length && getSum(list)} SEK</span>
    </React.Fragment>
);

PayoutsList.propTypes = {
    list: PropTypes.array.isRequired,
    updatePayment: PropTypes.func.isRequired,
    createPayment: PropTypes.func.isRequired,
    removePayment: PropTypes.func.isRequired,
    id: PropTypes.string.isRequired
};

export default PayoutsList;
