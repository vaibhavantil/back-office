import React from 'react';
import PropTypes from 'prop-types';
import { Table, Header, Segment } from 'semantic-ui-react';
import PayoutRow from './PayoutRow';
import PaymentCreator from './PaymentCreator';

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

        <span>Total payed out: {props.sum || 0} SEK</span>
    </React.Fragment>
);

PayoutsList.propTypes = {
    list: PropTypes.array.isRequired,
    createPayment: PropTypes.func.isRequired,
    id: PropTypes.string.isRequired,
    sum: PropTypes.number
};

export default PayoutsList;
