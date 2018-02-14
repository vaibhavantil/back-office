import React from 'react';
import PropTypes from 'prop-types';
import { Table, Header, Segment } from 'semantic-ui-react';
import PayoutRow from './PayoutRow';
import PaymentCreator from './PaymentCreator';
import { getSum } from 'app/lib/helpers';

const PayoutsList = props => (
    <React.Fragment>
        <Segment clearing>
            <Header as="h2" floated="left">
                Payouts
            </Header>
            <Header as="h2" floated="right">
                <PaymentCreator
                    createPayment={props.createPayment}
                    id={props.id}
                />
            </Header>
        </Segment>

        {!props.list.length ? <Header>No payouts</Header> : null}
        {props.list.length ? (
            <React.Fragment>
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
                        {props.list.map(item => (
                        <Table.Row key={item.id}>
                                <PayoutRow {...props} data={item} />
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
            </React.Fragment>
        ) : null}

        <span>Total payed out: {getSum(props.list)} SEK</span>
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
