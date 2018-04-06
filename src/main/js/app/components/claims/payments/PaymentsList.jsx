import React from 'react';
import PropTypes from 'prop-types';
import { Table, Header, Segment } from 'semantic-ui-react';
import PaymentRow from './PaymentRow';
import PaymentCreator from './PaymentCreator';

const PayoutsList = props => (
    <React.Fragment>
        <Segment clearing>
            <Header as="h2" floated="left">
                Payments
            </Header>
            <Header as="h2" floated="right">
                <PaymentCreator
                    createPayment={props.createPayment}
                    id={props.id}
                />
            </Header>

            {!props.list.length ? <Header>No payments</Header> : null}
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
                            {props.list.map((item, id) => (
                                <Table.Row key={item.id || id}>
                                    <PaymentRow {...props} data={item} />
                                </Table.Row>
                            ))}
                        </Table.Body>
                    </Table>
                </React.Fragment>
            ) : null}

            <span>Total payed out: {props.sum || 0} SEK</span>
        </Segment>
    </React.Fragment>
);

PayoutsList.propTypes = {
    list: PropTypes.array.isRequired,
    createPayment: PropTypes.func.isRequired,
    id: PropTypes.string.isRequired,
    sum: PropTypes.number
};

export default PayoutsList;
