import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { Dropdown } from 'semantic-ui-react';
import { claimStatus } from 'app/lib/selectOptions';
import ClaimTypeFields from './ClaimTypeFields';
import { fieldsToArray } from 'app/lib/helpers';

const ClaimInfoContainer = styled.div`
    display: flex;
    justify-content: space-around;
    margin: 100px;
    padding: 30px;
    border: solid 1px #ccc;
`;

const Column = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
`;

export default class ClaimInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            status: 'OPEN',
            type: ''
        };
    }

    statusChangeHandler = (e, { value }) => {
        const { claimUpdate, match } = this.props;
        const id = match.params.id;
        this.setState({ status: value });
        claimUpdate(id, { id, status: value }, 'status');
    };

    typeChangeHandler = (e, { value }) => {
        this.setState({ type: value });
    };

    submitTypeChanges = data => {
        const { match: { params }, claimUpdate, types } = this.props;
        const updates = fieldsToArray(data);
        const type = types.filter(item => item.name === this.state.type)[0];
        claimUpdate(params.id, { id: params.id, ...type, ...updates }, 'type');
    };

    getActiveType = () => {
        const { types } = this.props;
        return types.filter(item => item.name === this.state.type)[0];
    };

    componentDidMount() {
        const { userRequest, claimDetails: { data } } = this.props;
        if (data.userId) userRequest(data.userId);
        this.setState({
            status: data.status,
            type: data.type.name
        });
    }

    render() {
        const { user, types, claimDetails: { data } } = this.props;
        const { type, status } = this.state;
        const activeType = this.getActiveType();
        return (
            <React.Fragment>
                <ClaimInfoContainer>
                    <Column>
                        <span>Registration date: {data.registrationDate}</span>
                        <span>User: {user && user.firstName}</span>
                        <span>Audio:</span>
                        <audio src={data.url} controls />
                    </Column>

                    <Column>
                        Status
                        <Dropdown
                            onChange={this.statusChangeHandler}
                            options={claimStatus}
                            placeholder="Status"
                            selection
                            value={status}
                        />
                    </Column>
                </ClaimInfoContainer>
                <Column>
                    {type &&
                        types.length && (
                            <ClaimTypeFields
                                typesList={types}
                                activeType={activeType}
                                claimType={data.type}
                                typeChangeHandler={this.typeChangeHandler}
                                submitTypeChanges={this.submitTypeChanges}
                            />
                        )}
                </Column>
            </React.Fragment>
        );
    }
}

ClaimInfo.propTypes = {
    user: PropTypes.object,
    claimUpdate: PropTypes.func.isRequired,
    match: PropTypes.object.isRequired,
    types: PropTypes.array.isRequired,
    userRequest: PropTypes.func.isRequired,
    claimDetails: PropTypes.object.isRequired
};
