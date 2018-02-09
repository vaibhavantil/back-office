import React from 'react';
import PropTypes from 'prop-types';
import { Button, Form, Segment } from 'semantic-ui-react';
import PayoutsList from './PayoutsList';
import PaymentCreator from './PaymentCreator';

export default class Payments extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            resume: '',
            editDisabled: true
        };
    }

    editClickHandler = () => {
        this.setState({ editDisabled: false });
    };

    updateResume = () => {
        const { id, updateResume } = this.props;

        updateResume(id, { resume: this.state.resume });
        this.setState({
            editDisabled: true
        });
    };

    resumeChangeHandler = (e, { value }) => {
        this.setState({ resume: value });
    };

    componentWillReceiveProps({ claimDetails }) {
        if (claimDetails.data) {
            this.setState({ resume: claimDetails.data.resume });
        }
    }

    render() {
        const { claimDetails, createPayment, notes, id } = this.props;
        const { resume, editDisabled } = this.state;
        return (
            <Segment>
                <Form>
                    <Form.Group>
                        <label>Resume </label>
                        <Form.Input
                            type="number"
                            value={resume}
                            disabled={editDisabled}
                            onChange={this.resumeChangeHandler}
                        />

                        {editDisabled ? (
                            <Button onClick={this.editClickHandler}>
                                Edit
                            </Button>
                        ) : (
                            <Button onClick={this.updateResume}>Save</Button>
                        )}
                    </Form.Group>
                </Form>
                <PayoutsList list={claimDetails.payments} />
                <h2>
                    Total payed out:
                    {claimDetails.data && claimDetails.data.total}
                </h2>
                <PaymentCreator
                    createPayment={createPayment}
                    notes={notes}
                    claimId={id}
                />
            </Segment>
        );
    }
}

Payments.propTypes = {
    claimDetails: PropTypes.object.isRequired,
    id: PropTypes.string.isRequired,
    updateResume: PropTypes.func.isRequired,
    createPayment: PropTypes.func.isRequired,
    notes: PropTypes.array
};
