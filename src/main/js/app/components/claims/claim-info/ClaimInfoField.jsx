import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Checkbox } from 'semantic-ui-react';
import claimInputs from './ClaimInputs';
import * as types from 'app/lib/claimFieldsTypes';

const FieldRow = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    width: 500px;
    min-height: 60px;
`;

const InputContainer = styled.div`
    width: 300px;
    margin-left: auto;
`;

export default class ClaimInfoField extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            inputIsVisible: false
        };
    }

    toggleInput = () => {
        this.setState(
            {
                inputIsVisible: !this.state.inputIsVisible
            },
            () => {
                if (!this.state.inputIsVisible) {
                    const { field: { name }, cleanupField } = this.props;
                    cleanupField(name);
                }
            }
        );
    };

    getInput = (type = 'text') => {
        const { field, inputHandler } = this.props;
        const input = claimInputs[type](
            inputHandler.bind(this, field.name),
            false,
            field.value
        );
        return <InputContainer>{input}</InputContainer>;
    };

    render() {
        const { field, required } = this.props;
        const { inputIsVisible } = this.state;
        return (
            <React.Fragment>
                <FieldRow>
                    {!required && (
                        <Checkbox
                            label={field.name}
                            onChange={this.toggleInput}
                        />
                    )}
                    {inputIsVisible || required ? (
                        <React.Fragment>
                            <span>{field.name}</span>
                            {this.getInput(types[field.type])}
                        </React.Fragment>
                    ) : null}
                </FieldRow>
            </React.Fragment>
        );
    }
}

ClaimInfoField.propTypes = {
    field: PropTypes.object.isRequired,
    inputHandler: PropTypes.func.isRequired,
    cleanupField: PropTypes.func.isRequired,
    required: PropTypes.bool
};