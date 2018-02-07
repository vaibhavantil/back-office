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
export default class ClaimInfoField extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            inputIsVisible: false
        };
    }

    toggleInput = () => {
        this.setState({
            inputIsVisible: !this.state.inputIsVisible
        });
    };

    getInput = (type = 'text') => {
        const { fieldData } = this.props;
        const input = claimInputs[type](
            this.props.inputHandler.bind(this, fieldData.name)
        );
        return (
            <div style={{ width: '300px', marginLeft: 'auto' }}>{input}</div>
        );
    };

    render() {
        const { fieldData } = this.props;
        const fieldInput = this.state.inputIsVisible
            ? this.getInput(types[fieldData.type])
            : null;
        return (
            <FieldRow>
                <span>{fieldData.name}</span>
                <Checkbox
                    style={{ marginLeft: '10px' }}
                    onChange={this.toggleInput}
                />
                {fieldInput}
            </FieldRow>
        );
    }
}

ClaimInfoField.propTypes = {
    fieldData: PropTypes.object.isRequired,
    inputHandler: PropTypes.func.isRequired
};
