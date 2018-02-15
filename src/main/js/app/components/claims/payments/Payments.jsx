import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Button, Form, Segment } from 'semantic-ui-react';
import PayoutsList from './PayoutsList';

const Label = styled.label`
    display: flex;
    align-items: center;
    font-weight: 700;
    font-size: 13px;
`;
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

    componentWillReceiveProps({ claimDetails: { data } }) {
        if (data && data.resume) {
            this.setState({ resume: data.resume });
        }
    }

    render() {
        const { claimDetails } = this.props;
        const { resume, editDisabled } = this.state;
        return (
            <Segment>
                <Form>
                    <Form.Group>
                        <Label>Reserves: </Label>
                        <Form.Input
                            type="number"
                            value={resume}
                            disabled={editDisabled}
                            onChange={this.resumeChangeHandler}
                        />

                        {editDisabled ? (
                            <Button
                                onClick={this.editClickHandler}
                                content="Edit"
                            />
                        ) : (
                            <Button
                                primary
                                onClick={this.updateResume}
                                content="Save"
                            />
                        )}
                    </Form.Group>
                </Form>

                <PayoutsList {...this.props} list={claimDetails.payments} />
            </Segment>
        );
    }
}

Payments.propTypes = {
    claimDetails: PropTypes.object.isRequired,
    id: PropTypes.string.isRequired,
    updateResume: PropTypes.func.isRequired,
    createPayment: PropTypes.func.isRequired,
    updatePayment: PropTypes.func.isRequired,
    removePayment: PropTypes.func.isRequired,
    notes: PropTypes.array
};
