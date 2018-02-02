import React from 'react';
import styled from 'styled-components';
import { Button, Input } from 'semantic-ui-react';
import PayoutsList from './PayoutsList';

const PaymentsContainer = styled.div`
    dispaly: flex;
    flex-direction: column;
    margin: 100px;
    padding: 30px;
    border: solid 1px #ccc;
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

    componentWillReceiveProps({ claimDetails }) {
        if (claimDetails.data) {
            this.setState({ resume: claimDetails.data.resume });
        }
    }

    render() {
        const { claimDetails: { payments } } = this.props;
        const { resume, editDisabled } = this.state;
        return (
            <PaymentsContainer>
                <div>
                    <Input
                        value={resume}
                        disabled={editDisabled}
                        onChange={this.resumeChangeHandler}
                    />

                    {editDisabled ? (
                        <Button onClick={this.editClickHandler}>Edit</Button>
                    ) : (
                        <Button onClick={this.updateResume}>Save</Button>
                    )}
                </div>
                <PayoutsList list={payments} />
                <span>Total payed out: {321}</span>
            </PaymentsContainer>
        );
    }
}
