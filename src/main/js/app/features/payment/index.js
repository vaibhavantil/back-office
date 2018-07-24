import React from 'react'
import { Query } from 'react-apollo'
import gql from 'graphql-tag'
import { Table } from 'semantic-ui-react'
import moment from 'moment'

import { history } from 'app/store'
import { Checkmark, Cross } from 'components/icons'

const query = gql`
query MonthlyPaymentsQuery($month: YearMonth!) {
    getMonthlyPayments(month: $month) {
        amount
        member {
            memberId
            firstName
            lastName
            directDebitStatus {
                activated
            }
        }
    }
}
`

const goToMember = (memberId) => () => history.push(`/members/${memberId}`)

const MonthlyPaymentsTable = ({monthlyPayments}) => (
    <Table celled selectable compact>
        <Table.Header>
            <Table.Row>
                <Table.HeaderCell>memberId</Table.HeaderCell>
                <Table.HeaderCell>Full name</Table.HeaderCell>
                <Table.HeaderCell>Subscription cost</Table.HeaderCell>
                <Table.HeaderCell>Direct Debit activated?</Table.HeaderCell>
            </Table.Row>
        </Table.Header>
        <Table.Body>
            {monthlyPayments.map(monthlyPayment => (
                <Table.Row key={`${monthlyPayment.member.memberId}:${monthlyPayment.amount.amount}`}>
                    <Table.Cell onClick={goToMember(monthlyPayment.member.memberId)}>{monthlyPayment.member.memberId}</Table.Cell>
                    <Table.Cell>{monthlyPayment.member.firstName} {monthlyPayment.member.lastName}</Table.Cell>
                    <Table.Cell>{monthlyPayment.amount.amount} {monthlyPayment.amount.currency}</Table.Cell>
                    <Table.Cell>{monthlyPayment.member.directDebitStatus.activated ? <Checkmark /> : <Cross /> }</Table.Cell>
                </Table.Row>
            ))}
        </Table.Body>
    </Table>
)

class Payment extends React.Component {
    constructor(props) {
        super(props);
        this.variables = { month: moment().format('YYYY-MM')};
    }

    render(){
        return (
            <Query query={query} variables={this.variables}>
                {({loading, error, data}) => {
                    if (error) {
                        return (<div>Error!</div>)
                    }
                    if (loading || !data) {
                        return (<div>Loading...</div>)
                    }

                    return (<MonthlyPaymentsTable monthlyPayments={data.getMonthlyPayments} />)
                }}
            </Query>
        )
    }
}

export default Payment
