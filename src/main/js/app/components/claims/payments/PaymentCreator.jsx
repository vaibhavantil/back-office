import React from 'react';
import PropTypes from 'prop-types';
import { Form, Segment } from 'semantic-ui-react';

export default class PaymentCreator extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            note: '',
            amount: '',
            exg: false
        };
    }

    amountInputHandler = (e, { value }) => {
        this.setState({ amount: value });
    };

    noteInputhandler = (e, { value }) => {
        this.setState({ note: value });
    };

    checkboxHandler = () => {
        this.setState({ exg: !this.state.exg });
    };

    createPayment = () => {
        const { createPayment, claimId } = this.props;
        const { exg, amount, note } = this.state;
        createPayment(claimId, { exg, amount, note });
    };

    render() {
        return (
            <Segment>
                <Form>
                    <Form.Group>
                        <Form.Input
                            type="number"
                            placeholder="Amount"
                            onChange={this.amountInputHandler}
                            value={this.state.amount}
                        />
                        <Form.Input
                            type="text"
                            placeholder="Note"
                            onChange={this.noteInputhandler}
                            value={this.state.note}
                        />
                    </Form.Group>
                    <Form.Group>
                        <label>Ex gratia</label>
                        <Form.Checkbox
                            checked={this.state.exg}
                            onChange={this.checkboxHandler}
                        />
                    </Form.Group>

                    <Form.Button
                        primary
                        onClick={this.createPayment}
                        content="Create"
                    />
                </Form>
            </Segment>
        );
    }
}

PaymentCreator.propTypes = {
    createPayment: PropTypes.func.isRequired,
    claimId: PropTypes.string.isRequired
};
