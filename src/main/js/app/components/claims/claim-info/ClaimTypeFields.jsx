import React from 'react';
import PropTypes from 'prop-types';
import { Dropdown, List } from 'semantic-ui-react';
import ClaimInfoField from './ClaimInfoField';

const ClaimTypeFields = ({
    claimType,
    typesList,
    activeType,
    typeChangeHandler
}) => (
    <React.Fragment>
        Type
        <Dropdown
            onChange={typeChangeHandler}
            options={typesList}
            placeholder="Type"
            selection
            value={activeType.name}
        />
        <h3>Required fileds:</h3>
        <List>
            {activeType.required.map((item, id) => (
                <List.Item key={id}>
                    <ClaimInfoField fieldData={item} />
                </List.Item>
            ))}
        </List>
        <h3>Additional fileds:</h3>
        <List>
            {activeType.additional.map((item, id) => (
                <List.Item key={id}>
                    <ClaimInfoField fieldData={item} />
                </List.Item>
            ))}
        </List>
    </React.Fragment>
);

ClaimTypeFields.propTypes = {
    claimType: PropTypes.object.isRequired,
    typesList: PropTypes.array.isRequired,
    activeType: PropTypes.object.isRequired,
    typeChangeHandler: PropTypes.func.isRequired
};

export default ClaimTypeFields;
