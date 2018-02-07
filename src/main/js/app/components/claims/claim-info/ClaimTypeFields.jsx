import React from 'react';
import PropTypes from 'prop-types';
import { Button, Dropdown, List } from 'semantic-ui-react';
import ClaimInfoField from './ClaimInfoField';

export default class ClaimTypeFields extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            fieldsData: {
                required: [],
                additional: []
            }
        };
    }

    inputHandler = (fieldType, name, input, e, { value }) => {
        const { fieldsData } = this.state;
        this.setState({
            fieldsData: {
                ...fieldsData,
                [fieldType]: {
                    ...fieldsData[fieldType],
                    [name]: value
                }
            }
        });
    };

    submitHandler = () => {
        const { submitTypeChanges } = this.props;
        submitTypeChanges(this.state.fieldsData);
    };

    render() {
        const { typesList, activeType, typeChangeHandler } = this.props;
        return (
            <React.Fragment>
                Type
                <Dropdown
                    onChange={typeChangeHandler}
                    options={typesList}
                    placeholder="Type"
                    selection
                    value={activeType.name}
                />
                <h3>Required fields:</h3>
                <List>
                    {activeType.required.map((item, id) => (
                        <List.Item key={id}>
                            <ClaimInfoField
                                fieldData={item}
                                inputHandler={this.inputHandler.bind(
                                    this,
                                    'required'
                                )}
                            />
                        </List.Item>
                    ))}
                </List>
                <h3>Additional fields:</h3>
                <List>
                    {activeType.additional.map((item, id) => (
                        <List.Item key={id}>
                            <ClaimInfoField
                                fieldData={item}
                                inputHandler={this.inputHandler.bind(
                                    this,
                                    'additional'
                                )}
                            />
                        </List.Item>
                    ))}
                </List>
                <Button primary onClick={this.submitHandler}>
                    Save
                </Button>
            </React.Fragment>
        );
    }
}

ClaimTypeFields.propTypes = {
    claimType: PropTypes.object.isRequired,
    typesList: PropTypes.array.isRequired,
    activeType: PropTypes.object.isRequired,
    typeChangeHandler: PropTypes.func.isRequired,
    submitTypeChanges: PropTypes.func.isRequired
};
