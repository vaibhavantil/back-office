import React from 'react';
import PropTypes from 'prop-types';
import { Button, Dropdown, List } from 'semantic-ui-react';
import ClaimInfoField from './ClaimInfoField';
import {
    updateTypesList,
    getActiveType,
    getClaimFieldsData
} from 'app/lib/helpers';
export default class ClaimTypeFields extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            fieldsData: {
                optionalData: [],
                requiredData: []
            },
            type: null
        };
    }

    inputHandler = (fieldType, fieldName, input, e, { value }) => {
        const { fieldsData, type } = this.state;
        const newState = getClaimFieldsData(
            fieldsData[fieldType],
            type[fieldType],
            fieldName,
            value
        );
        this.setState({
            fieldsData: {
                ...fieldsData,
                [fieldType]: newState
            }
        });
    };

    submitTypeChanges = () => {
        const { claimId, claimDetailsUpdate, claimTypeUpdate } = this.props;
        const { type, fieldsData } = this.state;
        claimTypeUpdate(claimId, { type: type.name }, 'type');
        claimDetailsUpdate(claimId, fieldsData, 'type');
    };

    typeChangeHandler = (e, { value }) => {
        const { claimId, claimTypeUpdate } = this.props;
        const { type, types } = this.state;
        if (!type) {
            claimTypeUpdate(claimId, { type: value }, 'type');
        } else {
            this.setState({ type: getActiveType(types, value) });
        }
    };

    cleanupField = (fieldType, fieldName) => {
        const { fieldsData } = this.state;
        const newState = fieldsData[fieldType].filter(
            item => item.name !== fieldName
        );
        this.setState({
            fieldsData: {
                ...fieldsData,
                [fieldType]: newState
            }
        });
    };

    getFieldsList = fieldType => {
        const fields = this.state.type[fieldType];
        return fields.map((field, id) => (
            <List.Item key={id}>
                <ClaimInfoField
                    field={field}
                    inputHandler={this.inputHandler.bind(
                        this,
                        fieldType,
                        field.name
                    )}
                    cleanupField={this.cleanupField.bind(
                        this,
                        fieldType,
                        field.name
                    )}
                />
            </List.Item>
        ));
    };

    setActiveType = (types, claimType) => {
        const activeType = getActiveType(types, claimType);
        this.setState({ type: activeType });
    };

    componentWillMount() {
        const { types, claimInfo } = this.props;
        this.setActiveType(types, claimInfo.type);
    }

    componentWillReceiveProps({ types, claimInfo }) {
        if (claimInfo.type) {
            this.setActiveType(types, claimInfo.type);
        }
    }

    render() {
        const { types, claimInfo: { type } } = this.props;
        const updatedTypes = updateTypesList(types.slice());
        return (
            <React.Fragment>
                Type
                <Dropdown
                    onChange={this.typeChangeHandler}
                    options={updatedTypes}
                    placeholder="Type"
                    selection
                    value={type}
                />
                {type && (
                    <React.Fragment>
                        <h3>Required fields:</h3>
                        {this.getFieldsList('requiredData')}
                        <h3>Additional fields:</h3>
                        {this.getFieldsList('optionalData')}
                        <Button primary onClick={this.submitTypeChanges}>
                            Save
                        </Button>
                    </React.Fragment>
                )}
            </React.Fragment>
        );
    }
}

ClaimTypeFields.propTypes = {
    types: PropTypes.array.isRequired,
    claimId: PropTypes.string.isRequired,
    claimInfo: PropTypes.object.isRequired,
    claimTypeUpdate: PropTypes.func.isRequired,
    claimDetailsUpdate: PropTypes.func.isRequired
};
