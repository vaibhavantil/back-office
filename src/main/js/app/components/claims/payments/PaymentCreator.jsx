import React from 'react';
import PropTypes from 'prop-types';
import { Button, Form, Modal } from 'semantic-ui-react';

export default class PaymentCreator extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            note: '',
            amount: '',
            exg: false,
            open: false
        };
    }

    amountInputHandler = (e, { value }) => this.setState({ amount: value });

    noteInputhandler = (e, { value }) => this.setState({ note: value });

    checkboxHandler = () => this.setState({ exg: !this.state.exg });

    createPayment = () => {
        const { createPayment, claimId } = this.props;
        const { exg, amount, note } = this.state;
        createPayment(claimId, { exg, amount, note });
        this.setState({
            note: '',
            amount: '',
            exg: false,
            open: false
        });
    };

    toggleModal = () => this.setState({ open: !this.state.open });

    render() {
        const { open, amount, note, exg } = this.state;
        return (
            <React.Fragment>
                <Button primary onClick={this.toggleModal}>
                    Create
                </Button>
                <Modal
                    size="tiny"
                    dimmer="blurring"
                    open={open}
                    onClose={this.toggleModal}
                >
                    <Modal.Header>Create Payout</Modal.Header>
                    <Modal.Content>
                        <Form onSubmit={this.createPayment}>
                            <Form.Field>
                                <label>Amount</label>
                                <Form.Input
                                    type="number"
                                    placeholder="Amount"
                                    onChange={this.amountInputHandler}
                                    value={amount}
                                />
                            </Form.Field>

                            <Form.Field>
                                <label>Note</label>
                                <Form.Input
                                    type="text"
                                    placeholder="Note"
                                    onChange={this.noteInputhandler}
                                    value={note}
                                />
                            </Form.Field>
                            <Form.Field>
                                <label>Ex gratia</label>
                                <Form.Checkbox
                                    checked={exg}
                                    onChange={this.checkboxHandler}
                                />
                            </Form.Field>
                        </Form>
                    </Modal.Content>
                    <Modal.Actions>
                        <Button
                            primary
                            onClick={this.createPayment}
                            content="Create"
                        />
                    </Modal.Actions>
                </Modal>
            </React.Fragment>
        );
    }
}

PaymentCreator.propTypes = {
    createPayment: PropTypes.func.isRequired,
    claimId: PropTypes.string.isRequired
};
