import React from "react";
import gql from "graphql-tag";
import { Query, Mutation } from "react-apollo";
import { Table, Form, Button, Input } from "semantic-ui-react";
import moment from "moment";

import { Checkmark, Cross } from "components/icons";

const transactionDateSorter = (a, b) => {
  const aDate = new Date(a.timestamp);
  const bDate = new Date(b.timestamp);

  if (aDate > bDate) {
    return 1;
  }
  if (bDate > aDate) {
    return -1;
  }
  return 0;
};

const GET_MEMBER_QUERY = gql`
  query GetMemberTransactions(
    $id: ID!
    $currentMonth: YearMonth!
    $previousMonth: YearMonth!
  ) {
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
      currentMonth: monthlySubscription(month: $currentMonth) {
        amount
      }
      previousMonth: monthlySubscription(month: $previousMonth) {
        amount
      }
    }
  }
`;

const CHARGE_MEMBER_MUTATION = gql`
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
`;

const MemberTransactionsTable = ({ transactions }) => (
  <Table celled selectable compact striped>
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
          <Table.Cell>
            {transaction.amount.amount} {transaction.amount.currency}
          </Table.Cell>
          <Table.Cell>
            {moment(transaction.timestamp).format("YYYY-MM-DD HH:mm:ss")}
          </Table.Cell>
          <Table.Cell>{transaction.type}</Table.Cell>
          <Table.Cell>{transaction.status}</Table.Cell>
        </Table.Row>
      ))}
    </Table.Body>
  </Table>
);

class PaymentsTab extends React.Component {
  constructor(props) {
    super(props);
    this.variables = {
      id: props.match.params.id,
      currentMonth: moment().format("YYYY-MM"),
      previousMonth: moment()
        .subtract(1, "month")
        .format("YYYY-MM")
    };
    this.state = {
      amount: null,
      confirming: false,
      confirmed: false
    };
  }

  handleChange = e => {
    this.setState({ amount: e.target.value });
  };

  handleChargeSubmit = mutation => () => {
    mutation({
      variables: {
        id: this.variables.id,
        amount: {
          amount: +this.state.amount,
          currency: "SEK"
        }
      }
    });
    this.setState({ amount: null, confirming: false, confirmed: false });
  };

  handleConfirmation = () => {
    this.setState({ confirming: true });
  };

  handleConfirmationChange = e => {
    if (e.target.value.replace(/ /g, "").toLowerCase() === "tech") {
      this.setState({ confirming: false, confirmed: true });
    }
  };

  handleUpdate = (cache, result) => {
    const { transactions } = result.data.chargeMember;
    cache.writeQuery({
      query: GET_MEMBER_QUERY,
      data: {
        getMember: {
          transactions
        }
      }
    });
  };

  render() {
    return (
      <React.Fragment>
        <Query query={GET_MEMBER_QUERY} variables={this.variables}>
          {({ loading, error, data }) => {
            if (error) {
              return <div>Error!</div>;
            }

            if (loading || !data) {
              return <div>Loading...</div>;
            }

            return (
              <div>
                <p>
                  Direct Debit activated:{" "}
                  {data.getMember.directDebitStatus.activated ? (
                    <Checkmark />
                  ) : (
                    <Cross />
                  )}
                </p>
                <p>
                  Subscrtiption cost for this month({
                    this.variables.currentMonth
                  }) is : {data.getMember.currentMonth.amount.amount}{" "}
                  {data.getMember.currentMonth.amount.currency}
                </p>
                <p>
                  Subscrtiption cost for the previous month ({
                    this.variables.previousMonth
                  }) is : {data.getMember.previousMonth.amount.amount}{" "}
                  {data.getMember.previousMonth.amount.currency}
                </p>
                {data.getMember.directDebitStatus.activated && (
                  <Mutation
                    mutation={CHARGE_MEMBER_MUTATION}
                    update={this.handleUpdate}
                  >
                    {chargeMember => (
                      <div>
                        <Form>
                          <Form.Input
                            onChange={this.handleChange}
                            label="Amount"
                            placeholder="ex. 100"
                          />
                          <br />
                          {!this.state.confirmed && (
                            <Button onClick={this.handleConfirmation}>
                              Charge
                            </Button>
                          )}
                          {this.state.confirming && (
                            <React.Fragment>
                              <br />
                              <br />
                              <Input
                                onChange={this.handleConfirmationChange}
                                focus
                                label="Type tech to confirm"
                                placeholder="tech"
                              />
                              <br />
                            </React.Fragment>
                          )}
                          {this.state.confirmed && (
                            <React.Fragment>
                              Success!! Press execute, to execute the order
                              <br />
                              <br />
                              <Button
                                positive
                                onClick={this.handleChargeSubmit(chargeMember)}
                              >
                                Execute
                              </Button>
                            </React.Fragment>
                          )}
                        </Form>
                      </div>
                    )}
                  </Mutation>
                )}
                <br />
                <p>Transactions:</p>
                <MemberTransactionsTable
                  transactions={data.getMember.transactions
                    .slice()
                    .sort(transactionDateSorter)
                    .reverse()}
                />
              </div>
            );
          }}
        </Query>
      </React.Fragment>
    );
  }
}

export default PaymentsTab;
