import React from 'react'
import { Query } from 'react-apollo'
import gql from 'graphql-tag'
import { Table } from 'semantic-ui-react'

import { history } from 'app/store'

const query = gql`
query MonthlyPaymentsQuery($year: Int!, $month: Int!) {
    getMonthlyPayments(year: $year, month: $month) {
        memberId
        directDebitStatus
        price
    }
}
`

const Checkmark = () => (
    <i className="fas fa-check-circle" style={{color:'#00ff00'}} />
)

const Cross = () => (
    <i className="fas fa-times-circle" style={{color:'#ff0000'}}/>
)

const goToMember = (memberId) => () => history.push(`/members/${memberId}`)

const MonthlyPaymentsTable = ({monthlyPayments}) => (
    <Table celled selectable compact>
        <Table.Header>
            <Table.Row>
                <Table.HeaderCell>memberId</Table.HeaderCell>
                <Table.HeaderCell>Subscription cost</Table.HeaderCell>
                <Table.HeaderCell>Direct Debit activated?</Table.HeaderCell>
            </Table.Row>
        </Table.Header>
        <Table.Body>
            {monthlyPayments.map(monthlyPayment => (
                <Table.Row key={`${monthlyPayment.memberId}:${monthlyPayment.price}`}>
                    <Table.Cell onClick={goToMember(monthlyPayment.memberId)}>{monthlyPayment.memberId}</Table.Cell>
                    <Table.Cell>{monthlyPayment.price} SEK</Table.Cell>
                    <Table.Cell>{monthlyPayment.directDebitStatus ? <Checkmark /> : <Cross /> }</Table.Cell>
                </Table.Row>
            ))}
        </Table.Body>
    </Table>
)

class Payment extends React.Component {
    constructor(props) {
        super(props);
        this.variables = { year: 2018, month: 7 }; // TODO Generate this based on actual dates and allow user to check different dates
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
