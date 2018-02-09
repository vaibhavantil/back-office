import React from 'react';
import PropTypes from 'prop-types';
import { Button, Dropdown, List } from 'semantic-ui-react';
import ClaimInfoField from './ClaimInfoField';
import { fieldsToArray } from 'app/lib/helpers';
export default class ClaimTypeFields extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            fieldsData: {
                required: {},
                additional: {}
            },
            type: null,
            types: []
        };
    }

    inputHandler = (fieldType, name, input, e, { value }) => {
        const stateCopy = { ...this.state.fieldsData };
        stateCopy[fieldType][name] = value;
        this.setState({ fieldsData: stateCopy });
    };

    getActiveType = typeName => {
        return this.state.types.find(item => item.name === typeName);
    };

    submitTypeChanges = () => {
        const { claimId, claimDetailsUpdate, claimTypeUpdate } = this.props;
        const { type, fieldsData } = this.state;
        claimTypeUpdate(claimId, { type: type.name }, 'type');
        claimDetailsUpdate(claimId, fieldsData, 'type');
    };

    typeChangeHandler = (e, { value }) => {
        const { claimId, claimTypeUpdate } = this.props;
        const { type } = this.state;
        if (!type) {
            claimTypeUpdate(claimId, { type: value }, 'type');
        } else {
            this.setState({ type: this.getActiveType(value) });
        }
    };

    cleanupField = (fieldType, fieldName) => {
        const stateCopy = { ...this.state.fieldsData };
        delete stateCopy[fieldType][fieldName];
        this.setState({ fieldsData: stateCopy });
    };

    getFieldsList = fieldType => {
        const fields = this.state.type[fieldType];
        return fields.map((field, id) => (
            <List.Item key={id}>
                <ClaimInfoField
                    field={field}
                    inputHandler={this.inputHandler.bind(this, fieldType)}
                    cleanupField={this.cleanupField.bind(this, fieldType)}
                    required={fieldType === 'required'}
                />
            </List.Item>
        ));
    };

    componentWillMount() {
        const { claimInfo, types } = this.props;
        let updatedTypesList = [...types];
        if (claimInfo.details && claimInfo.type) {
            updatedTypesList = updatedTypesList.map(
                item =>
                    item.name === claimInfo.type
                        ? {
                              ...item,
                              ...fieldsToArray(claimInfo.details, {
                                  additional: item.additional,
                                  required: item.required
                              })
                          }
                        : item
            );
        }
        this.setState({ types: updatedTypesList }, () => {
            this.setState({
                type: this.getActiveType(claimInfo.type)
            });
        });
    }

    render() {
        const { type, types } = this.state;
        return (
            <React.Fragment>
                Type
                <Dropdown
                    onChange={this.typeChangeHandler}
                    options={types}
                    placeholder="Type"
                    selection
                    value={type && type.value}
                />
                {type && (
                    <React.Fragment>
                        <h3>Required fields:</h3>
                        {this.getFieldsList('required')}
                        <h3>Additional fields:</h3>
                        {this.getFieldsList('additional')}
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
