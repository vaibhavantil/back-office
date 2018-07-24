import React from 'react'
import gql from 'graphql-tag'
import { Query, Mutation } from 'react-apollo'
import { Table, Form, Button } from 'semantic-ui-react'

import { Checkmark, Cross } from 'components/icons';

const query = gql`
query GetMemberTransactions($id: ID!) {
    getMember(id: $id) {
        directDebitStatus {
            activated
        }
        transactions {
            id
            amount
            timestamp
            type
            status
        }
    }
}
`

const chargeMemberMutation = gql`
mutation ChargeMember($id: ID!, $amount: MonetaryAmount!) {
    chargeMember(id: $id, amount: $amount) {
        transactions {
            id
            amount
            timestamp
            type
            status
        }
    }
}
`

const MemberTransactionsTable = ({transactions}) => (
    <Table celled selectable compact>
        <Table.Header>
            <Table.Row>
                <Table.HeaderCell>ID</Table.HeaderCell>
                <Table.HeaderCell>Amount</Table.HeaderCell>
                <Table.HeaderCell>Timestamp</Table.HeaderCell>
                <Table.HeaderCell>Type</Table.HeaderCell>
                <Table.HeaderCell>Status</Table.HeaderCell>
            </Table.Row>
        </Table.Header>
        <Table.Body>
            {transactions.map(transaction => (
                <Table.Row key={transaction.id}>
                    <Table.Cell>{transaction.id}</Table.Cell>
                    <Table.Cell>{transaction.amount.amount} {transaction.amount.currency}</Table.Cell>
                    <Table.Cell>{transaction.timestamp}</Table.Cell>
                    <Table.Cell>{transaction.type}</Table.Cell>
                    <Table.Cell>{transaction.status}</Table.Cell>
                </Table.Row>
            ))}
        </Table.Body>
    </Table>
)

class PaymentsTab extends React.Component {
    constructor(props) {
        super(props)
        this.variables = {id: props.match.params.id}
    }

    handleChargeSubmit = (mutation) => (e) => {
        e.preventDefault();
        debugger // eslint-disable-line
    }

    render() {
        return (
            <React.Fragment>
                <Query query={query} variables={this.variables}>
                    {({loading, error, data}) => {
                        if (error) {
                            return (<div>Error!</div>)
                        }

                        if (loading || !data) {
                            return (<div>Loading...</div>)
                        }

                        return (
                            <div>
                                <p>Direct Debit activated: {data.getMember.directDebitStatus.activated ? <Checkmark /> : <Cross />}</p>
                                {data.getMember.directDebitStatus.activated || (
                                    <Mutation mutation={chargeMemberMutation}>
                                        {(chargeMember) => (
                                            <div>
                                                <Form onSubmit={this.handleChargeSubmit(chargeMember)}>
                                                    <Form.Input label="Amount" placeholder="ex. 100" />
                                                    <Button type="submit">Charge</Button>
                                                </Form>
                                            </div>
                                        )}
                                    </Mutation>
                                )}
                                <p>Transactions:</p>
                                <MemberTransactionsTable transactions={data.getMember.transactions} />
                            </div>
                        )
                    }}
                </Query>
            </React.Fragment>
        );
    }
}

export default PaymentsTab