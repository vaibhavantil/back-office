import React from 'react';
import PropTypes from 'prop-types';
import { Button, Dropdown, List } from 'semantic-ui-react';
import ClaimInfoField from './ClaimInfoField';
import { updateTypesList, getActiveType } from 'app/lib/helpers';
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
        let typesList = [...types];
        if (claimInfo.details && claimInfo.type) {
            typesList = updateTypesList(typesList, claimInfo);
        }
        this.setState({
            types: typesList,
            type: getActiveType(typesList, claimInfo.type)
        });
    }

    componentWillReceiveProps({ types, claimInfo }) {
        const { claimInfo: { type } } = this.props;
        if (type !== claimInfo.type) {
            this.setState({
                type: getActiveType(types, claimInfo.type)
            });
        }
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
